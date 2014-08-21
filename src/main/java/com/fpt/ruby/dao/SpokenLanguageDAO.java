package com.fpt.ruby.dao;

import com.fpt.ruby.model.SpokenLanguage;

public interface SpokenLanguageDAO {
	
		  void persistSpokenLanguage(SpokenLanguage spokenLanguage);
		  
		  SpokenLanguage findSpokenLanguageByIso_639_1(String iso_639_1);
		  
		  void updateSpokenLanguage(SpokenLanguage spokenLanguage);
		  
		  void deleteSpokenLanguage(SpokenLanguage spokenLanguage);
		  
}
