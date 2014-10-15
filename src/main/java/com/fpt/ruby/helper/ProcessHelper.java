package com.fpt.ruby.helper;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fpt.ruby.conjunction.ConjunctionHelper;
import com.fpt.ruby.model.Cinema;
import com.fpt.ruby.model.Log;
import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.QueryParamater;
import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.model.TimeExtract;
import com.fpt.ruby.nlp.AnswerMapper;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.service.CinemaService;
import com.fpt.ruby.service.LogService;
import com.fpt.ruby.service.MovieFlyService;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.fpt.ruby.service.mongo.QuestionStructureService;

import fpt.qa.intent.detection.IntentConstants;
import fpt.qa.intent.detection.MovieIntentDetection;
import fpt.qa.intent.detection.NonDiacriticMovieIntentDetection;
import fpt.qa.mdnlib.util.string.DiacriticConverter;

public class ProcessHelper{
	private static ConjunctionHelper conjunctionHelperWithDiacritic, conjunctionHelperNoneDiacritic;
	private static final Logger logger = LoggerFactory.getLogger(ProcessHelper.class);
	static {
		String dir = ( new RedisHelper() ).getClass().getClassLoader().getResource( "" ).getPath();
	    conjunctionHelperWithDiacritic = new ConjunctionHelper(dir);
		conjunctionHelperNoneDiacritic = new ConjunctionHelper(dir + "/non-diacritic");
	}
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
			MovieTicketService movieTicketService, CinemaService cinemaService, LogService logService ) {
		if (DiacriticConverter.hasDiacriticAccents(question)){
			System.out.println("DIACRITIC");
			RubyAnswer rubyAnswerDiacritic = getAnswer(true, question, movieFlyService, movieTicketService, cinemaService, logService);
			return rubyAnswerDiacritic;
		}
		System.out.println("NONE DIACRITIC");
		RubyAnswer rubyAnswerNoneDiacritic = getAnswer(false, question, movieFlyService, movieTicketService, cinemaService, logService);
		return rubyAnswerNoneDiacritic;
		/*RubyAnswer rubyAnswerDiacritic = getAnswer(true, question, movieFlyService, movieTicketService, cinemaService, logService);
		return rubyAnswerDiacritic;*/
	}

	private static RubyAnswer getAnswer(boolean isDiacritic, String question, MovieFlyService movieFlyService,
			MovieTicketService movieTicketService, CinemaService cinemaService, LogService logService ) {
		// Conjunction
		ConjunctionHelper conjunctionHelper;
		if (isDiacritic) conjunctionHelper = conjunctionHelperWithDiacritic;
		else conjunctionHelper = conjunctionHelperNoneDiacritic;

		String intent = "";
		// Intent
		if (isDiacritic) intent = MovieIntentDetection.getIntent(question);
		else {
			question = DiacriticConverter.removeDiacritics(question);
			intent = NonDiacriticMovieIntentDetection.getIntent(question);
		}
		System.out.println("[ProcessHelper] Intent: " + intent);
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setQuestion( question );
		rubyAnswer.setIntent( intent );
		
		String questionType = AnswerMapper.getTypeOfAnswer( intent, question );
		rubyAnswer.setAnswer( "Xin lỗi, tôi không trả lời được câu hỏi này" );
		System.out.println( "[ProcessHelper] Question Type: " + questionType );
		// static question
		try{
			if(intent.equals(IntentConstants.CIN_DIS) || intent.equals(IntentConstants.CIN_MAP) || intent.equals(IntentConstants.CIN_SERVICETIME)){
				rubyAnswer.setSuccessful( true );
				return rubyAnswer;
			}
			
			if (intent.equals(IntentConstants.MOV_AWARD) || intent.equals(IntentConstants.MOV_WRITER) ||
					intent.equals(IntentConstants.TICKET_PRICE) || intent.equals(IntentConstants.TICKET_STATUS) ||
					intent.equals(IntentConstants.UNDEF)){
				rubyAnswer.setSuccessful( true );
				return rubyAnswer;
			}
			
			QueryParamater queryParamater = new QueryParamater();
			if( questionType.equals( AnswerMapper.Static_Question ) ){
				if (intent.equals(IntentConstants.CIN_ADD)){
					String cinName = conjunctionHelper.getCinemaName(question);
					System.out.println("[Process Helper] Cin name: " + cinName);
					List<Cinema> cinemas = cinemaService.findByName(cinName);
					queryParamater.setCinName(cinName);
					rubyAnswer.setAnswer( AnswerMapper.getCinemaStaticAnswer( intent, cinemas ) );
				}
				else{
					String movieTitle = conjunctionHelper.getMovieTitle( question );
					System.out.println( "Movie Title: " + movieTitle );
					List< MovieFly > movieFlies = movieFlyService.findByTitle( movieTitle );
					queryParamater.setMovieTitle(movieTitle);
					rubyAnswer.setAnswer( AnswerMapper.getStaticAnswer( intent, movieFlies ) );
				}
				rubyAnswer.setQueryParamater(queryParamater);
				rubyAnswer.setQuestionType( AnswerMapper.Static_Question );
				
			}else if( questionType.equals( AnswerMapper.Dynamic_Question ) ){
				System.out.println( "Dynamic ...." );
				MovieTicket matchMovieTicket = conjunctionHelper.getMovieTicket( question );
				TimeExtract timeExtract = NlpHelper.getTimeCondition( question );
				List< MovieTicket > movieTickets = movieTicketService.findMoviesMatchCondition( matchMovieTicket,
						timeExtract.getBeforeDate(), timeExtract.getAfterDate() );
				System.out.println( "Size: " + movieTickets.size() );
				if( timeExtract.getBeforeDate() != null )
					rubyAnswer.setBeginTime( timeExtract.getBeforeDate() );
				if( timeExtract.getAfterDate() != null )
					rubyAnswer.setEndTime( timeExtract.getAfterDate() );
				queryParamater.setMovieTitle(matchMovieTicket.getMovie());
				queryParamater.setCinName(matchMovieTicket.getCinema());
				rubyAnswer.setQueryParamater(queryParamater);
				rubyAnswer.setAnswer( AnswerMapper.getDynamicAnswer( intent, movieTickets, matchMovieTicket, timeExtract.getBeforeDate() != null ) );
				rubyAnswer.setQuestionType( AnswerMapper.Dynamic_Question );
				System.out.println( "DONE Process" );
			}else{
				System.out.println( "Feature .." );
				MovieTicket matchMovieTicket = conjunctionHelper.getMovieTicket( question );
				Date today = new Date();
				System.out.println( "afterdate: " + today );
				TimeExtract timeExtract = NlpHelper.getTimeCondition( question );
				if( timeExtract.getBeforeDate() != null )
					rubyAnswer.setBeginTime( timeExtract.getBeforeDate() );
				if( timeExtract.getAfterDate() != null )
					rubyAnswer.setEndTime( timeExtract.getAfterDate() );
				// list movie tickets for the duration of one day
				List< MovieTicket > movieTickets = movieTicketService.findMoviesMatchCondition( matchMovieTicket,
						timeExtract.getBeforeDate(), timeExtract.getAfterDate());
				System.out.println( "No of returned tickets: " + movieTickets.size() );
				queryParamater.setMovieTitle(matchMovieTicket.getMovie());
				queryParamater.setCinName(matchMovieTicket.getCinema());
				rubyAnswer.setQueryParamater(queryParamater);
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
		
		if( !rubyAnswer.getAnswer().contains( "Xin lỗi, tôi không trả lời được câu hỏi này" ) ){
			rubyAnswer.setSuccessful( true );
		}
		
		return rubyAnswer;
	}

	/*public static String getSimsimiResponse( String question ) {
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
			return "Em má»‡t rá»“i, khÃ´ng chÆ¡i ná»¯a, Ä‘i ngá»§ Ä‘Ã¢y!";
		}

	}*/

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