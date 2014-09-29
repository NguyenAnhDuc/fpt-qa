package com.fpt.ruby.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fpt.ruby.model.Channel;
import com.fpt.ruby.model.TVProgram;
import com.fpt.ruby.service.TVProgramService;

@Component
public class CrawlerMyTV {

	public static String sendGet(String url) throws Exception{
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
	 
		// add request header
		HttpResponse response = client.execute(request);
	 
		System.out.println("Response Code : " 
	                + response.getStatusLine().getStatusCode());
	 
		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));
	 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}
	
	
	public List<Channel> getChanel() throws Exception{
		List< Channel > channels = new ArrayList< Channel >();
		Document doc = Jsoup.parse(sendGet("http://www.mytv.com.vn/lich-phat-song"));
		Element chanel = doc.getElementById("channelId");
		
		Elements chanElements = chanel.select("option");
		for (Element element : chanElements){
			Channel channel = new Channel();
			channel.setId(element.val());
			channel.setName(element.text());
			channels.add(channel);
		}
		return channels;
	}
	
	public void crawlMyTV(TVProgramService tvProgramService) throws Exception{
		System.out.println("Crawling from MYTV");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String date = df.format(new Date(System.currentTimeMillis()));
		List< Channel > channels = getChanel();
		for (Channel channel : channels){
				try{
					System.out.println("Crawling from " + channel.getName());
					List< TVProgram > tvPrograms =  crawlChannel( channel, date);
					tvPrograms = calculateEndTime( tvPrograms );
					for (TVProgram tvProgram : tvPrograms){
						tvProgramService.save( tvProgram );
					}
				}
				catch (Exception ex){
					ex.printStackTrace();
					continue;
				}
		}
	}
	
	public List<TVProgram> calculateEndTime(List<TVProgram> tvPrograms){
		List< TVProgram > results = new ArrayList< TVProgram >();
		for (int i=0;i<tvPrograms.size()-1;i++){
			TVProgram tvProgram = new TVProgram();
			tvProgram = tvPrograms.get( i );
			tvProgram.setEnd_date( tvPrograms.get( i+1 ).getStart_date() );
			results.add( tvProgram );
		}
		return results;
	}
	
	public  List<TVProgram> crawlChannel(Channel channel, String date) throws Exception{
		List< TVProgram > tvPrograms = new ArrayList< TVProgram >();
		String url="http://www.mytv.com.vn/module/ajax/ajax_get_schedule.php?channelId=" + channel.getId() 
					+ "&dateSchedule=" + date;
		System.out.println(url);
		Document doc = Jsoup.parse(sendGet(url));
		Elements elements = doc.select("p");
		for (Element element : elements){
			String tmp = element.text().replace("<\\/strong>", "").replace("<\\/p>", "");
			String time = tmp.substring(0, 5);
			String programName = tmp.substring(5,tmp.length());
			//System.out.println("Time: " + time + " | " + "Program Name: " + StringEscapeUtils.unescapeJava(programName));
			String[] times = time.split(":");
			Date channelDate = new Date();
			channelDate.setHours(Integer.parseInt(times[0]));
			channelDate.setMinutes(Integer.parseInt(times[1]));
			TVProgram tvProgram = new TVProgram();
			tvProgram.setChannel( channel.getName() );
			tvProgram.setTitle( StringEscapeUtils.unescapeJava(programName) );
			tvProgram.setStart_date( channelDate );
			tvPrograms.add( tvProgram );
		}
		return tvPrograms;
	}
	
	
	/*public static List<TVProgram> crawlChanel(String chanel, String url){
		List<TVProgram> tvPrograms = new ArrayList<TVProgram>();
		try{
			Document doc = Jsoup.parse(sendGet(url));
			Elements elements = doc.getElementsByClass("haszone");
			for (Element element : elements){
				TVProgram tvProgram = new TVProgram();
				String time = element.getElementsByClass("thoigian").first().html();
				String programTitle = element.select("h5").first().ownText();
				String programType = element.getElementsByClass("red-play").attr("zonename");
				String[] times = time.split(":");
				Date date = new Date();
				date.setHours(Integer.parseInt(times[0]));
				date.setMinutes(Integer.parseInt(times[1]));
				tvProgram.setChanel(chanel);
				tvProgram.setDate(date);
				tvProgram.setTitle(programTitle);
				tvProgram.setType(programType);
				tvPrograms.add(tvProgram);
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
			return tvPrograms;
		}
		return tvPrograms;
	}*/
	
	public static void main(String[] args) throws Exception {
		/*List<TVProgram> tvPrograms = crawlChanel("vtv1", "http://vtv.vn/truyen-hinh-truc-tuyen/vtv1.htm");
		for (TVProgram tvProgram : tvPrograms){
			System.out.println(tvProgram.getTitle() + " | " + tvProgram.getType() + " | " + tvProgram.getDate().toLocaleString());
		}*/
		//getChanel();
		//crawlChannel("1","26/09/2014");
		List< Channel > channels = new ArrayList< Channel >();
		Document doc = Jsoup.parse(sendGet("http://www.mytv.com.vn/lich-phat-song"));
		Element chanel = doc.getElementById("channelId");
		
		Elements chanElements = chanel.select("option");
		for (Element element : chanElements){
			Channel channel = new Channel();
			channel.setId(element.val());
			channel.setName(element.text());
			channels.add(channel);
			if (channel.getId() == "161"){

			}
		}
				
	}
}
