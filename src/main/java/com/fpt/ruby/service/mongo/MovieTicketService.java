package com.fpt.ruby.service.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.model.MovieTicket;

public class MovieTicketService {
	private MongoOperations mongoOperations;
	public MovieTicketService(MongoOperations mongoOperations){
		this.mongoOperations = mongoOperations;
	}
	
	public MovieTicketService(){
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
	}
	
	public List<MovieTicket> findAll(){
		return mongoOperations.findAll(MovieTicket.class);
	}
	
	public List<MovieTicket> findMoviesMatchCondition(MovieTicket matchMovieTicket,Date beforeDate, Date afterDate){
		List<MovieTicket> movieTickets = mongoOperations.findAll(MovieTicket.class);
		List<MovieTicket> results = new ArrayList<MovieTicket>();
		List<MovieTicket> matches = new ArrayList<MovieTicket>();
		for (MovieTicket movieTicket : movieTickets){
			if ( (matchMovieTicket.getCinema() == null || (movieTicket.getCinema().toLowerCase().contains(matchMovieTicket.getCinema().toLowerCase()))) 
			 &&	 (matchMovieTicket.getCity() == null || (movieTicket.getCity().equals(matchMovieTicket.getCity())))
			 &&  (matchMovieTicket.getMovie() == null || (movieTicket.getMovie().toLowerCase().contains(matchMovieTicket.getMovie().toLowerCase())))
			 &&  (matchMovieTicket.getType() == null || (movieTicket.getType().equals(matchMovieTicket.getType())))
			 &&  (matchMovieTicket.getDate() == null || (movieTicket.getDate().equals(matchMovieTicket.getDate()))) )
		    matches.add(movieTicket);
		}
		if (beforeDate == null && afterDate == null){
			return matches;
		}
		else if (beforeDate == null && afterDate != null){
			for (MovieTicket movieTicket : matches){
				if (movieTicket.getDate().before(afterDate))
					results.add(movieTicket);
			}
			return results;
		}
		else if (beforeDate != null && afterDate == null ){
			for (MovieTicket movieTicket : matches){
				if (movieTicket.getDate().after(beforeDate))
					results.add(movieTicket);
			}
			return results;
		}
		else {
			for (MovieTicket movieTicket : matches){
				if (movieTicket.getDate().before(afterDate) && movieTicket.getDate().after(beforeDate))
					results.add(movieTicket);
			}
			return results;
		}
		
	}
}
