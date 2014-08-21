package com.fpt.ruby.nlp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;

import redis.clients.jedis.Jedis;

import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.service.mongo.QuestionStructureService;

public class NlpHelper {
	
	
	public static QuestionStructure processQuestionStructure(String question){
		QuestionStructure questionStructure = new QuestionStructure();
		questionStructure.setKey(normalizeQuestion(question));
		questionStructure.setHead("this is default head");
		questionStructure.setModifiers(new ArrayList<String>());
		return questionStructure;
	}
	
	public static String normalizeQuestion(String question){
		return question.toLowerCase();
	}
	
	public static void main(String[] args) {
		System.out.println("aaaaaaaaa".hashCode());
	}

	
	
}
