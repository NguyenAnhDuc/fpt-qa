package fpt.qa.rubyweb;

import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.model.MovieTicket;

public class JsoupTest {
	private static MongoOperations mongoOperation;
	public static void init(){
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	}
	
	private static void showMovie(MovieTicket movieTicket){
		System.out.println("Movie: " + movieTicket.getMovie());
		System.out.println("Cinema: " + movieTicket.getCinema());
		System.out.println("Date: " + movieTicket.getDate());
		System.out.println("Type: " + movieTicket.getType());
		System.out.println("------------------------------");
	}
	
	public static void crawlCinema(String city, String cinema,String url,int date) throws Exception{
		System.out.println("url: " + url);
		System.out.println("Crawling cinema: " + cinema);
		url += "?date=" + date;
		Document doc = Jsoup.connect(url).timeout(100000).get();
		Elements newsHeadlines = doc.select("#ShowtimeItemResult");
		int count = 0;
		for (Element element : newsHeadlines) {
			count ++;
			MovieTicket movieTicket = new MovieTicket();
			Element headElement =  element.getElementsByClass("head").first();
			//System.out.print(headElement.select("span").select("a").last().text());
			
			
			//2D
			Elements btnDigitalElenments = element.select("#BoxTime").first()
												  .select(".TimesContent").first()
												  .select(".content").first()
												  .select(".bntDigital");
			
			
			for (Element element2 : btnDigitalElenments) {
				String time = element2.select("span").first().text().trim();
				movieTicket.setCity(city);
				movieTicket.setType("2D");
				movieTicket.setCinema(cinema);
				movieTicket.setMovie(headElement.select("span").select("a").last().attr("title"));
				Date dateTicket = new Date();
				dateTicket.setDate(dateTicket.getDate() + date);
				dateTicket.setHours(Integer.parseInt(time.split(":")[0]));
				dateTicket.setMinutes(Integer.parseInt(time.split(":")[1]));
				dateTicket.setSeconds(0);
				movieTicket.setDate(dateTicket);
				mongoOperation.save(movieTicket);
				//showMovie(movieTicket);
			}

			//3D
			Elements bnt3dElenments = element.select("#BoxTime").first()
												  .select(".TimesContent").first()
												  .select(".content").first()
												  .select(".bnt3D");
			
			for (Element element2 : bnt3dElenments) {
				String time = element2.select("span").first().text().trim();
				movieTicket.setType("3D");
				movieTicket.setCity(city);
				movieTicket.setCinema(cinema);
				movieTicket.setMovie(headElement.select("span").select("a").last().attr("title"));
				Date dateTicket = new Date();
				dateTicket.setDate(dateTicket.getDate() + date);
				dateTicket.setHours(Integer.parseInt(time.split(":")[0]));
				dateTicket.setMinutes(Integer.parseInt(time.split(":")[1]));
				dateTicket.setSeconds(0);
				movieTicket.setDate(dateTicket);
				mongoOperation.save(movieTicket);
				//showMovie(movieTicket);
			}
			
		}
	}
	
	
	
	public static void testMovieTicket(){
		MovieTicket movieTicket = new MovieTicket();
		movieTicket.setCinema("lotte");
		movieTicket.setMovie("x-men");
		movieTicket.setDate(new Date());
		mongoOperation.save(movieTicket);
	}
	
	public static void showMovieTickets(){
		List<MovieTicket> movieTickets = mongoOperation.findAll(MovieTicket.class);
		int  count = 0;
		for (MovieTicket movieTicket : movieTickets){
			System.out.println(movieTicket.getCinema() + " " + movieTicket.getMovie() 
								+ " " + movieTicket.getType()  + " "+ movieTicket.getDate());
			count ++;
			//if (count > 100) break;
		}
	}
	
	public static void crawlHaNoi() throws Exception{
		mongoOperation.dropCollection(MovieTicket.class);
		String url = "http://phimchieurap.vn/lich-chieu/ha-noi";
		Document doc = Jsoup.connect(url).timeout(100000).get();
		Elements cinemas = doc.getElementById("ctl00_ContentPlaceHolder1_ddlrap").children();
		for (Element cinema : cinemas) {
			String  cinemaLink = cinema.attr("value");
			if (!cinemaLink.equals("0")){
				String cinemaName = cinema.text().trim();
				
				String city = "ha-noi";
				String cinemaUrl = url + "/rap/" + cinemaLink;
				int date = 0;
				crawlCinema(city, cinemaName, cinemaUrl, date);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		init();
		//testTime();
		//testMovieTicket();
		// query to search user
		/*Query searchMovieTicketQuery = new Query(Criteria.where("cinema").is("lotte"));
		MovieTicket movieTicket = mongoOperation.findOne(searchMovieTicketQuery, MovieTicket.class);
		System.out.println(movieTicket.getDate().getDate());*/
		//crawlCinema("Ha Noi","lotte landmark","http://phimchieurap.vn/lich-chieu/ha-noi/rap/lotte-cinema-landmark", 0);
		showMovieTickets();
		//crawlHaNoi();
	}
}
