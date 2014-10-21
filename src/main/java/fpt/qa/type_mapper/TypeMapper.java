package fpt.qa.type_mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import vn.hus.nlp.utils.UTF8FileUtility;

public class TypeMapper {

	private Map<EnumType, Set<String>> typeMapper;// = new HashMap<EnumType,
													// Set<String>>();

	public TypeMapper(String fileName) {
		typeMapper = new HashMap<EnumType, Set<String>>();
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
	
	
	public EnumType getType(String channel, String program) {
		channel = channel.trim();
		program = program.trim();
		for( EnumType type : typeMapper.keySet() ){
			if(typeMapper.get(type).contains(channel) || typeMapper.get(type).contains(program)) {
				return type;
			}
		}
		return EnumType.ALL;
	}
	
	public Set<String> getContent(EnumType type) {
		return typeMapper.get(type);
	}
	
	// Test
	void print() {
		System.out.println( "size = " + typeMapper.size() );
		for( EnumType word : typeMapper.keySet() ){
			System.out.print( word + "\t" );
			Set< String > syns = typeMapper.get( word );
			System.out.println( syns.toString() );
		}
	}
	private EnumType getType(String type) {
		// TODO Auto-generated method stub
		switch (type) {
		case "SPORT":
			return EnumType.SPORT;
		case "CARTOON":
			return EnumType.CARTOON;
		case "DOC_FILM":
			return EnumType.DOC_FILM;
		case "FASHION":
			return EnumType.FASHION;
		case "GAME_SHOW":
			return EnumType.GAME_SHOW;
		case "NEW":
			return EnumType.NEW;
		case "MULTI_MEDIA":
			return EnumType.MULTI_MEDIA;
		case "FILM":
			return EnumType.FILM;
		default:
			return EnumType.ALL;
		}
	}
	
	public static void main(String[] args) {
		TypeMapper map = new TypeMapper( "resourcesc/type_mapper.txt" );
		map.print();
	}
	
}
