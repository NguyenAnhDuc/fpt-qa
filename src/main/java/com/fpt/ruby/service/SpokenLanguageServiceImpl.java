package com.fpt.ruby.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpt.ruby.dao.SpokenLanguageDAO;
import com.fpt.ruby.model.SpokenLanguage;

@Service("spokenLanguageService")
public class SpokenLanguageServiceImpl implements SpokenLanguageService{

	@Autowired
	SpokenLanguageDAO spokenLanguageDAO;
	
	@Override
	@Transactional
	public void persistSpokenLanguage(SpokenLanguage spokenLanguage) {
		spokenLanguageDAO.persistSpokenLanguage(spokenLanguage);
		
	}

	@Override
	@Transactional
	public void updateSpokenLanguage(SpokenLanguage spokenLanguage) {
		spokenLanguageDAO.updateSpokenLanguage(spokenLanguage);
		
	}
	@Override
	@Transactional
	public SpokenLanguage findSpokenLanguageByIso_639_1(String iso_639_1) {
		return spokenLanguageDAO.findSpokenLanguageByIso_639_1(iso_639_1);
	}

	
	@Override
	@Transactional
	public void deleteSpokenLanguage(SpokenLanguage spokenLanguage) {
		spokenLanguageDAO.deleteSpokenLanguage(spokenLanguage);
		
	}

}
