package fpt.qa.type_mapper;

public class NewsTypeRecognizer extends TypeRecognizer {
	public NewsTypeRecognizer() {
		SetType(ProgramType.NEWS);

		String[] dedicatedChannels = new String[] { "CNN" };
		String[] typeKeywords = new String[] { "tintuc", "bantin", "thoisu",
				"news" };
		String[] relatedKeyword = new String[] { "tin tức", "bản tin",
				"thời sự", "tường thuật", "sự kiện", "tin nóng", "news",
				"mới nhất", "nóng hổi", "cập nhật", "liên tục",
				"biên tập viên", "phát thanh viên", "đời sống xã hội",
				"sự kiện", "khủng bố", "chính trị", "BTV", "btv" };

		super.loadConfig(dedicatedChannels, typeKeywords, relatedKeyword);
	}

}
