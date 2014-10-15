package com.fpt.ruby.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.model.TVProgram;
import com.fpt.ruby.nlp.TVModifiers;

@Service
public class TVProgramService {
	private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
	private static long ONE_DAY = 24 * 60 * 60 * 1000;
	private MongoOperations mongoOperations;
	public TVProgramService(MongoOperations mongoOperations){
		this.mongoOperations = mongoOperations;
	}
	
	public TVProgramService(){
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
	}
	
	public void cleanOldData(){
		Date now = new Date(new Date().getTime() - ONE_WEEK);
		Query query = new Query(Criteria.where("start_date" ).lt( now ));
		List<TVProgram> tvPrograms = mongoOperations.find(query, TVProgram.class);
		for (TVProgram tvProgram : tvPrograms){
			System.out.println("TV Program: " + tvProgram.getChannel() + " | " + tvProgram.getTitle() +  " | " + tvProgram.getStart_date());
			mongoOperations.remove(tvProgram);
		}
	}
	
	public List< TVProgram > getList(TVModifiers mod, String question){
		String channel = mod.getChannel();
		String title = mod.getProg_title();
		Date start = mod.getStart();
		Date end = mod.getEnd();
		
		if (mod.getStart() == null && mod.getEnd() == null){
			if (question.contains( "hết" ) || question.contains( "kết thúc" ) || 
				(question.contains( "đến" ) && (question.contains( "mấy giờ" ) ||
						question.contains( "khi nào" ) || question.contains( "lúc nào" ) ||
						question.contains( "bao giờ" )))){
				return new ArrayList< TVProgram >();
			}
		}
		
		if (channel != null && title != null && start != null && end != null){
			if (start.equals( end )){
				return findByTitleAtTimeAtChannel(title, start, channel );
			} else {
				return findByTitleInPeriodAtChannel( title, start, end, channel );
			}
		}
		
		if (channel == null){
			if (title == null){
				if (start == null){
					System.out.println(question);
					return new ArrayList< TVProgram >();
				}
				if (start.equals( end )){
					return findAtTime( start );
				}
				return findInPeriod( start, end );
			}
			if (start == null && end == null){
				return findByTitle( title );
			}
			if (start.equals( end )){
				return findByTitleAtTime( title, start );
			}
			return findByTitleInPeriod( title, start, end );
		}
		if (title == null){
			if (start == null && end == null){
				return findByChannel( channel );
			}
			if (start.equals( end )){
				return findAtTimeAtChannel( start, channel );
			}
			return findInPeriodAtChannel( start, end, channel );
		}
		if (start == null && end == null){
			return findByTitleAndChannel( title, channel );
		}
		
		return new ArrayList< TVProgram >();
	}
	
	public List<TVProgram> findAll(){
		return mongoOperations.findAll(TVProgram.class);
	}
	
	public List<TVProgram> findByTitle(String title){
		System.out.println("FIND BY TITLE");
		Date today = new Date();
		today.setHours(0);today.setMinutes(0);today.setSeconds(0);
		Date oneWeekBefore = new Date(today.getTime() - ONE_WEEK);
		Date oneWeekLater = new Date(today.getTime() + ONE_WEEK);
		
		return findByTitleInPeriod( title, oneWeekBefore, oneWeekLater );
	}
	
	public List< TVProgram > findByChannel(String channel){
		Date today = new Date();
		today.setHours(0);today.setMinutes(0);today.setSeconds(0);
		Date oneWeekBefore = new Date(today.getTime() - ONE_WEEK);
		Date oneWeekLater = new Date(today.getTime() + ONE_WEEK);
		
		return findInPeriodAtChannel(oneWeekBefore,oneWeekLater, channel );
	}
	
	public List< TVProgram > findByTitleAndChannel(String title, String channel){
		Date today = new Date();
		today.setHours(0);today.setMinutes(0);today.setSeconds(0);
		Date oneWeekLater = new Date(today.getTime() + ONE_WEEK);
		Date oneWeekBefore = new Date(today.getTime() - ONE_WEEK);
		
		return findByTitleInPeriodAtChannel( title, oneWeekBefore, oneWeekLater, channel );
	}
	
