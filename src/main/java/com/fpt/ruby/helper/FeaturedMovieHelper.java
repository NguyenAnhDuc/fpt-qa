package com.fpt.ruby.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.nlp.AnswerMapper;

public class FeaturedMovieHelper {
	private static Map<String, String> genreMap = new HashMap<String, String>();
	private static Map<String, String> countryMap = new HashMap<String, String>();
	private static Map<String, String> langMap = new HashMap<String, String>();
	
	static {
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		loadGenreMap(dir + "/dicts/genreMap.txt");
		loadCountryMap(dir + "/dicts/countryMap.txt");
		loadLangMap(dir + "/dicts/languageMap.txt");
	}
	
	private static void loadGenreMap(String fileIn){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileIn));
			String line;
			
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (line.isEmpty() || idx < 0){
					continue;
				}
				genreMap.put(line.substring(0, idx), line.substring(idx+1));
			}
			reader.close();
		} catch (IOException e){
			
		}
	}
	
	private static void loadCountryMap(String fileIn){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileIn));
			String line;
			
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (line.isEmpty() || idx < 0){
					continue;
				}
				countryMap.put(line.substring(0, idx), line.substring(idx+1));
			}
			reader.close();
		} catch (IOException e){
			
		}
	}
	
	private static void loadLangMap(String fileIn){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileIn));
			String line;
			
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (line.isEmpty() || idx < 0){
					continue;
				}
				langMap.put(line.substring(0, idx), line.substring(idx+1));
			}
			reader.close();
		} catch (IOException e){
			
		}
	}
	
	public static String filterByDirector(String director, List<MovieFly> movieFlies){
		String movTitles = "";
		for (MovieFly mf : movieFlies){
			if (mf.getDirector().contains(director)){
				movTitles += mf.getTitle() + ", ";
			}
		}
		
		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return "phim " + movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByActor(String actor, List<MovieFly> movieFlies){
		String movTitles = "";
		for (MovieFly mf : movieFlies){
			if (mf.getActor().contains(actor)){
				movTitles += mf.getTitle() + ", ";
			}
		}

		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return "phim " + movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByImdb(List<MovieFly> movieFlies){
		float highest = 0;
		String title = "";
		for (MovieFly mf : movieFlies){
			if (mf.getImdbRating() > highest){
				highest = mf.getImdbRating();
				title = mf.getTitle();
			}
		}

		if (title.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return "phim " + title + " có imdb rating cao nhất";
	}
	
	public static String filterByCountry(String country, List<MovieFly> movieFlies){
		String movTitles = "";
		String lookupKey = country;
		if (countryMap.containsKey(country)){
			lookupKey = countryMap.get(country);
		}
		for (MovieFly mf : movieFlies){
			if (mf.getCountry().equals(lookupKey)){
				movTitles += mf.getTitle() + ", ";
			}
		}

		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return "phim " + movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByLang(String lang, List<MovieFly> movieFlies){
		String movTitles = "";
		String lookupKey = lang;
		if (langMap.containsKey(lang)){
			lookupKey = langMap.get(lang);
		}
		for (MovieFly mf : movieFlies){
			if (mf.getLanguage().equals(lookupKey)){
				movTitles += mf.getTitle() + ", ";
			}
		}
		
		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return "phim " + movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByGenre(String genre, List<MovieFly> movieFlies){
		String movTitles = "";
		String lookupKey = genre;
		if (langMap.containsKey(genre)){
			lookupKey = langMap.get(genre);
		}
		for (MovieFly mf : movieFlies){
			if (mf.getGenre().equals(lookupKey)){
				movTitles += mf.getTitle() + ", ";
			}
		}

		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return "phim " + movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByAward(String award, List<MovieFly> movieFlies){
		String movTitles = "";
		for (MovieFly mf : movieFlies){
			if (mf.getAwards() != null){
				movTitles += mf.getTitle() + ", ";
			}
		}

		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return "phim " + movTitles.substring(0, movTitles.length() - 2) + " đã nhận được giải thưởng";
	}
	
}
