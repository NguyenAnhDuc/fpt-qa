package fpt.qa.type_mapper;

public class GameShowTypeRecognizer extends TypeRecognizer {
	public GameShowTypeRecognizer() {
		SetType(ProgramType.GAME_SHOW);
		
		String[] dedicatedChannels = new String[] {};
		String[] typeKeywords = new String[] { "gameshow" };
		String[] relatedKeyword = new String[] { "gameshow", "game show",
				"series", "mùa thứ", "tập", "episode", "người chơi", "câu hỏi",
				"question", "format", "cuộc thi", "tổ chức", "phiên bản tiếng việt", "lượt chơi", "youtube", "mc","event", "ticket"};

		super.loadConfig(dedicatedChannels, typeKeywords, relatedKeyword);
	}
}
