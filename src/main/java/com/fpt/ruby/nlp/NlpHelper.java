package com.fpt.ruby.nlp;

import java.util.ArrayList;
import java.util.List;

import mdnlib.struct.pair.Pair;

import com.fpt.ruby.conjunction.ConjunctionHelper;
import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.QuestionStructure;

import fpt.qa.intent.detection.IntentConstants;
import fpt.qa.intent.detection.MovieIntentDetection;

public class NlpHelper {
	private static ConjunctionHelper conjunctionHelper;
	static{
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		MovieIntentDetection.init(dir + "/qc", dir + "/dicts");
		conjunctionHelper = new ConjunctionHelper(dir+"/cj");
	}
	
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
	
	public static QuestionStructure processQuestionStructure(String question){
		QuestionStructure questionStructure = new QuestionStructure();
		questionStructure.setKey(normalizeQuestion(question));
		questionStructure.setHead(question.isEmpty() ? "" : MovieIntentDetection.getIntent(normalizeQuestion(question)));
		questionStructure.setModifiers(new ArrayList<String>());
		return questionStructure;
	}
	
	public static String normalizeQuestion(String question){
		if(question.isEmpty()){
			return "";
		}
		int j = question.length() - 1;
		// remove question mark or special character
		for (; j >= 0; j--){
			char c = question.charAt(j);
			if (Character.isLetter(c) || Character.isDigit(c)){
				break;
			}
		}
		return question.toLowerCase().substring(0,j);
	}
	
	public static void main(String[] args) {
		System.out.println("aaaaaaaaa".hashCode());
		MovieIntentDetection.init("/home/ngan/Work/AHongPhuong/Intent_detection/data/qc/2",
				"/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/data/dicts");
		System.out.println(MovieIntentDetection.getIntent(normalizeQuestion("tối nay có phim gì hay/")));
	}

	
	
}
