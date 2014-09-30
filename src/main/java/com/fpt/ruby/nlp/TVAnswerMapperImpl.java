package com.fpt.ruby.nlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.TVProgram;

import fpt.qa.intent.detection.TVIntentDetection;
import fpt.qa.mdnlib.util.string.DiacriticConverter;

public class TVAnswerMapperImpl implements TVAnswerMapper {
	public static final String DEF_ANS = "Xin lỗi, chúng tôi không có thông tin cho câu trả lời của bạn";
	private TVIntentDetection intentDetector = new TVIntentDetection();
	private TVIntentDetection nonDiacritic = new TVIntentDetection();
	
	public void init() {
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		intentDetector.init( dir + "/dicts", dir + "/qc/tv" );
		nonDiacritic.init( dir + "/dicts/non-diacritic", dir + "/qc/tv/non-diacritic" );
	}
	
	public String getAnswer ( String question ) {
		String tmp = "";
		
		String intent = intentDetector.getIntent( question );
		System.out.println("TV Intent: " + intent);
		tmp += "TV Intent: " + intent + "\n";
		
		String intent2 = nonDiacritic.getIntent( question );
		System.out.println("Non-diacritic TV Intent: " + intent2);
		tmp += "Non-diacritic TV Intent: " + intent2 + "\n";
		
		if (!DiacriticConverter.hasDiacriticAccents(question)){
			intent = intent2;
		}
		
		TVModifiers mod = TVModifiers.getModifiers( question );
		tmp += mod + "\n\n\n";
		
		return tmp;
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
			time += progs.get( i ).getStart_date() + ", ";
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
			title += progs.get( i ).getTitle() + ", ";
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
			channel += progs.get( i ).getTitle() + ", ";
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
			title += tv.getTitle() + " : " + tv.getStart_date().toString() + ", ";
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
			info += tv.getChannel() + " : " + tv.getTitle() + ", ";
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
			info += tv.getChannel() + " : " + tv.getStart_date() + ", ";
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
			info += tv.getChannel() + " : " + tv.getTitle() + " : " + tv.getStart_date() + ", ";
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
			
			writer.close();
			reader.close();
		}catch ( IOException e ){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args){
		
	}

}