package com.fpt.ruby.nlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.QueryParamater;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.model.TVProgram;
import com.fpt.ruby.model.TimeExtract;
import com.fpt.ruby.service.TVProgramService;

import fpt.qa.intent.detection.TVIntentDetection;
import fpt.qa.mdnlib.util.string.DiacriticConverter;

public class TVAnswerMapperImpl implements TVAnswerMapper {
	public static final String DEF_ANS = "Xin lỗi, chúng tôi không có thông tin cho câu trả lời của bạn";
	private TVIntentDetection intentDetector = new TVIntentDetection();
	private TVIntentDetection nonDiacritic = new TVIntentDetection();
	private TVProgramService tps = new TVProgramService();
	
	public void init() {
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
//		String dir = "/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/src/main/resources";
		intentDetector.init( dir + "/qc/tv", dir + "/dicts");
		nonDiacritic.init( dir + "/qc/tv/non-diacritic", dir + "/dicts/non-diacritic");
	}
	
	public RubyAnswer getAnswer ( String question ) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		String tmp = "\t" + question + "\n";
		
		String intent = intentDetector.getIntent( NlpHelper.normalizeQuestion(question) );
		System.out.println("TV Intent: " + intent);
		tmp += "\t" + "TV Intent: " + intent + "\n";
		
		String intent2 = nonDiacritic.getIntent( NlpHelper.normalizeQuestion(question) );
		System.out.println("Non-diacritic TV Intent: " + intent2);
		tmp += "\t" + "Non-diacritic TV Intent: " + intent2 + "\n";
		
		if (!DiacriticConverter.hasDiacriticAccents(question)){
			intent = intent2;
		}
		
		rubyAnswer.setQuestion(question);
		rubyAnswer.setIntent(intent);
		rubyAnswer.setAnswer(tmp + DEF_ANS + "\n\n\n");
		
		if (intent.equals( "UDF")){
			return rubyAnswer;
		}
		
		TVModifiers mod = TVModifiers.getModifiers( question.replaceAll("(\\d+)(h)", "$1 giờ ").replaceAll( "\\s+", " " ) );
		tmp += "\t" + question.replaceAll("(\\d+)(h)", "$1 giờ ").replaceAll( "\\s+", " " ) + "\n";
		tmp += "\t" + mod + "\n";
		
		rubyAnswer.setQuestionType(mod.getChannel());
		rubyAnswer.setMovieTitle(mod.getProg_title() + "\n" + mod.getStart() + "\n" + mod.getEnd());
		System.out.println("TV channel: " + mod.getChannel());
		System.out.println("TV prog title: " + mod.getProg_title());
		
		// get Time condition
		TimeExtract timeExtract = NlpHelper.getTimeCondition( question );
		Date start = timeExtract.getBeforeDate();
		Date end = timeExtract.getAfterDate();
		
		if (question.contains( "đang" ) && !question.contains( "đang làm gì" ) ||
				question.contains( "bây giờ" ) || question.contains( "hiện tại" )){
			start = new Date();
			end = start;
		}
		mod.setStart(start);
		mod.setEnd(end);
		QueryParamater queryParamater = new QueryParamater();
		if (mod.getChannel() != null) queryParamater.setTvChannel("TV channel: " + mod.getChannel());
		if (mod.getProg_title() != null) queryParamater.setTvProTitle("TV Program Title: " + mod.getProg_title());
		rubyAnswer.setQueryParamater(queryParamater);
		System.out.println("TV query start time: " + mod.getStart());
		System.out.println("TV query end time: " + mod.getEnd());
		rubyAnswer.setBeginTime(mod.getStart());
		rubyAnswer.setEndTime(mod.getEnd());
		// end time processing
		
		List< TVProgram > progs = tps.getList( mod, question );

		if (mod.getChannel() == null && mod.getProg_title() == null){
			System.err.println("[TVAnserMapper]: Channel null and program null");
			if (mod.getStart() == null){
				rubyAnswer.setAnswer( DEF_ANS + "\n\n\n" );
				return rubyAnswer;
			}
			
			if (mod.getStart().equals( mod.getEnd() )){
				rubyAnswer.setAnswer( getChannelAndProgram( progs ) + "\n\n\n" );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getChannelProgAndTime( progs ) + "\n\n\n" );
			return rubyAnswer;
		}
		
