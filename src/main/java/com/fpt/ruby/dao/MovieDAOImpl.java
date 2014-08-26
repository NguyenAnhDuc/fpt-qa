package com.fpt.ruby.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
	public List<Movie> findAll() {
		return (List<Movie>) sessionFactory.getCurrentSession().createCriteria(Movie.class).list();
	}
	
	@Override
	public List<Movie> findMovieMatchTitle(String matchTitle) {
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(Movie.class);
		cr.add(Restrictions.like("title", matchTitle));
		List<Movie> movies = new ArrayList<Movie>();
		for (Movie movie : (List<Movie>) cr.list()){
			Hibernate.initialize(movie.getGenres());
			//Hibernate.initialize(movie.getProduction_companies());
			//Hibernate.initialize(movie.getProduction_countries());
			//Hibernate.initialize(movie.getSpoken_languages());
			movies.add(movie);
		}
		return movies;
	}
	
	@Override
	public Movie findMovieById(int id) {
		System.out.println("Begin get movie with Genres");
		Session session = sessionFactory.getCurrentSession();
		
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