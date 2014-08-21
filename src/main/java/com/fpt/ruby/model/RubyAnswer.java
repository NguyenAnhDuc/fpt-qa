package com.fpt.ruby.model;

public class RubyAnswer {
	private String question;
	private String answer;
	private boolean isInCache;
	private QuestionStructure questionStructure;
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
