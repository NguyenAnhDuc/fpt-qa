package com.fpt.ruby.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fpt.ruby.helper.HttpHelper;
import com.fpt.ruby.model.MovieFly;

public class MovieFlyService {
	private static final String RT_apikey = "squrt6un22xe46uy5wmxej8e";
	
	
	private static List<String> searchOnRT(String title) throws UnsupportedEncodingException{
		List<String> imdbIds = new ArrayList<String>();
		String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q="+URLEncoder.encode(title,"UTF-8")
					+"&apikey=" + RT_apikey;
		try{
			String jsonRTString = HttpHelper.sendGet(url);
			JSONObject jsonRT = new JSONObject(jsonRTString);
			JSONArray movies = jsonRT.getJSONArray("movies");
			for (int i=0;i<movies.length();i++){
				JSONObject movieJson = movies.getJSONObject(i);
				imdbIds.add("tt"+movieJson.getJSONObject("alternate_ids").getString("imdb"));
			}
		}
		catch (Exception ex){
			System.out.println("Exception: " + ex.getMessage());
		}
		return imdbIds;
	}
	
	public  MovieFly searchOnImdbByTitle(String id){
		MovieFly movieFly = new MovieFly();
		String url = "http://www.omdbapi.com/?i=" + id;
		try{
			String jsonImdbString = HttpHelper.sendGet(url);
			System.out.println(jsonImdbString);
			JSONObject jsonImdb = new JSONObject(jsonImdbString);
			movieFly.setTitle(jsonImdb.getString("Title"));
			movieFly.setGenre(jsonImdb.getString("Genre"));
			movieFly.setYear(Integer.parseInt(jsonImdb.getString("Year")));
			movieFly.setActor(jsonImdb.getString("Actors"));
			movieFly.setRuntime(jsonImdb.getString("Runtime"));
			movieFly.setImdbRating(Float.parseFloat(jsonImdb.getString("imdbRating")));
			movieFly.setImdbId(jsonImdb.getString("imdbID"));
			movieFly.setWriter(jsonImdb.getString("Writer"));
			movieFly.setAwards(jsonImdb.getString("Awards"));
			movieFly.setCountry(jsonImdb.getString("Country"));
			movieFly.setLanguage(jsonImdb.getString("Language"));
			movieFly.setDirector(jsonImdb.getString("Director"));
			movieFly.setPlot(jsonImdb.getString("Plot"));
			
			SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
			java.util.Date utilDate = format.parse(jsonImdb.getString("Released"));
			movieFly.setReleased(new Date(utilDate.getTime()));
		}
		catch (Exception ex){
			System.out.println("Exception ex: " + ex.getMessage());
		}
		return movieFly;
	}
	
	public List<MovieFly> searchOnImdb(String title) throws UnsupportedEncodingException {
		List<MovieFly> movieFlies = new ArrayList<MovieFly>();
		List<String> imdbIds = searchOnRT(title);
		for (String imdbId : imdbIds){
			MovieFly movieFly = searchOnImdbByTitle(imdbId);
			movieFlies.add(movieFly);
		}
		return movieFlies;
	}
	
	public List<MovieFly> searchOnImdb(List<String> titles) throws UnsupportedEncodingException {
		List<MovieFly> movieFlies = new ArrayList<MovieFly>();
		
		for (String t : titles){
			movieFlies.addAll(searchOnImdb(t));
		}
		return movieFlies;
	}
	
	public static void main(String[] args) throws Exception {
		MovieFlyService movieFlyService = new MovieFlyService();
		List<MovieFly> movieFlies = movieFlyService.searchOnImdb("Starred Up");
		for (MovieFly movieFly : movieFlies){
			System.out.println("Genre: " + movieFly.getGenre());
			System.out.println("Imdb Rating: " + movieFly.getImdbRating());
		}
	}

}
