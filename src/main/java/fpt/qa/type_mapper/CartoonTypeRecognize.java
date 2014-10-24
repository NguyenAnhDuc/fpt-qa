package fpt.qa.type_mapper;

public class CartoonTypeRecognize extends TypeRecognizer {
	public CartoonTypeRecognize() {
		SetType(ProgramType.CARTOON);

		String[] dedicatedChannels = new String[] { "disney", "cartoon" };
		String[] typeKeywords = new String[] { "hoathinh", "cartoon", "disney" };
		String[] relatedKeyword = new String[] { "hoạt hình", "thiếu nhi",
				"animated", "cartoon", "nhi đồng", "walt disney", "animation",
				"cartoon network", "trẻ nhỏ", "tập", "series", "imdb"};

		super.loadConfig(dedicatedChannels, typeKeywords, relatedKeyword);
	}

}
