package com.fpt.ruby.nlp;

import java.util.ArrayList;

import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.QuestionStructure;

import fpt.qa.intent.detection.MovieIntentDetection;

public class NlpHelper {
	static{
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		MovieIntentDetection.init(dir + "/qc", dir + "/dicts");
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
