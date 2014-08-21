package fpt.qa.rubyweb;

import org.springframework.beans.factory.annotation.Autowired;

import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.service.mongo.QuestionStructureService;

public class TestService {
	//@Autowired 
	private static QuestionStructureService questionStructureService;
	
	public static void main(String[] args) {
		QuestionStructure questionStructure = new QuestionStructure();
		questionStructure.setHead("test");
		questionStructureService = new QuestionStructureService();
		questionStructureService.save(questionStructure);
		System.out.println("DONE");
	}
}
