package fpt.qa.typeclassifier;

import java.util.ArrayList;
import java.util.List;

import fpt.qa.additionalinformation.modifier.ConjunctionWithType;
import fpt.qa.mdnlib.struct.pair.Pair;
import fpt.qa.type_mapper.ProgramType;
import fpt.qa.type_mapper.TypeMapper;

public class ProgramTypeExtractor {

	public ConjunctionWithType conjunctionWithType;

	public ProgramTypeExtractor() {
		this.conjunctionWithType = new ConjunctionWithType(
				"src/main/resources/");
	}
	public ProgramTypeExtractor(String resourcePath) {
		this.conjunctionWithType = new ConjunctionWithType(resourcePath);
	}
	
	public List<Pair<ArrayList<String>, String>> getNER(String text) {
		return this.conjunctionWithType
				.getListRelevantConjunctionsWithType(text);
	}

	public String tagText(String text) {
		text = lowecase(text);
		List<Pair<ArrayList<String>, String>> listNer = getNER(text);
		for (Pair<ArrayList<String>, String> p : listNer) {
			for (String s : p.first) {
				//System.out.println(p.first + "~~~"+p.second);
				if (text.contains(s)) {
					String tag = makeTag(p.second, s);
					text = text.replaceAll(s, tag);
					break ;
				}
			}
		}
		return text;
	}
	
	
	public ProgramType getType(String text) {
		text = lowecase(text);
		List<Pair<ArrayList<String>, String>> listNer = getNER(text);
		for (Pair<ArrayList<String>, String> p : listNer) {
			if(p.second.equalsIgnoreCase("TYPE")){
				//System.out.println("list : "+p.first);
				for(String s : p.first) {
					ProgramType type = TypeMapper.getType(s.trim());
					//System.out.println(s);
					if(!type.equals(ProgramType.ALL)) {
						return type ;
					}
				}
			}
		}
		return ProgramType.ALL;
	}
	
	
	public static void main(String[] args) {
		System.out
				.println(new ProgramTypeExtractor()
						.getType("Kênh vtv 3 hôm nay có phim gì?"));
		System.out
		.println(new ProgramTypeExtractor()
				.tagText("Kênh vtv 3 hôm nay có phim gì?"));
	}

	public String makeTag(String type, String obj) {
		return "<" + type + ">" + obj + "</" + type + ">";
	}
	
	private String lowecase( String text) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < text.length();i++) {
			sb.append(Character.toLowerCase(text.charAt(i)));
		}
		return sb.toString();
	}
}
