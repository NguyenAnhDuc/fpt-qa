package com.fpt.ruby.nlp;

import java.util.List;

import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.model.MovieTicket;

import fpt.qa.intent.detection.IntentConstants;

public class AnswerMapper {
	private static MovieAnswerMapper mam = new MovieAnswerMapperImpl();
	private static TicketAnswerMapper tam = new TicketAnswerMapperImpl();
	public static String Static_Question = "static";
	public static String Dynamic_Question = "dynamic";
	
	// detect question is static or dynamic
	public static String getTypeOfAnswer(String intent){
		if (intent.equals(IntentConstants.MOV_DATE)) return Dynamic_Question; 
		if (intent.equals(IntentConstants.CIN_NAME)) return Dynamic_Question; 
		if (intent.equals(IntentConstants.MOV_TITLE)) return Dynamic_Question; 
		if (intent.equals(IntentConstants.MOV_TYPE)) return Dynamic_Question; 
		return Static_Question;
	}
	
	public static String getStaticAnswer(String intent, List<MovieFly> ans){
		if(intent.equals(IntentConstants.MOV_ACTOR)){
			return mam.getActorMovieAnswer(ans);
		}
		
		if(intent.equals(IntentConstants.MOV_AUDIENCE)){
			return mam.getAudienceMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_AWARD)){
			return mam.getCountryMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_COUNTRY)){
			return mam.getCountryMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_DIRECTOR)){
			return mam.getDirectorMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_GENRE)){
			return mam.getGenreMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_IMDBRATING)){
			return mam.getImdbRatingMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_LANG)){
			return mam.getLangMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_PLOT)){
			return mam.getPlotMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_RELEASE)){
			return mam.getReleaseMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_RUNTIME)){
			return mam.getRuntimeMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_TITLE)){
			return mam.getTitleMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_YEAR)){
			return mam.getYearMovieAnswer(ans);
		}
		
		return "Xin lỗi, chúng tôi chưa có câu trả lời cho câu hỏi của bạn";
	}
	
	public static String getDynamicAnswer(String intent, List<MovieTicket> ans){
		if (intent.equals(IntentConstants.MOV_DATE)){
			return tam.getDateTicketAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.CIN_NAME)){
			return tam.getCinemaTicketAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_TYPE)){
			return tam.getTypeTicketAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_TITLE)){
			return tam.getTitleTicketAnswer(ans);
		}
		
		return "Xin lỗi, chúng tôi chưa có câu trả lời cho câu hỏi của bạn";
	}
}
