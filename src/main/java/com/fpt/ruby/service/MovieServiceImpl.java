package com.fpt.ruby.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.fpt.ruby.dao.MovieDAO;
import com.fpt.ruby.model.Movie;

@Service("movieService")
public class MovieServiceImpl implements MovieService{

	@Autowired	MovieDAO MovieDAO;
	@Override
	@Transactional
	public void persistMovie(Movie movie) {
		MovieDAO.persistMovie(movie);
		
	}

	@Override
	@Transactional
	public void updateMovie(Movie movie) {
		MovieDAO.updateMovie(movie);
		
	}
	@Override
	@Transactional
	public Movie findMovieById(int id) {
		return MovieDAO.findMovieById(id);
	}

	@Override
	@Transactional
	public void deleteMovie(Movie movie) {
		MovieDAO.deleteMovie(movie);
		
	}
	
	@Override
	@Transactional
	public void saveMovie(Movie movie) {
		MovieDAO.saveMovie(movie);
		
	}

	@Override
	public Movie findMovieByTheMovieDbId(int theMovieDbId) {
		return MovieDAO.findMovieByTheMovieDbId(theMovieDbId);
	}

	@Override
	@Transactional
	public Movie getMovieByIdWithAllLazy(final int id) {
		return MovieDAO.getMovieByIdWithAllLazy(id);
	}

}
