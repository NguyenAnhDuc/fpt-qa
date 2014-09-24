package fpt.qa.additionalinformation.name_mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import vn.hus.nlp.utils.UTF8FileUtility;
import fpt.qa.mdnlib.struct.pair.Pair;

/**
 * @author Thien BUI-DUC, hus.ict@gmail.com
 * <p>
 * 04 Sep 2014, 12:56:43
 * <p>
 * Build a names map for the domain names dictionary. 
 */

public class NameMapper {

	private  HashMap<NamedEntity, Set<String>> nameMap;
	
	public NameMapper(String namedFile) {
		nameMap = new HashMap<NamedEntity, Set<String>>();
		String[] lines = UTF8FileUtility.getLines(namedFile);
		for (String line : lines) {
			if(line=="" | line.indexOf('#')==0) {
				continue ;
			}
			int i = line.indexOf('\t');
			String type = line.substring(0, i).trim();
			int j = line.indexOf(';');
            if( j == -1 ){ j = line.length() -1 ; }
			String finalName = line.substring(i + 1, j).trim();
			NamedEntity namedEntity = new NamedEntity(type, finalName);

			Set<String> varNames = new HashSet<>();
			varNames.add(finalName); // add the word itself
			StringTokenizer tokenizer = new StringTokenizer(
					line.substring(j + 1), ",");
			while (tokenizer.hasMoreTokens()) {
				varNames.add(tokenizer.nextToken().trim());
			}
			nameMap.put(namedEntity, varNames);
		}

	}
	
	/**
	 * Gets final names of a given type and name.
	 * 
	 * @param word
	 * @return final name of the type and name.
	 */
	
	public String getFinalName(String type, String name) {
		name = name.trim();
		type = type.trim();
		for (NamedEntity entity : nameMap.keySet()) {
			if (entity.getType().equalsIgnoreCase(type)
					&& nameMap.get(entity).contains(name.trim())) {
				return entity.getFinalName();
			}
		}
		return "";
	}
	
	/**
	 * Gets a list of variable names of a given type and name.
	 * 
	 * @param word
	 * @return a list of variable names of the type and name.
	 */
	public Set<String> getVariationNames(String type, String name) {
		name = name.trim();
		type = type.trim();
		for (NamedEntity entity : nameMap.keySet()) {
			if (entity.getType().equalsIgnoreCase(type)
					&& nameMap.get(entity).contains(name.trim())) {
				return nameMap.get(entity);
			}
		}
		return null; 
	}
	
	/**
	 * Display datas in map
	 * 
	 * @param 
	 * @return 
	 */
	void print() {
		System.out.println("size = " + nameMap.size());
		for (NamedEntity word : nameMap.keySet()) {
			System.out.print(word + "\t");
			Set<String> syns = nameMap.get(word);
			System.out.println(syns.toString());
		}
	}

    public List< Pair< String, String > > getAllNames(){
        List< Pair< String, String> > allNames = new ArrayList< Pair< String, String > >();
    
        for( NamedEntity word : nameMap.keySet() ){
            String type = word.getType();
            for( String name : nameMap.get( word ) ){
                allNames.add( new Pair< String, String >( name, type ) );
            }
        }

        return allNames;
    }
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String args[]) {
		NameMapper map = new NameMapper("resources/data/movieNames.txt");
		map.print();
	}
}
