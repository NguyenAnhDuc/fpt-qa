package com.fpt.ruby.service;

import java.util.List;

import com.fpt.ruby.model.Movie;

public interface MovieService {

	void persistMovie(Movie movie);

	Movie findMovieById(int id);
	
	Movie getMovieByIdWithAllLazy(int  id);

	Movie findMovieByTheMovieDbId(int theMovieDbId);

	void updateMovie(Movie movie);

	void deleteMovie(Movie movie);
	
	void saveMovie(Movie movie);
	
	List<Movie> findAll();
	
	List<Movie> findMovieMatchTitle(String matchTitle);
}
