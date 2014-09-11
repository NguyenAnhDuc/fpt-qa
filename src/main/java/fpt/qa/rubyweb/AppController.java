package fpt.qa.rubyweb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import modifier.AbsoluteTime;
import modifier.AbsoluteTime.TimeResult;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fpt.ruby.App;
import com.fpt.ruby.crawler.CrawlPhimChieuRap;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.RubyAnswer;
import com.mongodb.BasicDBObject;

@Controller
@RequestMapping("/")
public class AppController {
	private App app;
	
	@PostConstruct
	public void init(){
		app = new App();
	}
	
	@RequestMapping(value="/listCachedQuestion", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<String> getListQuestion(){
		return app.getListCachedQuestion();
		//return "haha";
	}
	
	@RequestMapping(value="/getAnswer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RubyAnswer prototypeGetAnswer(@RequestParam("question") String question){
		return app.getAnswer(question);
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String home(Model model){
		List<String> questions = app.getListCachedQuestion();
		model.addAttribute("questions", questions);
		return "chat";
	}
	
	
	
	
	
	/*@RequestMapping(value="/allCinema", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BasicDBObject testMovie(){
		BasicDBObject result = new BasicDBObject();
		List<MovieTicket> movieTickets = mongoOperation.findAll(MovieTicket.class);
		Set<String> cinemas = new HashSet<String>();
		Set<String> movies = new HashSet<String>();
		for (MovieTicket movieTicket : movieTickets) {
			System.out.println(movieTicket.getCinema() + " " + movieTicket.getMovie());
			cinemas.add(movieTicket.getCinema());
			movies.add(movieTicket.getMovie());
		}
		result.append("cinemas", cinemas);
		result.append("movies", movies );
		return result;
	}*/
	@RequestMapping(value="/admin/crawlManual", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BasicDBObject crawlPhimChieuRap(@RequestParam("cin_name") String cinName, @RequestParam("mov_title") String movieTitle,
											@RequestParam("time") String time){
		try{
			
			String[] times = time.split("\\s+");
			for (String etime : times){
				System.out.println(etime.trim().split(":")[0]);
				System.out.println(etime.trim().split(":")[1]);
				MovieTicket movieTicket = new MovieTicket();
				movieTicket.setCinema(cinName.trim());
				movieTicket.setMovie(movieTitle.trim());
				Date date = new Date();
				date.setHours(Integer.parseInt(etime.trim().split(":")[0]));
				date.setMinutes(Integer.parseInt(etime.trim().split(":")[1]));
				date.setSeconds(0);
				movieTicket.setDate(date);
				movieTicketService.save(movieTicketService);
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
			return new BasicDBObject().append("status", "failed");
		}
		return new BasicDBObject().append("status", "success");
	}
}