	public List< TVProgram > findInPeriod(Date start, Date end){
		Query query = new Query(Criteria.where("start_date").gt( start ).lt( end ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findByTitleInPeriod(String title, Date start, Date end){
		System.out.println("Find By Title In Period: " + start.toGMTString() + " | " + end.toGMTString());
		Query query = new Query(Criteria.where("title").regex("^.*" + title + ".*","i")
				.and( "start_date" ).
				gt( start ).and( "end_date" ).lt( end ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findInPeriodAtChannel(Date start, Date end, String channel){
		Query query = new Query(Criteria.where("channel").regex("^" + channel + "$","i").and( "start_date" ).
				gt( start ).and( "end_date" ).lt( end ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findByTitleInPeriodAtChannel(String title, Date start, Date end, String channel){
		Query query = new Query(Criteria.where("channel").regex("^" + channel + "$","i").
				and( "title" ).regex("^.*" + title + ".*","i").
				and( "start_date" ).gt( start ).and( "end_date" ).lt( end ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAtTime(Date startDate){
		Query query = new Query(Criteria.where("start_date").lt( startDate ).and( "end_date" ).gt( startDate ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findByTitleAtTime(String title, Date date){
		Query query = new Query(Criteria.where("title").regex("^.*" + title + ".*","i").
				and("start_date").lt( date ).and( "end_date" ).gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAtTimeAtChannel(Date date, String channel){
		Query query = new Query(Criteria.where("channel").regex("^" + channel + "$","i").
				and("start_date").lt( date ).and( "end_date" ).gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findByTitleAtTimeAtChannel(String title, Date date, String channel){
		Query query = new Query(Criteria.where("channel").regex("^" + channel + "$","i").
				and( "title" ).regex("^.*" + title + ".*","i").
				and("start_date").lt( date ).and( "end_date" ).gt( date ));
		return mongoOperations.find(query, TVProgram.class);
	}
	
	
	public List< TVProgram > findAfter(Date date){
		Query query = new Query(Criteria.where("start_date").gt( date )).with( new Sort( Direction.ASC, "start_date" ) );
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAfterByTitle(String title, Date date){
		Query query = new Query(Criteria.where("title").regex("^.*" + title + ".*","i").
				and("start_date").gt( date )).with( new Sort( Direction.ASC, "start_date" ) );
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAfterAtChannel(Date date, String channel){
		Query query = new Query(Criteria.where("channel").regex("^" + channel + "$","i").
				and("start_date").gt( date )).with( new Sort( Direction.ASC, "start_date" ) );
		return mongoOperations.find(query, TVProgram.class);
	}
	
	public List< TVProgram > findAfterByTitleAtChannel(String title, Date date, String channel){
		Query query = new Query(Criteria.where("channel").regex("^" + channel + "$","i").
				and( "title" ).regex("^.*" + title + ".*","i").
				and("start_date").gt( date )).with( new Sort( Direction.ASC, "start_date" ) );
		return mongoOperations.find(query, TVProgram.class);
	}
	
	
	public List<TVProgram> findProgramToShow(int day){
		Date date = new Date(new Date().getTime() + day*ONE_DAY );
		date.setHours(0);date.setMinutes(0);date.setSeconds(0);
		Query query = new Query(Criteria.where("start_date").gt( date )).with( new Sort( Direction.ASC, "start_date" ) );
		List<TVProgram> results = mongoOperations.find(query, TVProgram.class);
		return results;
	}
	
	public void save(TVProgram tvProgram){
		mongoOperations.save(tvProgram);
	}
	
	public void dropCollection(){
		mongoOperations.dropCollection(TVProgram.class);
	}
	
	
	public static void main(String[] args) throws Exception {
		TVProgramService tvService = new TVProgramService();
		tvService.cleanOldData();
//		List< TVProgram > all = tvService.findAll();
//		
//		// Export db section
//		BufferedWriter writer = new BufferedWriter( new FileWriter( "/home/ngan/Desktop/tmp.txt" ) );
//		for (TVProgram tv : all){
//			writer.write( tv.getChannel() + "\t" + tv.getStart_date() + "\t" + tv.getEnd_date() + "\t" + tv.getTitle() + "\n" );
//		}
//		
//		writer.close();
//		// end export db section
		
	}

}
