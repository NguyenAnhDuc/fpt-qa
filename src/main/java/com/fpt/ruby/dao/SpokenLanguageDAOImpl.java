package com.fpt.ruby.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fpt.ruby.model.SpokenLanguage;

@Repository("SpokenLanguageDAO")
public class SpokenLanguageDAOImpl implements SpokenLanguageDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void persistSpokenLanguage(SpokenLanguage spokenLanguage) {
		sessionFactory.getCurrentSession().persist(spokenLanguage);
	}

	@Override
	public SpokenLanguage findSpokenLanguageByIso_639_1(String iso_639_1) {
		return (SpokenLanguage) sessionFactory.getCurrentSession().get(SpokenLanguage.class, iso_639_1);
	}

	@Override
	public void updateSpokenLanguage(SpokenLanguage spokenLanguage) {
		sessionFactory.getCurrentSession().update(spokenLanguage);

	}
	@Override
	public void deleteSpokenLanguage(SpokenLanguage spokenLanguage) {
		sessionFactory.getCurrentSession().delete(spokenLanguage);

	}

}