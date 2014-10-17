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

import fpt.qa.intent.detection.IntentConstants;
import fpt.qa.intent.detection.TVIntentDetection;
import fpt.qa.mdnlib.util.string.DiacriticConverter;

public class TVAnswerMapperImpl implements TVAnswerMapper {
	public static final String DEF_ANS = "Xin lỗi, chúng tôi không có thông tin cho câu trả lời của bạn";
	public static final String UDF_ANS = "Xin lỗi, chúng tôi không trả lời được câu hỏi của bạn";
	private TVIntentDetection intentDetector = new TVIntentDetection();
	private TVIntentDetection nonDiacritic = new TVIntentDetection();
	private TVProgramService tps = new TVProgramService();
	
	public void init() {
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
//		String dir = "D:/Workspace/Code/FTI/rubyweb/src/main/resources";
		
		intentDetector.init( dir + "/qc/tv", dir + "/dicts");
		nonDiacritic.init( dir + "/qc/tv/non-diacritic", dir + "/dicts/non-diacritic");
	}
	
	public RubyAnswer getAnswer ( String question ) {
		System.out.println("RUBY GET ANSWER");
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
		rubyAnswer.setAnswer(DEF_ANS );
		
		if (intent.equalsIgnoreCase(IntentConstants.TV_UDF)) 
			return rubyAnswer;
		
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
		
		if (question.contains( "dang" ) && !question.contains( "dang lam gi" ) ||
				question.contains( "bay gio" ) || question.contains( "hien tai" )
					|| question.contains( "sap" ) || question.contains( "tiep theo" )){
			start = new Date();
			end = start;
		}
		mod.setStart(start);
		mod.setEnd(end);
		
		QueryParamater queryParamater = new QueryParamater();
		if (mod.getChannel() != null) queryParamater.setTvChannel("TV channel: " + mod.getChannel());
		if (mod.getProg_title() != null) queryParamater.setTvProTitle("TV Program Title: " + mod.getProg_title());
		rubyAnswer.setQueryParamater(queryParamater);
		
		System.out.println("Tv query start time: " + mod.getStart());
		System.out.println("Tv query end time: " + mod.getEnd());
		rubyAnswer.setBeginTime(mod.getStart());
		rubyAnswer.setEndTime(mod.getEnd());
		// end time processing
		System.out.println("Find list TV Program");
		List< TVProgram > progs = tps.getList( mod, question );
		System.out.println("List TVProgram Size: " + progs.size());
		if (mod.getChannel() == null && mod.getProg_title() == null){
			System.err.println("[TVAnserMapper]: Channel null and program null");
			if (mod.getStart() == null){
				rubyAnswer.setAnswer( UDF_ANS  );
				return rubyAnswer;
			}
			
			if (mod.getStart().equals( mod.getEnd() )){
				rubyAnswer.setAnswer( getChannelAndProgram( progs )  );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getChannelProgAndTime( progs )  );
			return rubyAnswer;
		}
		
		if (mod.getChannel() == null){
			System.err.println("[TVAnserMapper]: Channel null");
			if ( intent.equals( IntentConstants.TV_POL ) && progs.isEmpty()){
				rubyAnswer.setAnswer( "Không!"  );
				return rubyAnswer;
			}
			if (intent.equals( IntentConstants.TV_DAT )){
				rubyAnswer.setAnswer( getChannelAndTime( progs )  );
				return rubyAnswer;
			}
			if (intent.equals( IntentConstants.TV_CHN )){
				rubyAnswer.setAnswer( getChannel( progs )  );
				return rubyAnswer;
			}
			if (mod.getStart() == null){
				rubyAnswer.setAnswer( DEF_ANS  );
				return rubyAnswer;
			}
			if (mod.getStart().equals( mod.getEnd() )){
				if (intent.equals( IntentConstants.TV_CHN )){
					rubyAnswer.setAnswer( getChannel( progs )  );
					return rubyAnswer;
				}
				rubyAnswer.setAnswer( getChannelAndProgram( progs )  );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getChannelProgAndTime( progs )  );
			return rubyAnswer;
		}
		
		if (mod.getProg_title() == null){
			System.err.println("[TVAnserMapper]: Program null");
			if (mod.getStart() != null && mod.getStart().equals( mod.getEnd() )){
				rubyAnswer.setAnswer( getTitle( progs )  );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getTitleAndTime( progs )  );
			return rubyAnswer;
		}
		if (intent.equals( IntentConstants.TV_DAT )){
			if (progs.size() > 0){
				rubyAnswer.setAnswer( getTime( progs )  );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer(mod.getChannel() + " không chiếu " + mod.getProg_title());
			return rubyAnswer;
		}
		
		if (intent.equals( IntentConstants.TV_POL )){
			if (progs.size() > 0){
				rubyAnswer.setAnswer( "Có"  );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer(mod.getChannel() + " không chiếu " + mod.getProg_title());
			return rubyAnswer;
		}
		
		if (mod.getStart() == null){
			rubyAnswer.setAnswer( DEF_ANS  );
			return rubyAnswer;
		}
		
		if (mod.getStart().equals( mod.getEnd() )){
			if (progs.isEmpty()){
				rubyAnswer.setAnswer( "Không có " + mod.getProg_title() + " nào trên kênh " + mod.getChannel() + " vào lúc đó!" );
				return rubyAnswer;
			}
			rubyAnswer.setAnswer( getTitle( progs )  );
			return rubyAnswer;
		}
		
		if (progs.isEmpty()){
			rubyAnswer.setAnswer( "Không có chương trình " + mod.getProg_title() +
					" nào trên " + mod.getChannel() + " vào lúc đó" );
			return rubyAnswer;
		}
		rubyAnswer.setAnswer( getTitleAndTime( progs )  );
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
		
		if (limit < progs.size()){
			time += ". . . ";
		}
		return time;
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
			if (!title.contains(progs.get( i ).getTitle() + "</br>")){
				title += progs.get( i ).getTitle() + "</br>";
			}
		}
		if (limit < progs.size()){
			title += ". . . ";
		}
		
		return title;
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
			if (!channel.contains(progs.get( i ).getChannel())){
				channel += progs.get( i ).getChannel() + "</br>";
			}
		}
		if (limit < progs.size()){
			channel += ". . . ";
		}
		return channel;
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
			title += sdf.format(tv.getStart_date()) + " : " + tv.getTitle() + "</br>";
		}
		if (limit < progs.size()){
			title += ". . . ";
		}
		
		return title;
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
		if (limit < progs.size()){
			info += ". . . ";
		}
		return info;
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
		if (limit < progs.size()){
			info += ". . . ";
		}
		
		return info;
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
			info += tv.getChannel() + " : " + sdf.format(tv.getStart_date()) + " : " + tv.getTitle() + "</br>";
		}
		if (limit < progs.size()){
			info += ". . . ";
		}
		
		return info;
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
		tam.studyFile( "D:\\Workspace\\Code\\FTI\\rubyweb\\AIML_tvd_questions.txt",
				"D:\\Workspace\\Code\\FTI\\rubyweb\\AIML_tvd_questions.out" );
	}

}