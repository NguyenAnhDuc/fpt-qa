package com.fpt.ruby;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import com.fpt.ruby.conjunction.ConjunctionHelper;
import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.nlp.AnswerMapper;
import com.fpt.ruby.service.MovieFlyService;
import com.fpt.ruby.service.mongo.MovieTicketService;

import fpt.qa.intent.detection.IntentConstants;
import fpt.qa.intent.detection.MovieIntentDetection;
import mdnlib.struct.pair.Pair;

public class TestAllFlow {
	private static ConjunctionHelper conjunctionHelper;
	private static MovieFlyService movieFlyService;
	private static MovieTicketService movieTicketService;
	public static String getMovieTitle(String question){
		List<Pair<String, String>> conjunctions = conjunctionHelper.getConjunction(question);
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println(conjunction.first + " | " + conjunction.second);
			if (conjunction.second.equals(IntentConstants.MOV_TITLE))
				return conjunction.first.replace("{", "").replace("}", "");
		}
		return null;
	}
	
	public static MovieTicket getMovieTicket(String question){
		List<Pair<String, String>> conjunctions = conjunctionHelper.getConjunction(question);
		MovieTicket movieTicket = new MovieTicket();
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println(conjunction.first + " | " + conjunction.second);
			if (conjunction.second.equals(IntentConstants.CIN_NAME))
				movieTicket.setCinema(conjunction.first.replace("{", "").replace("}", ""));
			if (conjunction.second.equals(IntentConstants.MOV_TITLE))
				movieTicket.setMovie(conjunction.first.replace("{", "").replace("}", ""));
		}
		return movieTicket;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		//init 
		conjunctionHelper = new ConjunctionHelper();
		movieFlyService = new MovieFlyService();
		movieTicketService = new MovieTicketService();
		String question = "đạo diễn phim Ninja  là ai?";
		question = "phim Lucy có hay không?";
		question = "diễn viên của phim vệ binh dải ngân hà là ai?";
		question = "tối nay rạp lotte landmark chiếu phim gì";
		MovieIntentDetection.init("data/qc", "data/dicts");
		String intent = MovieIntentDetection.getIntent(question);
		String questionType = AnswerMapper.getTypeOfAnswer(intent);
		
		// static question
		if (questionType.equals(AnswerMapper.Static_Question)){
			String movieTitle = getMovieTitle(question);
			System.out.println("Movie Title: " + movieTitle);
			List<MovieFly> movieFlies = movieFlyService.searchOnImdb(movieTitle);
			String answer = AnswerMapper.getStaticAnswer(intent, movieFlies);
			System.out.println("Answer: " + answer);
		}
		else {
			MovieTicket matchMovieTicket = getMovieTicket(question);
			Date beforeDate = new Date();
			beforeDate = null;
			Date afterDate = new Date();
			afterDate = null;
			List<MovieTicket> movieTickets = movieTicketService.findMoviesMatchCondition(matchMovieTicket, beforeDate, afterDate);
			String answer = AnswerMapper.getDynamicAnswer(intent, movieTickets);
			System.out.println("Answer: " + answer);
		}
	}
}

