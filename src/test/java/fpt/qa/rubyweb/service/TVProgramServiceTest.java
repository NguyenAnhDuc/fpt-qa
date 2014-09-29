package fpt.qa.rubyweb.service;

import java.util.Date;
import java.util.List;

import com.fpt.ruby.model.TVProgram;
import com.fpt.ruby.service.TVProgramService;

public class TVProgramServiceTest{

	public static final String channel = "VTV6";
	public static final String title = "đùa chút";
	public static final Date now = new Date();
	public static final long one_hour = 60 * 60 * 1000;
	
	private static final long ONE_DAY = 24 * one_hour;
	private static final long ONE_WEEK = 7 * ONE_DAY;
	
	public static final Date after_one_hour = new Date(now.getTime() + 60 * 60 * 1000);
	
	
	public static final TVProgramService tvService = new TVProgramService();
	private String printTVProg(TVProgram tv){
		return tv.getChannel() + "\t" + tv.getStart_date() + "\t" + tv.getEnd_date() + "\t" + tv.getTitle();
	}
	
	public void findByTitle(){
		System.out.println("Testing findByTitle func");
		List< TVProgram > res = tvService.findByTitle( title );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findByChannel(){
		System.out.println("Testing findByChannel func");
		List< TVProgram > res = tvService.findByChannel( channel );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findByTitleAndChannel(){
		System.out.println("Testing findByTitleAndChannel func");
		List< TVProgram > res = tvService.findByTitleAndChannel( title, channel );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findInPeriod(){
		System.out.println("Testing findInPeriod func");
		List< TVProgram > res = tvService.findInPeriod( now, after_one_hour );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findByTitleInPeriod(){
		System.out.println("Testing findByTitleInPeriod func");
		List< TVProgram > res = tvService.findByTitleInPeriod( "Khám phá", now, after_one_hour);
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findInPeriodAtChannel(){
		System.out.println("Testing findInPeriodAtChannel func");
		List< TVProgram > res = tvService.findInPeriodAtChannel( now, after_one_hour, "vtv2");
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findByTitleInPeriodAtChannel(){
		System.out.println("Testing findByTitleInPeriodAtChannel func");
		List< TVProgram > res = tvService.findByTitleInPeriodAtChannel("thế giới", now, after_one_hour, "vtv2");
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findAtTime(){
		System.out.println("Testing findAtTime func");
		List< TVProgram > res = tvService.findAtTime( now );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findByTitleAtTime(){
		System.out.println("Testing findByTitleAtTime func");
		List< TVProgram > res = tvService.findByTitleAtTime( "trực tiếp", now );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findAtTimeAtChannel(){
		System.out.println("Testing findAtTimeAtChannel func");
		List< TVProgram > res = tvService.findAtTimeAtChannel( now, channel );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findByTitleAtTimeAtChannel(){
		System.out.println("Testing findByTitleAtTimeAtChannel func");
		List< TVProgram > res = tvService.findByTitleAtTimeAtChannel( "trực tiếp", now, "vtv1" );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	
	public void findAfter(){
		System.out.println("Testing findAfter func");
		List< TVProgram > res = tvService.findAfter( now );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findAfterByTitle(){
		System.out.println("Testing findAfterByTitle func");
		List< TVProgram > res = tvService.findAfterByTitle( title, now );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findAfterAtChannel(){
		System.out.println("Testing findAfterAtChannel func");
		List< TVProgram > res = tvService.findAfterAtChannel( now, channel );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public void findAfterByTitleAtChannel(){
		System.out.println("Testing findAfterByTitleAtChannel func");
		List< TVProgram > res = tvService.findAfterByTitleAtChannel( title, now, channel );
		System.out.println(res.size());
		
		if (!res.isEmpty()){
			for (TVProgram tv : res){
				System.out.println(printTVProg( tv ));
			}
		}
		System.out.println();
		System.out.println();
	}
	
	
	public static void main( String[] args ) {
		// TODO Auto-generated method stub
		TVProgramServiceTest test = new TVProgramServiceTest();
//		test.findByTitle();
//		test.findAfter();
//		test.findAfterAtChannel();
//		test.findAfterByTitle();
//		test.findAfterByTitleAtChannel();
//		
//		test.findAtTime();
//		test.findAtTimeAtChannel();
//		test.findByTitleAtTime();
//		test.findByTitleAtTimeAtChannel();
//		
//		test.findByChannel();
//		test.findByTitleAndChannel();
//
//		test.findInPeriod();
//		test.findByTitleInPeriod();
//		test.findByTitleInPeriodAtChannel();
//		test.findInPeriodAtChannel();
	}

}
