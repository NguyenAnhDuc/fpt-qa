package com.fpt.ruby.service;

import java.util.List;

import com.fpt.ruby.model.Genre;

public interface GenreService {

	void persistGenre(Genre genre);

	Genre findGenreById(int id);


	void updateGenre(Genre genre);

	void deleteGenre(Genre genre);
}
