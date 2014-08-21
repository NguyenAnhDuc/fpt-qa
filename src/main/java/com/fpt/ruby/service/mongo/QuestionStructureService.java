package com.fpt.ruby.service.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import redis.clients.jedis.Jedis;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.QuestionStructure;

public class QuestionStructureService {
	private final String REDIS_HOST = "10.3.9.236";
	private MongoOperations mongoOperations;
	private Jedis jedis;
	public QuestionStructureService(){
		//ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
		jedis = new Jedis(REDIS_HOST);
	}
	public void save (QuestionStructure questionStructure){
		mongoOperations.save(questionStructure);
	}
	
	public List<QuestionStructure> allQuestionStructures(){
		return mongoOperations.findAll(QuestionStructure.class);
	}
	
	public void cached(QuestionStructure questionStructure) {
		String key  = questionStructure.getKey();
		if (questionStructure.getHead() != null && !questionStructure.getHead().isEmpty())
			jedis.set(RedisHelper.getHeadKey(key), questionStructure.getHead());
		for (String modifier : questionStructure.getModifiers()) {
			jedis.lpush(RedisHelper.getModifiersKey(key),modifier);
		}
	}
	
	public boolean isInCache(String key) {
		if (jedis.get(RedisHelper.getHeadKey(key)) == null) return false;
		return true;
	}
	
	public QuestionStructure getInCache(String key){
		QuestionStructure questionStructure = new QuestionStructure();
		questionStructure.setKey(key);
		String head = jedis.get(RedisHelper.getHeadKey(key));
		if (head != null) 
			questionStructure.setHead(head);
		List<String> modifiers = new ArrayList<String>();
		String modifier = jedis.lpop(RedisHelper.getModifiersKey(key));
		while (modifier != null){
			modifiers.add(modifier);
			modifier = jedis.lpop(RedisHelper.getModifiersKey(key));
		}
		questionStructure.setModifiers(modifiers);
		return questionStructure;
	}
	
	public void loadFromDBToCache(){
		List<QuestionStructure> questionStructures = mongoOperations.findAll(QuestionStructure.class);
		for (QuestionStructure questionStructure : questionStructures) {
			String key  = questionStructure.getKey();
			if (questionStructure.getHead() != null && !questionStructure.getHead().isEmpty())
				jedis.set(RedisHelper.getHeadKey(key), questionStructure.getHead());
			for (String modifier : questionStructure.getModifiers()) {
				jedis.lpush(RedisHelper.getModifiersKey(key),modifier);
			}
		}
	}
	
}
