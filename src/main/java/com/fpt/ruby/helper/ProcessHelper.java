package com.fpt.ruby.helper;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.service.mongo.QuestionStructureService;

public class ProcessHelper {
	public static RubyAnswer getAnswer(String question,QuestionStructure questionStructure) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		//rubyAnswer.setAnswer("this is answer of ruby");
		
		rubyAnswer.setAnswer(getSimsimiResponse(question));
		rubyAnswer.setQuestionStructure(questionStructure);
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
