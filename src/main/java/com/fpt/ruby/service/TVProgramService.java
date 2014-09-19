package com.fpt.ruby.service;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.TVProgram;

public class TVProgramService {
	private MongoOperations mongoOperations;
	public TVProgramService(MongoOperations mongoOperations){
		this.mongoOperations = mongoOperations;
	}
	
	public TVProgramService(){
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
	}
	
	public List<TVProgram> findAll(){
		return mongoOperations.findAll(TVProgram.class);
	}
	
	public List<TVProgram> findByTitle(String title){
		Query query = new Query(Criteria.where("title").regex("^" + title + "$","i"));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List<TVProgram> findProgramToShow(){
		List<TVProgram> tvPrograms = mongoOperations.findAll(TVProgram.class);
		List<TVProgram> results = new ArrayList<TVProgram>();
		Date date = new Date();
		date.setHours(0);date.setMinutes(0);date.setSeconds(0);
		for (TVProgram tvProgram : tvPrograms){
			if (tvProgram.getDate() != null && 
				(tvProgram.getDate().getDate() == date.getDate() && tvProgram.getDate().getMonth() == date.getMonth() 
				 && tvProgram.getDate().getYear() == date.getYear()))
				 results.add(tvProgram);
		}
		return results;
	}
	
	public void save(TVProgram tvProgram){
		mongoOperations.save(tvProgram);
	}
	
	public void dropCollection(){
		mongoOperations.dropCollection(TVProgram.class);
	}
	
	
	
	public static void main(String[] args) throws Exception {
		TVProgramService movieFlyService = new TVProgramService();
		
	}

}
