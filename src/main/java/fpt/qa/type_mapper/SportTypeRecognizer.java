package fpt.qa.type_mapper;

import java.util.*;

public class SportTypeRecognizer extends TypeRecognizer {
	public SportTypeRecognizer() {
		SetType(ProgramType.SPORT);
		
		String[] dedicatedChannels = new String[] { "fox sports" };
		String[] typeKeywords = new String[] { "sport", "bongda", "football",
				"bongchuyen", "soccer", "championleague" };
		String[] relatedKeyword = new String[] { "sport", "bóng đá",
				"football", "bóng chuyền", "soccer", "match",
				"champion league", "cúp", "vô địch", "thi đấu", "đua xe", "racing"};

		super.loadConfig(dedicatedChannels, typeKeywords, relatedKeyword);
	}
}
