package com.fpt.ruby;

import java.util.List;

import com.fpt.ruby.helper.ProcessHelper;
import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.service.mongo.QuestionStructureService;

public class App {
	QuestionStructureService questionStructureService;
	public App(QuestionStructureService questionStructureService){
		this.questionStructureService = questionStructureService;
	}
	
	public App(){
		this.questionStructureService = new QuestionStructureService();
	}
	
	public RubyAnswer getAnswer(String question){
		String key = NlpHelper.normalizeQuestion(question);
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setInCache(this.questionStructureService.isInCache(key));
		rubyAnswer.setQuestion(question);
		// Process question
		QuestionStructure questionStructure = ProcessHelper.getQuestionStucture(question, questionStructureService );
		//QuestionStructure questionStructure = new QuestionStructure();
		// Process answer
		RubyAnswer answer =  ProcessHelper.getAnswer(question,questionStructure);
		rubyAnswer.setAnswer(answer.getAnswer());
		rubyAnswer.setQuestionStructure(answer.getQuestionStructure());
		return rubyAnswer;
	}
	
	public static void main(String[] args) {
		App app = new App();
		//System.out.println(app.getAnswer("tối nay có phim gì nhỉ"));
		System.out.println(app.questionStructureService.isInCache("tối nay rạp chiếu phim gì")); 
		//System.out.println(getAnswer("what"));
		List<QuestionStructure> questionStructures = app.questionStructureService.allQuestionStructures();
		for (QuestionStructure questionStructure : questionStructures){
			System.out.println("Key: " + questionStructure.getKey());
		}
		//questionStructure.setKey("test");
		//app.questionStructureService.cached(questionStructure);*/
		//app.questionStructureService.save(questionStructure);
		System.out.println("DONE");
	}

}
