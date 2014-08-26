package com.fpt.ruby;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.helper.ProcessHelper;
import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.fpt.ruby.service.mongo.QuestionStructureService;

public class App {
	MongoOperations mongoOperations;
	QuestionStructureService questionStructureService;
	MovieTicketService movieTicketService;
	public App(QuestionStructureService questionStructureService, MovieTicketService movieTicketService){
		this.questionStructureService = questionStructureService;
		this.movieTicketService = movieTicketService;
		
	}
	
	public App(){
		this.questionStructureService = new QuestionStructureService();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
		this.movieTicketService = new MovieTicketService(mongoOperations);
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
			//app.questionStructureService.cached(questionStructure);
			String key  = questionStructure.getKey();
			System.out.println("Key: " + key);
			questionStructure = app.questionStructureService.getInCache(key);
		}
		//questionStructure.setKey("test");
		//app.questionStructureService.cached(questionStructure);*/
		//app.questionStructureService.save(questionStructure);
		System.out.println("DONE");
	}

	public List<String> getListCachedQuestion() {
		List<QuestionStructure> allQuestion = this.questionStructureService.allQuestionStructures();
		List<String> listCachedQuestion = new ArrayList<String>();
		int count = 0;
		for (QuestionStructure questionStructure : allQuestion) {
			System.out.println(questionStructure.getKey());
			listCachedQuestion.add(questionStructure.getKey());
			count ++;
			if (count > 50) break;
		}
		return listCachedQuestion;
	}

}
