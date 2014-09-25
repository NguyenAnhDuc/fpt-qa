package com.fpt.ruby;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import com.fpt.ruby.conjunction.ConjunctionHelper;
import com.fpt.ruby.helper.ProcessHelper;
import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.nlp.AnswerMapper;
import com.fpt.ruby.service.MovieFlyService;
import com.fpt.ruby.service.mongo.MovieTicketService;

import fpt.qa.intent.detection.IntentConstants;
import fpt.qa.intent.detection.MovieIntentDetection;
import fpt.qa.mdnlib.struct.pair.Pair;

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
	
	public static String getMovieGenre(String question){
		List<Pair<String, String>> conjunctions = conjunctionHelper.getConjunction(question);
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println(conjunction.first + " | " + conjunction.second);
			if (conjunction.second.equals("loại_phim"))
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
	
	public static void test1() throws UnsupportedEncodingException{
		//init 
				conjunctionHelper = new ConjunctionHelper();
				movieFlyService = new MovieFlyService();
				movieTicketService = new MovieTicketService();
				String question = "đạo diễn phim Ninja  là ai?";
				question = "phim Lucy có hay không?";
				question = "diễn viên của phim vệ binh dải ngân hà là ai?";
				question = "tối nay rạp lotte landmark chiếu phim gì";
				question = "phim hài nào đang được chiếu rạp";
				question = "phim nào hay";
				question = "phim nào bắt đầu bằng chữ L đang chiếu rạp";
				
				MovieIntentDetection.init("data/qc", "data/dicts");
				String intent = MovieIntentDetection.getIntent(question);
				String questionType = AnswerMapper.getTypeOfAnswer(intent, question);
				System.out.println("intent: " + intent);
				System.out.println("questionType: " + questionType);
				
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
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		movieFlyService = new MovieFlyService();
		movieTicketService = new MovieTicketService();
		MovieIntentDetection.init("data/qc", "data/dicts");
		
		String question = "đạo diễn phim Ninja  là ai?";
		question = "phim Lucy có hay không?";
		question = "diễn viên của phim vệ binh dải ngân hà là ai?";
		question = "tối nay rạp lotte landmark chiếu phim gì";
		question = "phim hài nào đang được chiếu rạp";
		question = "phim nào hay";
		question = "phim nào bắt đầu bằng chữ L đang chiếu rạp";
		question = "phim nào được bình luận tốt nhất";
		question = "phim nào hay  nhất";
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Ruby answer: ");
		//System.out.println(ProcessHelper.getAnswer(question, movieFlyService, movieTicketService).getAnswer());
	}
}


