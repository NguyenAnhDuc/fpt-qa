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
		types.add(new CartoonTypeRecognize());
		types.add(new NewsTypeRecognizer());
		
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
		List<Double> rs = new ArrayList<Double>();
		for (TypeRecognizer tp : types) {
			Double confidentLv = tp.belongThisType(channel, program);
			rs.add(confidentLv);
			if (confidentLv > maxScore) {
				maxScore = confidentLv;
				type = tp.getType();
			}
			System.out.println(tp.getType() + " " + confidentLv);
		}
		
		int count = 0;
		List<TypeRecognizer> candidates = new ArrayList<TypeRecognizer>();

		int i = 0;
		for (Double d: rs) {
			if (d.equals(maxScore)) {
				count++;
				candidates.add(types.get(i));
			}
			++i;
		}
		
		System.out.println(maxScore + " --- " + count + "  " + candidates.size());
		if (maxScore > NOT_SURE) {
			if (count == 1)
				return type;
			// >= 2;
			else return getResultFromSearch(candidates, program);
		}

		System.out.println("STEP 3");
		return getResultFromSearch(types, program);
	}
	
	private static ProgramType getResultFromSearch(final List<TypeRecognizer> candidates, String program) {
		// step 3 : ba.fi
		try {
			String doc1 = TypeMapperUtil.getGoogleSearch(program, true);
			String doc2 = TypeMapperUtil.getGoogleSearch(program, false);

			TypeRecognizer result = null;
			Double max = 0.0;

			for (TypeRecognizer tp : candidates) {
				int a1 = TypeMapperUtil.getTotalWord(doc1, tp);
				int a2 = TypeMapperUtil.getTotalWord(doc2, tp);

				Double score = WITH_QUOTE_RATIO * a1 + (1 - WITH_QUOTE_RATIO) * a2;
				System.out.println(tp.getType() + " " + a1 + " " + a2
						+ " score = " + score);

				if (score > max) {
					max = score;
					result = tp;
				}
			}

			if (max > RANGE)
				return result.getType();
			else return ProgramType.ALL;
		} catch (Exception ex) {
			System.out.println("ERRO " + ex.getMessage());
			return ProgramType.ALL;
		} 
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
		switch (type) {
			case "SPORT":
				return ProgramType.SPORT;
			case "CARTOON":
				return ProgramType.CARTOON;
			case "GAME_SHOW":
				return ProgramType.GAME_SHOW;
			case "NEWS":
				return ProgramType.NEWS;
			case "FILM":
				return ProgramType.FILM;
			case "MUSIC":
				return ProgramType.MUSIC;
			default:
				return ProgramType.ALL;
		}
	}

	public static void main(String[] args) {
		TypeMapper map = new TypeMapper("resourcesc/type_mapper.txt");
		map.print();
	}

}
