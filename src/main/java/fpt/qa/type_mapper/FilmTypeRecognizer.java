package fpt.qa.type_mapper;

import java.util.*;
import java.util.regex.Pattern;

public class FilmTypeRecognizer extends TypeRecognizer {

	public FilmTypeRecognizer() {
		SetType(ProgramType.FILM);
		
		String[] dedicatedChannels = new String[] { "hbo", "starmovie",
				"screenred", "vtvcab2" };
		String[] typeKeywords = new String[] { "phim", "film", "fim" };
		String[] relatedKeyword = new String[] { "phim", "film", "truyện",
				"điện ảnh", "movie", "đạo diễn", "diễn viên", "actor", "vai chính", "vai phụ",
				"actress", "director", "imdb", "espisode", "tập", "phần", "youtube", "công chiếu", "kịch bản"};

		super.loadConfig(dedicatedChannels, typeKeywords, relatedKeyword);
	}
	
	protected Double customRecognizer(String channel, String prog) {
		String[] regexes = new String[] {"tap\\d", "\\dt", "t\\d", "s\\d", "ep\\d"};
		for (String reg: regexes) {
			Pattern pat = Pattern.compile(reg);
			if (pat.matcher(prog).find()) return QUITE_CONFIDENT;
		}
		return super.CLUELESS;
	}
}
