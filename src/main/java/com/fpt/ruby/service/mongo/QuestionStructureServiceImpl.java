/*package com.fpt.ruby.service.mongo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.fpt.ruby.model.QuestionStructure;

public class QuestionStructureServiceImpl implements QuestionStructureService {
	ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
	MongoOperations mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
	@Override
	public void save(QuestionStructure questionStructure) {
		mongoOperations.save(questionStructure);
	}
	
}
*/