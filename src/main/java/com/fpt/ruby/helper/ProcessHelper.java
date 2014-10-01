package com.fpt.ruby.helper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fpt.ruby.model.Log;
import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.QueryParamater;
import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.model.TimeExtract;
import com.fpt.ruby.nlp.AnswerMapper;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.nlp.NonDiacriticNlpHelper;
import com.fpt.ruby.service.LogService;
import com.fpt.ruby.service.MovieFlyService;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.fpt.ruby.service.mongo.QuestionStructureService;

import fpt.qa.intent.detection.MovieIntentDetection;
import fpt.qa.intent.detection.NonDiacriticMovieIntentDetection;
import fpt.qa.mdnlib.util.string.DiacriticConverter;

public class ProcessHelper{
	private static final Logger logger = LoggerFactory.getLogger(ProcessHelper.class);
	public static RubyAnswer getAnswer( String question, QuestionStructure questionStructure,
			MovieFlyService movieFlyService, MovieTicketService movieTicketService ) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setAnswer( "this is answer of ruby" );
		// rubyAnswer.setAnswer(getAnswer(question, movieFlyService,
		// movieTicketService));
		// rubyAnswer.setAnswer(getSimsimiResponse(question));
		rubyAnswer.setQuestionStructure( questionStructure );
		return rubyAnswer;
	}

	public static RubyAnswer getAnswer( String question, MovieFlyService movieFlyService,
			MovieTicketService movieTicketService, LogService logService ) {
		RubyAnswer withDiacritic = getAnswerWithDiacritic( question, movieFlyService, movieTicketService, logService );
		RubyAnswer removeDiacritic = getAnswerRemoveDiacritic( question, movieFlyService, movieTicketService, logService );		
		if( !removeDiacritic.isSuccessful() ){
			return removeDiacritic;
		}
		return withDiacritic;
	}

	private static RubyAnswer getAnswerRemoveDiacritic( String question, MovieFlyService movieFlyService,
			MovieTicketService movieTicketService, LogService logService ) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		logger.info("Get answer remove diacritic");
		System.out.println( DiacriticConverter.removeDiacritics( question ) );

		String intent = NonDiacriticMovieIntentDetection.getIntent( DiacriticConverter.removeDiacritics( question ) );
		logger.info( "Intent: " + intent );

		rubyAnswer.setQuestion( question );
		rubyAnswer.setIntent( intent );

		String questionType = AnswerMapper.getTypeOfAnswer( intent, question );
		rubyAnswer.setAnswer( "Xin lỗi, tôi không trả lời câu hỏi này được" );
		logger.info( "Question Type: " + questionType );
		// static question
		try{
			if( questionType.equals( AnswerMapper.Static_Question ) ){
				String movieTitle = NonDiacriticNlpHelper.getMovieTitle( question );
				System.out.println( "Movie Title: " + movieTitle );
				List< MovieFly > movieFlies = movieFlyService.findByTitle( movieTitle );
				if( movieFlies.size() == 0 ){
					movieFlies = new ArrayList< MovieFly >();
					MovieFly movieFly = movieFlyService.searchOnImdbByTitle( movieTitle );
					if( movieFly != null ){
						movieFlyService.save( movieFly );
						movieFlies.add( movieFly );
					}
				}

				rubyAnswer.setAnswer( AnswerMapper.getStaticAnswer( intent, movieFlies ) );
				rubyAnswer.setQuestionType( AnswerMapper.Static_Question );
				rubyAnswer.setMovieTitle( movieTitle );
			}else if( questionType.equals( AnswerMapper.Dynamic_Question ) ){
				System.out.println( "Dynamic ...." );
				MovieTicket matchMovieTicket = NonDiacriticNlpHelper.getMovieTicket( question );
				TimeExtract timeExtract = NonDiacriticNlpHelper.getTimeCondition( question );
				List< MovieTicket > movieTickets = movieTicketService.findMoviesMatchCondition( matchMovieTicket,
						timeExtract.getBeforeDate(), timeExtract.getAfterDate() );
				System.out.println( "Size: " + movieTickets.size() );
				if( timeExtract.getBeforeDate() != null )
					rubyAnswer.setBeginTime( timeExtract.getBeforeDate() );
				if( timeExtract.getAfterDate() != null )
					rubyAnswer.setEndTime( timeExtract.getAfterDate() );
				rubyAnswer.setAnswer( AnswerMapper.getDynamicAnswer( intent, movieTickets ) );
				rubyAnswer.setQuestionType( AnswerMapper.Dynamic_Question );
				rubyAnswer.setMovieTicket( matchMovieTicket );
				System.out.println( "DONE Process" );
			}else{
				System.out.println( "Feature .." );
				MovieTicket matchMovieTicket = new MovieTicket();
				matchMovieTicket.setCinema( "BHD Star Cineplex Icon 68" );
				Date today = new Date();
				System.out.println( "afterdate: " + today );

				// list movie tickets for the duration of one day
				List< MovieTicket > movieTickets = movieTicketService.findMoviesMatchCondition( matchMovieTicket,
						today, new Date( today.getTime() + 86400000 ) );
				System.out.println( "No of returned tickets: " + movieTickets.size() );
				rubyAnswer.setAnswer( AnswerMapper.getFeaturedAnswer( question, movieTickets, movieFlyService ) );
				rubyAnswer.setQuestionType( AnswerMapper.Featured_Question );
				rubyAnswer.setMovieTicket( matchMovieTicket );
			}
		}catch ( Exception ex ){
			System.out.println( "Exception! " + ex.getMessage() );
			ex.printStackTrace();
		}
		// Log
		Log log = new Log();
		log.setQuestion( question );
		log.setIntent( rubyAnswer.getIntent() );
		log.setAnswer( rubyAnswer.getAnswer() );
		log.setDate( new Date() );
		QueryParamater queryParamater = new QueryParamater();
		queryParamater.setBeginTime( rubyAnswer.getBeginTime() );
		queryParamater.setEndTime( rubyAnswer.getEndTime() );
		queryParamater.setMovieTitle( rubyAnswer.getMovieTitle() );
		queryParamater.setMovieTicket( rubyAnswer.getMovieTicket() );
		log.setQueryParamater( queryParamater );
		logService.save( log );
		
		if( rubyAnswer.getAnswer().contains( "Xin lỗi," ) ){
			rubyAnswer.setSuccessful( true );
		}
		
		return rubyAnswer;
	}

	private static RubyAnswer getAnswerWithDiacritic( String question, MovieFlyService movieFlyService,
			MovieTicketService movieTicketService, LogService logService ) {
		logger.info("Get answer with diacritic");
		RubyAnswer rubyAnswer = new RubyAnswer();
		String intent = MovieIntentDetection.getIntent( question );
		logger.info( "Movie Intent: " + intent );

		rubyAnswer.setQuestion( question );
		rubyAnswer.setIntent( intent );
		String questionType = AnswerMapper.getTypeOfAnswer( intent, question );
		rubyAnswer.setAnswer( "Xin lỗi, tôi không trả lời câu hỏi này được" );
		logger.info( "Question Type: " + questionType );
		// static question
		try{
			if( questionType.equals( AnswerMapper.Static_Question ) ){
				String movieTitle = NlpHelper.getMovieTitle( question );
				System.out.println( "Movie Title: " + movieTitle );
				List< MovieFly > movieFlies = movieFlyService.findByTitle( movieTitle );
				if( movieFlies.size() == 0 ){
					movieFlies = new ArrayList< MovieFly >();
					MovieFly movieFly = movieFlyService.searchOnImdbByTitle( movieTitle );
					if( movieFly != null ){
						movieFlyService.save( movieFly );
						movieFlies.add( movieFly );
					}
				}
				rubyAnswer.setAnswer( AnswerMapper.getStaticAnswer( intent, movieFlies ) );
				rubyAnswer.setQuestionType( AnswerMapper.Static_Question );

				rubyAnswer.setMovieTitle( movieTitle );
			}else if( questionType.equals( AnswerMapper.Dynamic_Question ) ){
				System.out.println( "Dynamic ...." );
				MovieTicket matchMovieTicket = NlpHelper.getMovieTicket( question );
				TimeExtract timeExtract = NlpHelper.getTimeCondition( question );
				List< MovieTicket > movieTickets = movieTicketService.findMoviesMatchCondition( matchMovieTicket,
						timeExtract.getBeforeDate(), timeExtract.getAfterDate() );
				System.out.println( "Size: " + movieTickets.size() );
				if( timeExtract.getBeforeDate() != null )
					rubyAnswer.setBeginTime( timeExtract.getBeforeDate() );
				if( timeExtract.getAfterDate() != null )
					rubyAnswer.setEndTime( timeExtract.getAfterDate() );
				rubyAnswer.setAnswer( AnswerMapper.getDynamicAnswer( intent, movieTickets ) );
				rubyAnswer.setQuestionType( AnswerMapper.Dynamic_Question );
				rubyAnswer.setMovieTicket( matchMovieTicket );
				System.out.println( "DONE Process" );
			}else{
				System.out.println( "Feature .." );
				MovieTicket matchMovieTicket = new MovieTicket();
				matchMovieTicket.setCinema( "BHD Star Cineplex Icon 68" );
				Date today = new Date();
				System.out.println( "afterdate: " + today );

				// list movie tickets for the duration of one day
				List< MovieTicket > movieTickets = movieTicketService.findMoviesMatchCondition( matchMovieTicket,
						today, new Date( today.getTime() + 86400000 ) );
				System.out.println( "No of returned tickets: " + movieTickets.size() );
				rubyAnswer.setAnswer( AnswerMapper.getFeaturedAnswer( question, movieTickets, movieFlyService ) );
				rubyAnswer.setQuestionType( AnswerMapper.Featured_Question );
				rubyAnswer.setMovieTicket( matchMovieTicket );
			}
		}catch ( Exception ex ){
			System.out.println( "Exception! " + ex.getMessage() );
			ex.printStackTrace();
		}
		// Log
		Log log = new Log();
		log.setQuestion( question );
		log.setIntent( rubyAnswer.getIntent() );
		log.setAnswer( rubyAnswer.getAnswer() );
		log.setDate( new Date() );
		QueryParamater queryParamater = new QueryParamater();
		queryParamater.setBeginTime( rubyAnswer.getBeginTime() );
		queryParamater.setEndTime( rubyAnswer.getEndTime() );
		queryParamater.setMovieTitle( rubyAnswer.getMovieTitle() );
		queryParamater.setMovieTicket( rubyAnswer.getMovieTicket() );
		log.setQueryParamater( queryParamater );
		logService.save( log );
		
		if( rubyAnswer.getAnswer().contains( "Xin lỗi," ) ){
			rubyAnswer.setSuccessful( true );
		}
		
		return rubyAnswer;
	}

	public static String getSimsimiResponse( String question ) {
		System.out.println( "Simsimi get answer ...." );
		System.out.println( "question: " + question );
		try{
			String url = "http://sandbox.api.simsimi.com/request.p?key=9cac0c6c-1810-447b-ad5c-c1c76b2aadeb&lc=vn&text="
					+ URLEncoder.encode( question, "UTF-8" );
			String jsonString = HttpHelper.sendGet( url );
			System.out.println( "response: " + jsonString );
			JSONObject json = new JSONObject( jsonString );
			String answer = json.getString( "response" );
			return answer;
		}catch ( Exception ex ){
			return "Em mệt rồi, không chơi nữa, đi ngủ đây!";
		}

	}

	public static RubyAnswer getAnswerFromSimsimi( String question, QuestionStructure questionStructure ) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setQuestionStructure( questionStructure );
		return rubyAnswer;
	}

	public static QuestionStructure getQuestionStucture( String question,
			QuestionStructureService questionStructureService ) {
		QuestionStructure questionStructure = new QuestionStructure();
		String key = NlpHelper.normalizeQuestion( question );
		// If in cache
		if( questionStructureService.isInCache( key ) ){
			questionStructure = questionStructureService.getInCache( key );
			return questionStructure;
		}
		// If not in cache
		questionStructure = NlpHelper.processQuestionStructure( question );
		// Cache new question
		questionStructureService.cached( questionStructure );
		// Save to mongodb
		questionStructureService.save( questionStructure );
		return questionStructure;
	}
}