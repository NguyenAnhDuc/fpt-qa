package com.fpt.ruby.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.model.NameMapper;

@Service
public class NameMapperService {

	private MongoOperations db;

	public NameMapperService(MongoOperations db) {
		this.db = db;
	}
	
	public NameMapperService() {
		ApplicationContext context = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		this.db = (MongoOperations) context.getBean("mongoTemplate");
	}

	public List<NameMapper> findAll() {
		return db.findAll(NameMapper.class);
	}

	public void save(List<NameMapper> nameMappers) {
		for (NameMapper name : nameMappers) {
			db.save(name);
		}
	}
	
	public void save(NameMapper inst) {
		db.save(inst);
	}
	
	public static void main(String[] args) {

	}
}
