package com.fpt.ruby.model;

import java.util.Date;

public class QueryParamater {
	private String movieTitle;
	private MovieTicket movieTicket;
	private Date beginTime;
	private Date endTime;
	public QueryParamater(){
		movieTitle = null;
		movieTicket = null;
		beginTime = null;
		endTime = null;
	}
	public Date getStartTime() {
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
	
	public String toShow(){
		StringBuilder result = new StringBuilder().append("{");
		if (movieTitle != null) result.append("Movie Title: " + movieTitle + " | ");
		if (movieTicket != null) result.append("Movie Ticket: " + movieTicket.toShow() + " | ");
		if (beginTime != null) result.append("Begin Time: " + beginTime.toLocaleString() + " | ");
		if (endTime != null) result.append("End Time: " + endTime.toLocaleString() + " | ");
		return result.append("}").toString();
	}
}
