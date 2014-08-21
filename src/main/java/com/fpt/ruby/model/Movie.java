package com.fpt.ruby.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "MOVIE")
public class Movie {

	@Id
	@Column (name = "ID", nullable = true)
	@GeneratedValue
	private int id;

	@Column(name = "original_title", nullable = true) private String original_title;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "movie_genre", 
			   joinColumns = {@JoinColumn(name="movie_id",nullable = false, updatable = false)},
			   inverseJoinColumns = {@JoinColumn(name = "genre_id",nullable = false, updatable = false)})
	private Set<Genre> genres;
	
	@Column(name = "adult", nullable = true) private boolean adult;
	@Column(name = "budget", nullable = true) private double budget;
	@Column(name = "homepage", nullable = true) private String homepage;
	@Column(name = "imdb_id", nullable = true) private String imdb_id;
	@Column(name = "themoviedb_id", nullable = true) private int themovie_id;
	@Column(name = "popularity", nullable = true) private Double popularity;
	
	@Column(name = "release_date", nullable = true) private Date release_date;
	@Column(name = "revenue", nullable = true) private double revenue;
	@Column(name = "runtime", nullable = true) private int runtime;
	@Column(name = "status", nullable = true) private String status;
	@Column(name = "overview", nullable = true) private String overview;
	@Column(name = "tagline", nullable = true) private String tagline;
	@Column(name = "title", nullable = true) private String title;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "movie_productioncountry", 
			   joinColumns = {@JoinColumn(name="movie_id",nullable = false, updatable = false)},
			   inverseJoinColumns = {@JoinColumn(name = "productioncountry_iso3166",nullable = false, updatable = false)})
	private Set<ProductionCountry> production_countries;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "movie_spokenlanguage", 
			   joinColumns = {@JoinColumn(name="movie_id",nullable = false, updatable = false)},
			   inverseJoinColumns = {@JoinColumn(name = "spokenlanguage_iso639",nullable = false, updatable = false)})
	private Set<SpokenLanguage> spoken_languages;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "movie_productioncompany", 
			   joinColumns = {@JoinColumn(name="movie_id",nullable = false, updatable = false)},
			   inverseJoinColumns = {@JoinColumn(name = "productioncompany_id",nullable = false, updatable = false)})
	private Set<ProductionCompany> production_companies;
	
	/*
	spoken_languages
	production_countries
	production_companies
	 */

	public int getId() {
		return id;
	}

	public boolean isAdult() {
		return adult;
	}

	public void setAdult(boolean adult) {
		this.adult = adult;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getImdb_id() {
		return imdb_id;
	}

	public void setImdb_id(String imdb_id) {
		this.imdb_id = imdb_id;
	}

	public int getThemovie_id() {
		return themovie_id;
	}

	public void setThemovie_id(int themovie_id) {
		this.themovie_id = themovie_id;
	}

	public Double getPopularity() {
		return popularity;
	}

	public void setPopularity(Double popularity) {
		this.popularity = popularity;
	}

	public Date getRelease_date() {
		return release_date;
	}

	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<ProductionCountry> getProduction_countries() {
		return production_countries;
	}

	public void setProduction_countries(Set<ProductionCountry> production_countries) {
		this.production_countries = production_countries;
	}

	public Set<SpokenLanguage> getSpoken_languages() {
		return spoken_languages;
	}

	public void setSpoken_languages(Set<SpokenLanguage> spoken_languages) {
		this.spoken_languages = spoken_languages;
	}

	public Set<ProductionCompany> getProduction_companies() {
		return production_companies;
	}

	public void setProduction_companies(Set<ProductionCompany> production_companies) {
		this.production_companies = production_companies;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOriginal_title() {
		return original_title;
	}

	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}

	/*public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public int getImdb_id() {
		return imdb_id;
	}

	public void setImdb_id(int imdb_id) {
		this.imdb_id = imdb_id;
	}*/

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}
}