		if (mod.getChannel() == null){
			System.err.println("[TVAnserMapper]: Channel null");
			if ( intent.equals( "POL" ) && progs.isEmpty()){
				rubyAnswer.setAnswer( "Không đúng!" + "\n\n\n" );
				return rubyAnswer;
			}
			if (intent.equals( "DAT" )){
				rubyAnswer.setAnswer( getTime( progs ) + "\n\n\n" );
				return rubyAnswer;
			}
			if (intent.equals( "CHN" )){
				rubyAnswer.setAnswer( getChannel( progs ) + "\n\n\n" );
				return rubyAnswer;
			}
			if (mod.getStart() == null){
				rubyAnswer.setAnswer( DEF_ANS + "\n\n\n" );
				return rubyAnswer;
			}
			if (mod.getStart().equals( mod.getEnd() )){
				if (intent.equals( "CHN" )){
					rubyAnswer.setAnswer( getChannel( progs ) + "\n\n\n" );
					return rubyAnswer;
				}
				rubyAnswer.setAnswer( getChannelAndProgram( progs ) + "\n\n\n" );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getChannelProgAndTime( progs ) + "\n\n\n" );
			return rubyAnswer;
		}
		
		if (mod.getProg_title() == null){
			System.err.println("[TVAnserMapper]: Program null");
			if (mod.getStart() == null){
				rubyAnswer.setAnswer( DEF_ANS + "\n\n\n" );
				return rubyAnswer;
			}
			if (mod.getStart().equals( mod.getEnd() )){
				rubyAnswer.setAnswer( getTitle( progs ) + "\n\n\n" );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getTitleAndTime( progs ) + "\n\n\n" );
			return rubyAnswer;
		}
		if (intent.equals( "DAT" )){
			if (progs.size() > 0){
				rubyAnswer.setAnswer( getTime( progs ) + "\n\n\n" );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer(mod.getChannel() + " không chiếu " + mod.getProg_title());
			return rubyAnswer;
		}
		
		if (mod.getStart() == null){
			rubyAnswer.setAnswer( DEF_ANS + "\n\n\n" );
			return rubyAnswer;
		}
		
		if (mod.getStart().equals( mod.getEnd() )){
			if (progs.isEmpty()){
				rubyAnswer.setAnswer( "Không có " + mod.getProg_title() + " nào trên kênh " + mod.getChannel() + " vào lúc đó!\n\n\n" );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getTitle( progs ) + "\n\n\n" );
			return rubyAnswer;
		}
		
		rubyAnswer.setAnswer( getTitleAndTime( progs ) + "\n\n\n" );
		return rubyAnswer;
		
//		return DEF_ANS;
	}

	public String  getTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
		String time = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			time += sdf.format(progs.get( i ).getStart_date()) + "</br>";
		}
		
		return time.substring( 0, time.length() - 1 );
	}

	public String  getTitle ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String title = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			title += progs.get( i ).getTitle() + "</br>";
		}
		
		return title.substring( 0, title.length() - 1 );
	}

	public String  getChannel ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String channel = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			channel += progs.get( i ).getChannel() + "</br>";
		}
		
		return channel.substring( 0, channel.length() - 2 );
	}

	public String  getTitleAndTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
		String title = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			title += tv.getTitle() + " : " + sdf.format(tv.getStart_date()) + "</br>";
		}
		
		return title.substring( 0, title.length() - 2 );
	}

	public String  getChannelAndProgram ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String info = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			info += tv.getChannel() + " : " + tv.getTitle() + "</br>";
		}
		
		return info.substring( 0, info.length() - 2 );
	}

	public String  getChannelAndTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
		String info = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			info += tv.getChannel() + " : " + sdf.format(tv.getStart_date()) + "</br>";
		}
		
		return info.substring( 0, info.length() - 2 );
	}

	public String  getChannelProgAndTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
		String info = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			info += tv.getChannel() + " : " + tv.getTitle() + " : " + sdf.format(tv.getStart_date()) + "</br>";
		}
		
		return info.substring( 0, info.length() - 2 );
	}

	public String  getEndDate ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		return progs.get( 0 ).getEnd_date().toString();
	}
	
	
	public void studyFile(String fileIn, String fileOut){
		try{
			BufferedReader reader = new BufferedReader( new FileReader( fileIn ) );
			BufferedWriter writer = new BufferedWriter( new FileWriter( fileOut ) );
			
			String line;
			while ((line = reader.readLine()) != null){
				writer.write( getAnswer( line  ) + "\n");
			}
			
			writer.close();
			reader.close();
		}catch ( IOException e ){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args){
		TVAnswerMapperImpl tam = new TVAnswerMapperImpl();
		tam.init();
		tam.studyFile( "/home/ngan/Work/AHongPhuong/Intent_detection/data/tv/AIML_tvd_questions.txt",
				"/home/ngan/Work/AHongPhuong/Intent_detection/data/tv/AIML_tvd_questions.out" );
	}

}