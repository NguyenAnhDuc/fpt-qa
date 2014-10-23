package fpt.qa.type_mapper;

import java.util.*;

public class FilmTypeRecognizer extends TypeRecognizer {

	public FilmTypeRecognizer() {
		SetType(ProgramType.FILM);
		
		String[] dedicatedChannels = new String[] { "hbo", "starmovie",
				"screenred", "vtvcab2" };
		String[] typeKeywords = new String[] { "phim", "film", "fim" };
		String[] relatedKeyword = new String[] { "phim", "film", "truyện",
				"điện ảnh", "movie", "đạo diễn", "diễn viên", "actor",
				"actress", "director", "imdb", "espisode", "tập", "phần"};

		super.loadConfig(dedicatedChannels, typeKeywords, relatedKeyword);
	}
}
