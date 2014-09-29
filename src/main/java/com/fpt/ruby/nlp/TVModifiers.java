package com.fpt.ruby.nlp;

import java.util.Date;
import java.util.List;

import com.fpt.ruby.conjunction.ConjunctionHelper;
import com.fpt.ruby.helper.RedisHelper;

import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.additionalinformation.modifier.AbsoluteTime.TimeResult;
import fpt.qa.mdnlib.struct.pair.Pair;

public class TVModifiers{
	private static final String CHANNEL = "chanel_title";
	private static final String PROGRAM = "program_title";
	private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
	
	static ConjunctionHelper conjunctionHelper;
	static AbsoluteTime timeParser;
	
	// init the conjunction helper object
	static{
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		conjunctionHelper = new ConjunctionHelper( dir );
		timeParser = new AbsoluteTime(dir + "/vnsutime");
	}
	
	private String prog_title;
	private String channel;
	private Date start;
	private Date end;
	
	public String getProg_title() {
		return prog_title;
	}
	public void setProg_title( String prog_title ) {
		this.prog_title = prog_title;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel( String channel ) {
		this.channel = channel;
	}
	
	public Date getStart() {
		return start;
	}
	public void setStart( Date start ) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd( Date end ) {
		this.end = end;
	}
	public static TVModifiers getModifiers(String question){
		TVModifiers mod = new TVModifiers();
		List<Pair<String, String>> conjunctions = conjunctionHelper.getConjunction(question);
		for (Pair<String, String> conjunction : conjunctions ){
			if (mod.getChannel() == null && conjunction.second.equals( CHANNEL )){
				mod.setChannel( conjunction.first );
				continue;
			}
			
			if (mod.getProg_title() == null && conjunction.second.equals( PROGRAM )){
				mod.setProg_title( conjunction.first );
				continue;
			}
		}
		if (question.contains( "đang" ) && !question.contains( "đang làm gì" ) ||
				question.contains( "bây giờ" ) || question.contains( "hiện tại" )){
			mod.setStart( new Date() );
			mod.setEnd( new Date(mod.getStart().getTime() + ONE_WEEK) );
			return mod;
		}
		
		TimeResult time = timeParser.getAbsoluteTime( question );
		mod.setStart( time.getBeginTime() );
		mod.setEnd( time.getEndTime() );
		
		return mod;
	}
	
	public String toString(){
		return channel + " : " + prog_title + " : " + start + " : " + end;
	}
	
}
