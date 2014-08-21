package com.fpt.ruby.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fpt.ruby.model.Movie;

@Repository("MovieDAO")
public class MovieDAOImpl implements MovieDAO {

	@Autowired
	private SessionFactory sessionFactory;

	
	@Override
	public void persistMovie(Movie movie) {
		sessionFactory.getCurrentSession().persist(movie);
	}

	@Override
	public Movie findMovieById(int id) {
		System.out.println("Begin get movie with Genres");
		Session session = sessionFactory.getCurrentSession();
		System.out.println("exception?");
		
		return (Movie) sessionFactory.getCurrentSession().get(Movie.class, id);
	}

	@Override
	public Movie findMovieByTheMovieDbId(int theMovieDbId) {
		// TODO Auto-generated method stub
		return (Movie) sessionFactory.getCurrentSession().get(Movie.class, theMovieDbId);
	}
	
	@Override
	public void updateMovie(Movie Movie) {
		sessionFactory.getCurrentSession().update(Movie);

	}
	
	@Override
	public void saveMovie(Movie Movie) {
		sessionFactory.getCurrentSession().save(Movie);
	}
	
	@Override
	public void deleteMovie(Movie movie) {
		sessionFactory.getCurrentSession().delete(movie);

	}

	@Override
	public Movie getMovieByIdWithAllLazy(int id) {
		Movie movie = (Movie) sessionFactory.getCurrentSession().get(Movie.class, id);
		Hibernate.initialize(movie.getGenres());
		Hibernate.initialize(movie.getProduction_companies());
		Hibernate.initialize(movie.getProduction_countries());
		Hibernate.initialize(movie.getSpoken_languages());
		return movie;
	}

	

}