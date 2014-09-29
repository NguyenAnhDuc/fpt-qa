package com.fpt.ruby.model;

import java.util.Date;

public class RubyAnswer {

	private String question;
	private String answer;
	private boolean isInCache;
	private String intent;
	private String questionType;
	private String movieTitle;
	private MovieTicket movieTicket;
	private QuestionStructure questionStructure;
	private Date beginTime;
	private Date  endTime;
	
	
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getMovieTitle() {
		return movieTitle;
	}
	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	public MovieTicket getMovieTicket() {
		return movieTicket;
	}
	public void setMovieTicket(MovieTicket movieTicket) {
		this.movieTicket = movieTicket;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public boolean isInCache() {
		return isInCache;
	}
	public void setInCache(boolean isInCache) {
		this.isInCache = isInCache;
	}
	public QuestionStructure getQuestionStructure() {
		return questionStructure;
	}
	public void setQuestionStructure(QuestionStructure questionStructure) {
		this.questionStructure = questionStructure;
	}
	
}
