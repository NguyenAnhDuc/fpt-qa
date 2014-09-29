package com.fpt.ruby.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fpt.ruby.model.TVProgram;
import com.fpt.ruby.service.TVProgramService;


public class CrawlTV {
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
				tvProgram.setChannel(chanel);
				//tvProgram.setDate(date);
				tvProgram.setChannel(chanel);
				tvProgram.setStart_date(date);
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
	/*	List<TVProgram> tvPrograms = crawlChanel("vtv1", "http://vtv.vn/truyen-hinh-truc-tuyen/vtv1.htm");
		TVProgramService tvProgramService = new TVProgramService();
		for (TVProgram tvProgram : tvPrograms){
			tvProgramService.save(tvProgram);
			System.out.println(tvProgram.getTitle() + " | " + tvProgram.getType() + " | " + tvProgram.getStart_date().toLocaleString());
		}
				*/
	}
}
