package com.fpt.ruby.nlp;

import java.util.List;

import mdnlib.struct.pair.Pair;

import com.fpt.ruby.conjunction.ConjunctionHelper;

import fpt.qa.intent.detection.IntentConstants;

public class Modifiers {
	public static ConjunctionHelper conjunctionHelper = new ConjunctionHelper();
	
	private String title;
	private String genre;
	private String actor;
	private String director;
	private String country;
	private String lang;
	private String audience;
	private String award;
	private String cin_name;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getCin_name() {
		return cin_name;
	}
	public void setCin_name(String cin_name) {
		this.cin_name = cin_name;
	}
	
	public boolean atLeastOneOtherFeatureNotNull(){
		if (genre != null || actor != null || director != null || lang != null){
			return true;
		}
		
		if (country != null || award != null || audience != null){
			return true;
		}
		
		return false;
	}
	

	public static Modifiers getModifiers(String question){
		List<Pair<String, String>> conjunctions = conjunctionHelper.getConjunction(question);
		Modifiers res = new Modifiers();
		for (Pair<String, String> conjunction : conjunctions ){
			String head = conjunction.second;
			String val = conjunction.first.replace("{", "").replace("}", "");
			
			if (head.equals(IntentConstants.MOV_TITLE)){
				res.setTitle(val);
				continue;
			}
			
			if (head.equals(IntentConstants.CIN_NAME)){
				res.setCin_name(val);
				continue;
			}
			
			if (head.equals("loại_phim")){
				res.setGenre(val);
				continue;
			}
			
			if (head.equals("diễn_viên")){
				res.setActor(val);
				continue;
			}
			
			if (head.equals("đạo_diễn")){
				res.setDirector(val);
				continue;
			}
			
			if (head.equals(IntentConstants.MOV_AUDIENCE)){
				res.setAudience(val);
				continue;
			}
			
			if (head.equals(IntentConstants.MOV_AWARD)){
				res.setAward(val);
				continue;
			}
			
			if (head.equals(IntentConstants.MOV_COUNTRY)){
				res.setCountry(val);
				continue;
			}
			
			if (head.equals(IntentConstants.MOV_LANG)){
				res.setLang(val);
				continue;
			}
		}
		
		return res;
	}
	
}
