package com.fpt.ruby.service;

import com.fpt.ruby.model.SpokenLanguage;

public interface SpokenLanguageService {

	void persistSpokenLanguage(SpokenLanguage spokenLanguage);

	SpokenLanguage findSpokenLanguageByIso_639_1(String iso_639_1 );

	void updateSpokenLanguage(SpokenLanguage spokenLanguage);

	void deleteSpokenLanguage(SpokenLanguage spokenLanguage);
}
