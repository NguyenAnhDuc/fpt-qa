package com.fpt.ruby;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.helper.ProcessHelper;
import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.service.MovieFlyService;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.fpt.ruby.service.mongo.QuestionStructureService;

public class App {
	MongoOperations mongoOperations;
	QuestionStructureService questionStructureService;
	MovieTicketService movieTicketService;
	MovieFlyService movieFlyService;
	public App(QuestionStructureService questionStructureService, MovieTicketService movieTicketService, MovieFlyService movieFlyService){
		this.questionStructureService = questionStructureService;
		this.movieTicketService = movieTicketService;
		this.movieFlyService = movieFlyService;
	}
	
	public App(){
		this.questionStructureService = new QuestionStructureService();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
		this.movieTicketService = new MovieTicketService(mongoOperations);
		this.movieFlyService = new MovieFlyService();
	}
	

	public RubyAnswer getAnswer(String question){
		String key = NlpHelper.normalizeQuestion(question);
		RubyAnswer rubyAnswer = new RubyAnswer();
		//rubyAnswer.setInCache(this.questionStructureService.isInCache(key));
		//rubyAnswer.setQuestion(question);
		// Process question
		//QuestionStructure questionStructure = ProcessHelper.getQuestionStucture(question, questionStructureService );
		//QuestionStructure questionStructure = new QuestionStructure();
		// Process answer
		rubyAnswer =  ProcessHelper.getAnswer(question,movieFlyService,movieTicketService);
		return rubyAnswer;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String question = "đạo diễn phim Lucy là ai";
		App app = new App();
		RubyAnswer answer = app.getAnswer(question);
		System.out.println("Answer: " + answer.getAnswer());
		//testGetTicket();
		//testQueryStaticMovie();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static void testQueryStaticMovie() throws UnsupportedEncodingException{
		App app = new App();
		String title = "Guardians of the galaxy";
		List<MovieFly> movieFlies = app.movieFlyService.searchOnImdb(title);
		for (MovieFly movieFly : movieFlies){
			System.out.println("Genre: " + movieFly.getGenre());
			System.out.println("Imdb Rating: " + movieFly.getImdbRating());
			System.out.println("Actors: " + movieFly.getActor());
			System.out.println("Director: " + movieFly.getDirector());
			System.out.println("Plot: " + movieFly.getPlot());
		}
	}
	
	private static void testGetTicket(){
		App app = new App();
		MovieTicket matchMovieTicket = new MovieTicket();
		matchMovieTicket.setCinema("Lotte Cinema Landmark");
		Date beforeDate = new Date();
		Date afterDate = new Date();
		System.out.println(beforeDate.equals(afterDate));
		List<MovieTicket> movieTickets = new ArrayList<MovieTicket>(); 
		movieTickets = app.movieTicketService.findMoviesMatchCondition(matchMovieTicket, beforeDate, afterDate);
	
		//List<MovieTicket> movieTickets = app.movieTicketService.findAll();
		/*Set<String> movieNames = new HashSet<String>();
		
		for (MovieTicket movieTicket : movieTickets) {
			movieNames.add(movieTicket.getMovie());
		}
		
		for (String movieName : movieNames){
			System.out.println(movieName);
		}*/
		for (MovieTicket movieTicket : movieTickets){
			System.out.println("Cinema: " + movieTicket.getCinema());
			System.out.println("Movie: " + movieTicket.getMovie());
			System.out.println("Date: " + movieTicket.getDate());
			System.out.println("---------------------------------");
		}
	}
	
	public List<String> getListCachedQuestion() {
		List<QuestionStructure> allQuestion = this.questionStructureService.allQuestionStructures();
		List<String> listCachedQuestion = new ArrayList<String>();
		int count = 0;
		for (QuestionStructure questionStructure : allQuestion) {
			System.out.println(questionStructure.getKey());
			listCachedQuestion.add(questionStructure.getKey());
			count ++;
			if (count > 50) break;
		}
		return listCachedQuestion;
	}
	
	public MovieFlyService getMovieFlyService(){
		return movieFlyService;
	}
}
