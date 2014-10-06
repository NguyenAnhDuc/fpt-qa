package com.fpt.ruby.conjunction;

import java.io.File;
import java.util.List;

import com.fpt.ruby.model.MovieTicket;

import fpt.qa.additionalinformation.modifier.ConjunctionWithType;
import fpt.qa.intent.detection.IntentConstants;
import fpt.qa.mdnlib.struct.pair.Pair;



public class ConjunctionHelper {
	private ConjunctionWithType conjunctionWithType;
	public ConjunctionHelper(){
		 conjunctionWithType = new ConjunctionWithType( "" );
	}
	public ConjunctionHelper(String dir){
		 conjunctionWithType = new ConjunctionWithType( dir );
	}
	
	public List<Pair<String, String>> getConjunction(String text){
		return conjunctionWithType.getOriginRelevantConjunctionsWithType(text); 
	}
	public String getMovieTitle(String question){
		List<Pair<String, String>> conjunctions = getConjunction(question);
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println(conjunction.first + " | " + conjunction.second);
			if (conjunction.second.equals(IntentConstants.MOV_TITLE))
				return conjunction.first.replace("{", "").replace("}", "");
		}
		return null;
	}
	
	public String getCinemaName(String question){
		List<Pair<String, String>> conjunctions = getConjunction(question);
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println("[Conjunction Helper - getCinemaName: ]" + conjunction.first + " | " + conjunction.second);
			if (conjunction.second.equals(IntentConstants.CIN_NAME))
				return conjunction.first.replace("{", "").replace("}", "");
		}
		return null;
	}
	
	public MovieTicket getMovieTicket(String question){
		System.err.println("Conjunction Helper: " + question);
		List<Pair<String, String>> conjunctions = getConjunction(question);
		MovieTicket movieTicket = new MovieTicket();
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println("[Conjunction Helper - getCinemaName: ]" + conjunction.first + " | " + conjunction.second);
			if (conjunction.second.equals(IntentConstants.CIN_NAME))
				movieTicket.setCinema(conjunction.first.replace("{", "").replace("}", ""));
			if (conjunction.second.equals(IntentConstants.MOV_TITLE))
				movieTicket.setMovie(conjunction.first.replace("{", "").replace("}", ""));
		}
		return movieTicket;
	}
}