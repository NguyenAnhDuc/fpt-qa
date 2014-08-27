package com.fpt.ruby.model;


public class MovieFly {
	private String title;
	private String genre;
	private String actor;
	private String writer;
	private String language;
	private String country;
	private String awards;
	private String director;
	
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAwards() {
		return awards;
	}
	public void setAwards(String awards) {
		this.awards = awards;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	private String runtime;
	private float imdbRating;
	private String imdbId;
	private String plot;
	private int year;
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
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public float getImdbRating() {
		return imdbRating;
	}
	public void setImdbRating(float imdbRating) {
		this.imdbRating = imdbRating;
	}
	public String getImdbId() {
		return imdbId;
	}
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	public String getPlot() {
		return plot;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
}