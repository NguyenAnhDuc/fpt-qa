package com.fpt.ruby.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.model.TVProgram;

@Service
public class TVProgramService {
	private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
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
		Date today = new Date();
		Date oneWeekLater = new Date(today.getTime() + ONE_WEEK);
		
		return findByTitleInPeriod( title, today, oneWeekLater );
	}
	
	public List< TVProgram > findByChanel(String chanel){
		Date today = new Date();
		Date oneWeekLater = new Date(today.getTime() + ONE_WEEK);
		
		return findInPeriodAtChanel( today, oneWeekLater, chanel );
	}
	
	public List< TVProgram > findByTitleAndChannel(String title, String chanel){
		Date today = new Date();
		Date oneWeekLater = new Date(today.getTime() + ONE_WEEK);
		
		return findByTitleInPeriodAtChannel( title, today, oneWeekLater, chanel );
	}
	
	public List< TVProgram > findInPeriod(Date start, Date end){
		Query query = new Query(Criteria.where("start_date").gt( start ).and( "start_date" ).lt( end ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findByTitleInPeriod(String title, Date start, Date end){
		Query query = new Query(Criteria.where("title").regex("^" + title + "$","i").and( "start_date" ).
				gt( start ).and( "end_date" ).lt( end ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findInPeriodAtChanel(Date start, Date end, String chanel){
		Query query = new Query(Criteria.where("chanel").regex("^" + chanel + "$","i").and( "start_date" ).
				gt( start ).and( "end_date" ).lt( end ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findByTitleInPeriodAtChannel(String title, Date start, Date end, String chanel){
		Query query = new Query(Criteria.where("chanel").regex("^" + chanel + "$","i").
				and( "title" ).regex("^" + title + "$","i").and( "start_date" ).
				and( "start_date" ).gt( start ).and( "end_date" ).lt( end ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAtTime(Date startDate){
		Query query = new Query(Criteria.where("start_date").lt( startDate ).and( "end_date" ).gt( startDate ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findByTitleAtTime(String title, Date date){
		Query query = new Query(Criteria.where("title").regex("^" + title + "$","i").
				and("start_date").lt( date ).and( "end_date" ).gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAtTimeAtChannel(Date date, String chanel){
		Query query = new Query(Criteria.where("chanel").regex("^" + chanel + "$","i").
				and("start_date").lt( date ).and( "end_date" ).gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findByTitleAtTimeAtChannel(String title, Date date, String chanel){
		Query query = new Query(Criteria.where("chanel").regex("^" + chanel + "$","i").
				and( "title" ).regex("^" + title + "$","i").and( "start_date" ).
				and("start_date").lt( date ).and( "end_date" ).gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	
	public List< TVProgram > findAfter(Date date){
		Query query = new Query(Criteria.where("start_date").gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAfterByTitle(String title, Date date){
		Query query = new Query(Criteria.where("title").regex("^" + title + "$","i").
				and("start_date").gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAfterAtChannel(Date date, String chanel){
		Query query = new Query(Criteria.where("chanel").regex("^" + chanel + "$","i").
				and("start_date").gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAfterByTitleAtChannel(String title, Date date, String chanel){
		Query query = new Query(Criteria.where("chanel").regex("^" + chanel + "$","i").
				and( "title" ).regex("^" + title + "$","i").and( "start_date" ).
				and("start_date").gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	
	public List<TVProgram> findProgramToShow(){
		List<TVProgram> tvPrograms = mongoOperations.findAll(TVProgram.class);
		List<TVProgram> results = new ArrayList<TVProgram>();
		Date date = new Date();
		date.setHours(0);date.setMinutes(0);date.setSeconds(0);
		for (TVProgram tvProgram : tvPrograms){
			if (tvProgram.getStart_date() != null && 
				(tvProgram.getStart_date().getDate() == date.getDate() && tvProgram.getStart_date().getMonth() == date.getMonth() 
				 && tvProgram.getStart_date().getYear() == date.getYear()))
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
