package com.fpt.ruby.nlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.TVProgram;
import com.fpt.ruby.service.TVProgramService;

import fpt.qa.intent.detection.TVIntentDetection;
import fpt.qa.mdnlib.util.string.DiacriticConverter;

public class TVAnswerMapperImpl implements TVAnswerMapper {
	public static final String DEF_ANS = "Xin lỗi, chúng tôi không có thông tin cho câu trả lời của bạn";
	private TVIntentDetection intentDetector = new TVIntentDetection();
	private TVIntentDetection nonDiacritic = new TVIntentDetection();
	private TVProgramService tps = new TVProgramService();
	
	public void init() {
//		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		String dir = "/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/src/main/resources";
		intentDetector.init( dir + "/qc/tv", dir + "/dicts");
		nonDiacritic.init( dir + "/qc/tv/non-diacritic", dir + "/dicts/non-diacritic");
	}
	
	public String getAnswer ( String question ) {
		String tmp = "\t" + question + "\n";
		
		String intent = intentDetector.getIntent( question );
		System.out.println("TV Intent: " + intent);
		tmp += "\t" + "TV Intent: " + intent + "\n";
		
		String intent2 = nonDiacritic.getIntent( question );
		System.out.println("Non-diacritic TV Intent: " + intent2);
		tmp += "\t" + "Non-diacritic TV Intent: " + intent2 + "\n";
		
		if (!DiacriticConverter.hasDiacriticAccents(question)){
			intent = intent2;
		}
		if (intent.equals( "UDF")){
			return tmp + DEF_ANS + "\n\n\n";
		}
		
		TVModifiers mod = TVModifiers.getModifiers( question.replaceAll("(\\d+)(h)", "$1 giờ ").replaceAll( "\\s+", " " ) );
		tmp += "\t" + question.replaceAll("(\\d+)(h)", "$1 giờ ").replaceAll( "\\s+", " " ) + "\n";
		tmp += "\t" + mod + "\n";
		
		List< TVProgram > progs = tps.getList( mod, question );

		if (mod.getChannel() == null && mod.getProg_title() == null){
			if (mod.getStart() == null){
				return tmp + DEF_ANS + "\n\n\n";
			}
			if (mod.getStart().equals( mod.getEnd() )){
				return tmp + getChannelAndProgram( progs ) + "\n\n\n";
			}
			return tmp + getChannelProgAndTime( progs ) + "\n\n\n";
		}
		
		if (mod.getChannel() == null){
			if (intent.equals( "DAT" ) || intent.equals( "POL" ) && progs.isEmpty()){
				return tmp + "Không đúng!" + "\n\n\n";
			}
			if (mod.getStart() == null){
				return tmp + DEF_ANS + "\n\n\n";
			}
			if (mod.getStart().equals( mod.getEnd() )){
				if (intent.equals( "CHN" )){
					return tmp + getChannel( progs ) + "\n\n\n";
				}
				return tmp + getChannelAndProgram( progs ) + "\n\n\n";
			}
			return tmp + getChannelProgAndTime( progs ) + "\n\n\n";
		}
		
		if (mod.getProg_title() == null){
			if (mod.getStart() == null){
				return tmp + DEF_ANS + "\n\n\n";
			}
			if (mod.getStart().equals( mod.getEnd() )){
				return tmp + getTitle( progs ) + "\n\n\n";
			}
			return tmp + getTitleAndTime( progs ) + "\n\n\n";
		}
		
		if (mod.getStart() == null){
			return tmp + DEF_ANS + "\n\n\n";
		}
		
		if (mod.getStart().equals( mod.getEnd() )){
			if (progs.isEmpty()){
				return tmp + "Không có " + mod.getProg_title() + " nào trên kênh " + mod.getChannel() + " vào lúc đó!\n\n\n";
			}
			return tmp + getTitle( progs ) + "\n\n\n";
		}
		
		return tmp + getTitleAndTime( progs ) + "\n\n\n";
		
//		return DEF_ANS;
	}

	public String  getTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String time = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			time += progs.get( i ).getStart_date() + "\n";
		}
		
		return time.substring( 0, time.length() - 2 );
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
			title += progs.get( i ).getTitle() + "\n";
		}
		
		return title.substring( 0, title.length() - 2 );
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
			channel += progs.get( i ).getTitle() + "\n";
		}
		
		return channel.substring( 0, channel.length() - 2 );
	}

	public String  getTitleAndTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String title = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			title += tv.getTitle() + " : " + tv.getStart_date().toString() + "\n";
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
			info += tv.getChannel() + " : " + tv.getTitle() + "\n";
		}
		
		return info.substring( 0, info.length() - 2 );
	}

	public String  getChannelAndTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String info = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			info += tv.getChannel() + " : " + tv.getStart_date() + "\n";
		}
		
		return info.substring( 0, info.length() - 2 );
	}

	public String  getChannelProgAndTime ( List< TVProgram > progs ) {
		if (progs.isEmpty())
			return DEF_ANS;
		
		String info = "";
		int limit = 5;
		if (progs.size() < 5) {
			limit = progs.size();
		}
		for (int i = 0; i < limit; i++){
			TVProgram tv = progs.get( i );
			info += tv.getChannel() + " : " + tv.getTitle() + " : " + tv.getStart_date() + "\n";
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