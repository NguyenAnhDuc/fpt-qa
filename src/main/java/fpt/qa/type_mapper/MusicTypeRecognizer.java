package fpt.qa.type_mapper;

public class MusicTypeRecognizer extends TypeRecognizer {

	public MusicTypeRecognizer() {
		SetType(ProgramType.MUSIC);

		String[] dedicatedChannels = new String[] { "mtv" };
		String[] typeKeywords = new String[] { "canhac", "musik", "music",
				"muzik", "cakhuc", "giaidieu", "nhay", "hat", "tinhca" };
		String[] relatedKeyword = new String[] { "ca nhạc", "music", "muzik",
				"sân khấu", "thần tượng", "ca khúc", "ca sĩ", "musik", "clip",
				"bài hát", "âm nhạc", "nhóm nhạc", "trình bày", "giai điệu",
				"ryhtm", "song", "writer", "singer", "soudcloud", "mp3",
				"youtube", "phối khí", "thanh nhạc"};

		super.loadConfig(dedicatedChannels, typeKeywords, relatedKeyword);
	}
}
