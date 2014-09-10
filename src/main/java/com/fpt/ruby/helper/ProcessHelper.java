package com.fpt.ruby.helper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.model.TimeExtract;
import com.fpt.ruby.nlp.AnswerMapper;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.service.MovieFlyService;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.fpt.ruby.service.mongo.QuestionStructureService;

import fpt.qa.intent.detection.MovieIntentDetection;

public class ProcessHelper {

	public static RubyAnswer getAnswer(String question,QuestionStructure questionStructure,
										MovieFlyService movieFlyService, MovieTicketService movieTicketService)  {
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setAnswer("this is answer of ruby");
		//rubyAnswer.setAnswer(getAnswer(question, movieFlyService, movieTicketService));
		//rubyAnswer.setAnswer(getSimsimiResponse(question));
		rubyAnswer.setQuestionStructure(questionStructure);
		return rubyAnswer;
	}
	
	public static RubyAnswer getAnswer(String question, MovieFlyService movieFlyService, MovieTicketService movieTicketService) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		String intent = MovieIntentDetection.getIntent(question);
		System.out.println("Intent: " + intent);
		rubyAnswer.setQuestion(question);
		rubyAnswer.setIntent(intent);
		String questionType = AnswerMapper.getTypeOfAnswer(intent, question);
		rubyAnswer.setAnswer("Xin lỗi, tôi không trả lời câu hỏi này được");
		System.out.println("Question Type: " + questionType);
		// static question
		try{
			if (questionType.equals(AnswerMapper.Static_Question)){
				String movieTitle = NlpHelper.getMovieTitle(question);
				System.out.println("Movie Title: " + movieTitle);
				List<MovieFly> movieFlies = movieFlyService.findByTitle(movieTitle);
				System.out.println("Size: " + movieFlies.size());
				if (movieFlies.size() == 0){
					movieFlies = new ArrayList<MovieFly>();
					MovieFly movieFly = movieFlyService.searchOnImdbByTitle(movieTitle);
					if (movieFly != null){					
						movieFlyService.save(movieFly);
						movieFlies.add(movieFly);
					}
				}
				rubyAnswer.setAnswer(AnswerMapper.getStaticAnswer(intent, movieFlies));
				rubyAnswer.setQuestionType(AnswerMapper.Static_Question);
				
				
				rubyAnswer.setMovieTitle(movieTitle);
			}
			else if (questionType.equals(AnswerMapper.Dynamic_Question)){
				MovieTicket matchMovieTicket = NlpHelper.getMovieTicket(question);
				Date beforeDate = new Date();
				beforeDate = null;
				Date afterDate = new Date();
				afterDate = null;
				TimeExtract timeExtract = NlpHelper.getTimeCondition(question);
				List<MovieTicket> movieTickets = movieTicketService.findMoviesMatchCondition
												(matchMovieTicket, timeExtract.getBeforeDate(), timeExtract.getAfterDate());
				System.out.println("Length: " + movieTickets.size());
				rubyAnswer.setAnswer(AnswerMapper.getDynamicAnswer(intent, movieTickets));
				rubyAnswer.setQuestionType(AnswerMapper.Dynamic_Question);
				rubyAnswer.setMovieTicket(matchMovieTicket);
			} 
			else {
				MovieTicket matchMovieTicket = new MovieTicket();
				matchMovieTicket.setCinema("Lotte Cinema Landmark");
				Date today = new Date();
				// list movie tickets for the duration of one day
				List<MovieTicket> movieTickets = movieTicketService.findMoviesMatchCondition(matchMovieTicket, new Date(today.getTime() + 86400000), today);
				rubyAnswer.setAnswer(AnswerMapper.getFeaturedAnswer(question, movieTickets, movieFlyService));
				rubyAnswer.setQuestionType(AnswerMapper.Featured_Question);
				rubyAnswer.setMovieTicket(matchMovieTicket);
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return rubyAnswer;
	}
	
	public static String getSimsimiResponse(String question){
		System.out.println("Simsimi get answer ....");
		System.out.println("question: " + question);
		try{
			String url = "http://sandbox.api.simsimi.com/request.p?key=9cac0c6c-1810-447b-ad5c-c1c76b2aadeb&lc=vn&text=" 
					+  URLEncoder.encode(question,"UTF-8");
			String jsonString = HttpHelper.sendGet(url);
			System.out.println("response: " + jsonString);
			JSONObject json = new JSONObject(jsonString);
			String answer = json.getString("response");
			return  answer;
		}
		catch (Exception ex){
			return "Em mệt rồi, không chơi nữa, đi ngủ đây!";
		}
		
		
	}
	
	public static RubyAnswer getAnswerFromSimsimi(String question, QuestionStructure questionStructure) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setQuestionStructure(questionStructure);
		return rubyAnswer;
	}
	
	public static QuestionStructure getQuestionStucture(String question,
			QuestionStructureService questionStructureService) {
		QuestionStructure questionStructure = new QuestionStructure();
		String key = NlpHelper.normalizeQuestion(question);
		// If in cache
		if (questionStructureService.isInCache(key)) {
			questionStructure = questionStructureService.getInCache(key);
			return questionStructure;
		}
		// If not in cache
		questionStructure = NlpHelper.processQuestionStructure(question);
		// Cache new question
		questionStructureService.cached(questionStructure);
		// Save to mongodb
		questionStructureService.save(questionStructure);
		return questionStructure;
	}
}