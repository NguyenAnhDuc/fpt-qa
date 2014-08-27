package com.fpt.ruby.dao;

import java.util.List;

import com.fpt.ruby.model.Movie;
public interface MovieDAO {
	
		  void persistMovie(Movie movie);
		  
		  Movie findMovieById(int id);
		  
		  Movie getMovieByIdWithAllLazy(int id);
		  
		  Movie findMovieByTheMovieDbId(int theMovieDbId);
		  
		  void updateMovie(Movie movie);

		  void saveMovie(Movie movie);
		  
		  void deleteMovie(Movie movie);

		  List<Movie> findAll();

		  List<Movie> findMovieMatchTitle(String matchTitle);
		  
}
