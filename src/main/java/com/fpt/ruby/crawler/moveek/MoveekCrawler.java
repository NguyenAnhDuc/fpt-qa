package com.fpt.ruby.crawler.moveek;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jmdn.struct.pair.Pair;

import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.service.mongo.MovieTicketService;

public class MoveekCrawler {

	private static HashMap<String, String> movie_urls = new HashMap<String, String>();
	private static HashMap<String, String> showtime_urls = new HashMap<String, String>();
	private static HashMap<String, String> cin_cities = new HashMap<String, String>();
	
	static {
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath() + "/dicts";
		loadMovieUrls(dir);
		loadshowTimeUrls(dir);
		loadCinCity(dir);
	}
	
	public static void doCrawl(MovieTicketService mts){
		Object[] keys = movie_urls.keySet().toArray();
		for (Object key : keys){
			String cinName = (String)key;
			String city = cin_cities.get(cinName);
			
			List<Pair<String, String>>	movies = MoveekCrawlerUtils.GetMoviesByCinemas(movie_urls.get(cinName), "");
			for (Pair<String, String> movie : movies){
				List<Pair<String, Date>> slots = MoveekCrawlerUtils.getSessionTime(showtime_urls.get(cinName), movie.first);
				
				for (Pair<String, Date> slot : slots){
					MovieTicket newTicket = new MovieTicket();
					newTicket.setType(slot.first);
					newTicket.setDate(slot.second);
					newTicket.setCinema(cinName);
					newTicket.setCity(city);
					newTicket.setMovie(movie.second);
					if (!mts.existedInDb(newTicket)){
						mts.save(newTicket);
					}
				}
			}
		}
	}
	

	private static void loadMovieUrls(String dir){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir + "/moveek_movie_url.txt"));
			
			String line;
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (idx < 0 || line.isEmpty()){
					continue;
				}
				
				String name = line.substring(0, idx);
				String url = line.substring(idx + 1);
				movie_urls.put(name, url);
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	private static void loadshowTimeUrls(String dir){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir + "/moveek_showtime_url.txt"));
			
			String line;
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (idx < 0 || line.isEmpty()){
					continue;
				}
				
				String name = line.substring(0, idx);
				String url = line.substring(idx + 1);
				showtime_urls.put(name, url);
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	private static void loadCinCity(String dir){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir + "/moveek_city.txt"));
			
			String line;
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (idx < 0 || line.isEmpty()){
					continue;
				}
				
				String name = line.substring(0, idx);
				String url = line.substring(idx + 1);
				cin_cities.put(name, url);
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
