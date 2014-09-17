package fpt.qa.rubyweb;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fpt.ruby.helper.*;
import com.fpt.ruby.model.Genre;
import com.fpt.ruby.model.Movie;
import com.fpt.ruby.service.GenreService;
import com.fpt.ruby.service.MovieService;
import com.fpt.ruby.service.ProductionCompanyService;
import com.fpt.ruby.service.ProductionCountryService;
import com.fpt.ruby.service.SpokenLanguageService;

public class MovieCrawl {
	private static GenreService genreService;
	private static ProductionCompanyService productionCompanyService;
	private static ProductionCountryService productionCountryService;
	private static SpokenLanguageService spokenLanguageService;
	private static MovieService movieService;
	
	public static void testDiscover() throws Exception{
		String jsonString = HttpHelper.sendGet("http://api.themoviedb.org/3/discover/movie?api_key=97081040da72479871eba2f996c0ea2d&page=1001");
		JSONObject jsonArray = new JSONObject(jsonString);
		JSONArray results = jsonArray.getJSONArray("results");
		System.out.println(results.length());
		for (int i=0;i<results.length();i++) {
			JSONObject result = results.getJSONObject(i);
			System.out.println(result.get("original_title"));
		}
	}
	
	
	
	public static void crawlMovie(int theMovieDbId) throws Exception{
		System.out.println("Crawling Movie " + theMovieDbId);
		String url = "https://api.themoviedb.org/3/movie/" +  theMovieDbId + "?api_key=97081040da72479871eba2f996c0ea2d";
		String jsonString = HttpHelper.sendGet(url);
		JSONObject json = new JSONObject(jsonString);
		Movie movie = extractMovie(json, genreService, productionCompanyService, productionCountryService, spokenLanguageService);
		movieService.saveMovie(movie);
	}
	
	
	
	public static Movie extractMovie(JSONObject json, GenreService genreService,ProductionCompanyService productionCompanyService,
			ProductionCountryService productionCountryService, SpokenLanguageService spokenLanguageService){
		ExtractHelper extractHelper = new ExtractHelper();
		Movie movie = new Movie();
		if (!json.get("adult").equals(null)) movie.setAdult(json.getBoolean("adult"));
		if (!json.get("budget").equals(null)) movie.setBudget(json.getDouble("budget"));
		if (!json.get("genres").equals(null)) movie.setGenres(extractHelper.extractGenre(json.getJSONArray("genres"),genreService));
		if (!json.get("homepage").equals(null) ) movie.setHomepage(json.getString("homepage"));
		if (!json.get("id").equals(null)) movie.setThemovie_id(json.getInt("id"));
		if (!json.get("imdb_id").equals(null)) movie.setImdb_id(json.getString("imdb_id"));
		if (!json.get("original_title").equals(null)) movie.setOriginal_title(json.getString("original_title"));
		if (!json.get("overview").equals(null)) movie.setOverview(json.getString("overview"));
		if (!json.get("popularity").equals(null)) movie.setPopularity(json.getDouble("popularity"));
		if (!json.get("production_companies").equals(null))
			movie.setProduction_companies(extractHelper.extractProductionCompany(json.getJSONArray("production_companies"),productionCompanyService));
		if (!json.get("production_countries").equals("production_countries"))
			movie.setProduction_countries(extractHelper.extractProductionCountry(json.getJSONArray("production_countries"), productionCountryService));
		try{
			if (!json.get("release_date").equals(null))
				movie.setRelease_date(extractHelper.extractDate(json.getString("release_date")));
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		if (!json.get("revenue").equals(null)) movie.setRevenue(json.getDouble("revenue"));
		if (!json.get("runtime").equals(null)) movie.setRuntime(json.getInt("runtime"));
		if (!json.get("spoken_languages").equals(null)) movie.setSpoken_languages(extractHelper.extractSpokenLanguage(json.getJSONArray("spoken_languages"),spokenLanguageService));
		if (!json.get("status").equals(null)) movie.setStatus(json.getString("status"));
		if (!json.get("tagline").equals(null)) movie.setTagline(json.getString("tagline"));
		if (!json.get("title").equals(null)) movie.setTitle(json.getString("title"));
		
		return movie;
	}
	
	public static void testMovie() throws Exception{
		Movie movie = movieService.findMovieById(100);
		System.out.println("Title: " + movie.getTitle());
		System.out.println("Overview: " + movie.getOverview());
		System.out.println("Tag line: " + movie.getTagline());
	}
	
	public static void testFindMatchTitle(){
		List<Movie> movies = movieService.findMovieMatchTitle("Baiju Bawra");
		int count = 0;
		for (Movie movie : movies){
			System.out.println("Id: " + movie.getId());
			System.out.println("Title: " + movie.getTitle());
			System.out.println("Overview: " + movie.getOverview());
			System.out.println("Tag line: " + movie.getTagline());
			for (Genre genre : movie.getGenres()){
				System.out.println("Genre: " + genre.getName());
			}
			count ++;
			if (count > 10) break;
		}
	}
	
	public static void crawlPage(int page) throws Exception{
		String url = "http://api.themoviedb.org/3/discover/movie"
					+ "?api_key=97081040da72479871eba2f996c0ea2d"+"&page=" + page;
		String jsonString = HttpHelper.sendGet(url);
		JSONObject json  = new JSONObject(jsonString);
		JSONArray results = json.getJSONArray("results");
		for (int i=0;i<results.length();i++){
			JSONObject result = results.getJSONObject(i);
			int theMovieDbId = result.getInt("id");
			crawlMovie(theMovieDbId);
		}
	}
	
	public static void crawlAll(int minPage,int maxPage) throws Exception{
		for (int i=minPage;i<=maxPage;i++){
			System.out.println("Crawling Page " + i);
			crawlPage(i);
		}
	}
	
	public static void extractAllMovieTitle(){
		List<Movie> movies = movieService.findAll();
		StringBuilder movieTitles = new StringBuilder();
		for (Movie movie : movies){
			movieTitles.append(movie.getTitle());
			movieTitles.append("\n");
		}
	}
	
	public static void init(){
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		movieService = (MovieService) context.getBean("movieService");
		genreService = (GenreService) context.getBean("genreService");
		productionCompanyService = (ProductionCompanyService) context.getBean("productionCompanyService");
		productionCountryService = (ProductionCountryService) context.getBean("productionCountryService");
		spokenLanguageService = (SpokenLanguageService) context.getBean("spokenLanguageService");
		System.out.println("LOAD CONTEXT COMPLETE!");
	}
	
	public static void main(String[] args) throws Exception {
		init();
		//testMovie();
		testFindMatchTitle();
		//testDiscover();
	}
}
