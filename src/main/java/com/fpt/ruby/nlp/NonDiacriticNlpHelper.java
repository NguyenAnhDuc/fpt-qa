
package com.fpt.ruby.nlp;

import java.util.ArrayList;
import java.util.List;

import com.fpt.ruby.conjunction.ConjunctionHelper;
import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.model.TimeExtract;

import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.additionalinformation.modifier.AbsoluteTime.TimeResult;
import fpt.qa.intent.detection.IntentConstants;
import fpt.qa.intent.detection.MovieIntentDetection;
import fpt.qa.intent.detection.NonDiacriticMovieIntentDetection;
import fpt.qa.mdnlib.struct.pair.Pair;

public class NonDiacriticNlpHelper{
	private static ConjunctionHelper conjunctionHelper;
	private static AbsoluteTime absoluteTime;
	
	static{
		String dir = ( new RedisHelper() ).getClass().getClassLoader().getResource( "" ).getPath();
		MovieIntentDetection.init(dir + "/qc/movie", dir + "/dicts");
		NonDiacriticMovieIntentDetection.init( dir + "/qc/movie/non-diacritic", dir + "/dicts/non-diacritic" );
		conjunctionHelper = new ConjunctionHelper( dir + "/non-diacritic" );
		absoluteTime = new AbsoluteTime( NonDiacriticNlpHelper.class.getClassLoader().getResource( "" ).getPath()
				+ "vnsutime/" );
	}

	public static String getMovieTitle( String question ) {
		List< Pair< String, String >> conjunctions = conjunctionHelper.getConjunction( question );
		for( Pair< String, String > conjunction : conjunctions ){
			System.out.println( conjunction.first + " | " + conjunction.second );
			if( conjunction.second.equals( IntentConstants.MOV_TITLE ) )
				return conjunction.first.replace( "{", "" ).replace( "}", "" );
		}
		return null;
	}

	public static MovieTicket getMovieTicket( String question ) {
		List< Pair< String, String >> conjunctions = conjunctionHelper.getConjunction( question );
		MovieTicket movieTicket = new MovieTicket();
		for( Pair< String, String > conjunction : conjunctions ){
			System.out.println( conjunction.first + " | " + conjunction.second );
			if( conjunction.second.equals( IntentConstants.CIN_NAME ) )
				movieTicket.setCinema( conjunction.first.replace( "{", "" ).replace( "}", "" ) );
			if( conjunction.second.equals( IntentConstants.MOV_TITLE ) )
				movieTicket.setMovie( conjunction.first.replace( "{", "" ).replace( "}", "" ) );
		}
		return movieTicket;
	}

	public static QuestionStructure processQuestionStructure( String question ) {
		QuestionStructure questionStructure = new QuestionStructure();
		questionStructure.setKey( normalizeQuestion( question ) );
		questionStructure.setHead( question.isEmpty() ? "" : NonDiacriticMovieIntentDetection
				.getIntent( normalizeQuestion( question ) ) );
		questionStructure.setModifiers( new ArrayList< String >() );
		return questionStructure;
	}

	public static String normalizeQuestion( String question ) {
		if( question.isEmpty() ){
			return "";
		}
		int j = question.length() - 1;
		// remove question mark or special character
		for( ; j >= 0; j-- ){
			char c = question.charAt( j );
			if( Character.isLetter( c ) || Character.isDigit( c ) ){
				break;
			}
		}
		return question.toLowerCase().substring( 0, j ).replaceAll( "(\\d+)(h)", "$1 giờ" );
	}

	public static TimeExtract getTimeCondition( String text ) {
		try{
			TimeResult timeResult = absoluteTime.getAbsoluteTime( text );
			TimeExtract timeExtract = new TimeExtract();
			timeExtract.setBeforeDate( timeResult.getBeginTime() );
			timeExtract.setAfterDate( timeResult.getEndTime() );
			return timeExtract;
		}catch ( Exception ex ){
			System.out.println( "Time Exception!" );
			return new TimeExtract();
		}

	}

	public static void main( String[] args ) {
	}

}
