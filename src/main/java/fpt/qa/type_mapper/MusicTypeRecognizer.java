package fpt.qa.type_mapper;

public class MusicTypeRecognizer extends TypeRecognizer {

	public MusicTypeRecognizer() {
		SetType(ProgramType.MUSIC);
		
		String[] dedicatedChannels = new String[] { "mtv" };
		String[] typeKeywords = new String[] { "canhac", "musik", "music",
				"muzik", "cakhuc", "giaidieu" };
		String[] relatedKeyword = new String[] { "ca nhạc", "music", "muzik",
				"ca khúc", "ca sĩ", "musik", "clip", "bài hát", "âm nhạc",
				"giai điệu", "ryhtm", "song", "writer", "singer", "soudcloud", "mp3"};

		super.loadConfig(dedicatedChannels, typeKeywords, relatedKeyword);
	}

}
