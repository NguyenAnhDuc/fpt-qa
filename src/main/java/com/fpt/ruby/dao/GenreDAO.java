package com.fpt.ruby.dao;

import com.fpt.ruby.model.Genre;

public interface GenreDAO {
	
		  void persistGenre(Genre genre);
		  
		  Genre findGenreById(int id);

		  void updateGenre(Genre genre);
		  
		  void deleteGenre(Genre genre);
		  
}
