package fpt.qa.type_mapper;

import java.util.*;

public class SportTypeRecognizer extends TypeRecognizer {
	public SportTypeRecognizer() {
		SetType(ProgramType.SPORT);

		String[] dedicatedChannels = new String[] { "fox sports" };
		String[] typeKeywords = new String[] { "sport", "bongda", "football",
				"bongchuyen", "soccer", "thethao", "league", "worldcup", "seagame", "asiad", "laliga", "hangnhat", "cupquocgia"};
		String[] relatedKeyword = new String[] { "sport", "bóng đá", "ngoại hạng",
				"football", "bóng chuyền", "soccer", "match", "league", "asiad",
				"racing", "trận đấu", "tấn công", "gặp nhau", "bàn thắng",
				"ghi điểm", "đánh bại", "thắng lợi", "vòng", "livescore",
				"tường thuật", "trực tiếp", "thất thủ", "chiến thắng", "vdqg", "vđqg", "vòng", "tournament", "event", "ticket", "cập nhật"};

		super.loadConfig(dedicatedChannels, typeKeywords, relatedKeyword);
	}
}
