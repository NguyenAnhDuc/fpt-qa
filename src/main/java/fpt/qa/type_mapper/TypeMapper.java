package fpt.qa.type_mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.jsoup.nodes.Document;

import vn.hus.nlp.utils.UTF8FileUtility;

public class TypeMapper {
	static List<TypeRecognizer> types;
	private static Map<ProgramType, Set<String>> typeMapper;// = new
															// HashMap<EnumType,
	// Set<String>>();
	private static final double NOT_SURE = 0.3;
	private static final double WITH_QUOTE_RATIO = 0.7;
	private static final double RANGE = 3;

	static {
		types = new ArrayList<TypeRecognizer>();
		types.add(new FilmTypeRecognizer());
		types.add(new GameShowTypeRecognizer());
		types.add(new MusicTypeRecognizer());
		types.add(new SportTypeRecognizer());
		
		for (TypeRecognizer tp : types) {
			tp.show();
			System.out.println("\n________________");
		}
		loadData("resourcesc/type_mapper.txt");
	}

	public TypeMapper() {
	}
	
	private static void loadData(String fileName) {
		typeMapper = new HashMap<ProgramType, Set<String>>();
		String[] lines = UTF8FileUtility.getLines(fileName);
		for (String line : lines) {
			if (line == "" && line.charAt(0) == '#') {
				continue;
			}
			int i = line.indexOf('\t');
			String type = line.substring(0, i).trim();

			Set<String> set = new HashSet<>();
			StringTokenizer tokenizer = new StringTokenizer(
					line.substring(i + 1), ",");
			while (tokenizer.hasMoreTokens()) {
				set.add(tokenizer.nextToken().trim());
			}
			typeMapper.put(getType(type), set);
		}
	}

	public TypeMapper(String fileName) {
		loadData(fileName);
	}

	public ProgramType getType(String channel, String program) {
		channel = channel.trim();
		program = program.trim();

		System.out.println("STEP 1");
		// step 1 : based on prepared data
		for (ProgramType type : typeMapper.keySet()) {
			if (typeMapper.get(type).contains(channel)
					|| typeMapper.get(type).contains(program)) {
				return type;
			}
		}

		System.out.println("STEP 2");
		// step 2 : based on keywords
		Double maxScore = 0.0;
		ProgramType type = null;

		for (TypeRecognizer tp : types) {
			Double confidentLv = tp.belongThisType(channel, program);
			if (confidentLv > maxScore) {
				maxScore = confidentLv;
				type = tp.getType();
			}
			System.out.println(tp.getType() + " " + confidentLv);
		}

		if (maxScore > NOT_SURE) {
			return type;
		}

		System.out.println("STEP 3");
		// step 3 : ba.fi
		try {
			String doc1 = TypeMapperUtil.getGoogleSearch(program, true);
			String doc2 = TypeMapperUtil.getGoogleSearch(program, false);

			TypeRecognizer result = null;
			Double max = 0.0;

			for (TypeRecognizer tp : types) {
				int a1 = TypeMapperUtil.getTotalWord(doc1, tp);
				int a2 = TypeMapperUtil.getTotalWord(doc2, tp);

				Double score = WITH_QUOTE_RATIO * a1 + a2;
				System.out.println(tp.getType() + " " + a1 + " " + a2
						+ " score = " + score);

				if (score > max) {
					max = score;
					result = tp;
				}
			}

			if (max > RANGE)
				return result.getType();
		} catch (Exception ex) {
			System.out.println("ERRO " + ex.getMessage());
		}

		return ProgramType.ALL;
	}

	public Set<String> getContent(ProgramType type) {
		return typeMapper.get(type);
	}

	// Test
	void print() {
		System.out.println("size = " + typeMapper.size());
		for (ProgramType word : typeMapper.keySet()) {
			System.out.print(word + "\t");
			Set<String> syns = typeMapper.get(word);
			System.out.println(syns.toString());
		}
	}

	private static ProgramType getType(String type) {
		// TODO Auto-generated method stub
		switch (type) {
		case "SPORT":
			return ProgramType.SPORT;
		case "CARTOON":
			return ProgramType.CARTOON;
		case "DOC_FILM":
			return ProgramType.DOC_FILM;
		case "FASHION":
			return ProgramType.FASHION;
		case "GAME_SHOW":
			return ProgramType.GAME_SHOW;
		case "NEW":
			return ProgramType.NEW;
		case "MULTI_MEDIA":
			return ProgramType.MULTI_MEDIA;
		case "FILM":
			return ProgramType.FILM;
		default:
			return ProgramType.ALL;
		}
	}

	public static void main(String[] args) {
		TypeMapper map = new TypeMapper("resourcesc/type_mapper.txt");
		map.print();
	}

}
