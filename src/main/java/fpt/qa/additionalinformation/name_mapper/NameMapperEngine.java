package fpt.qa.additionalinformation.name_mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fpt.qa.mdnlib.struct.pair.Pair;

public class NameMapperEngine{

//	private NameMapper MOVIE_MAP;
//	private NameMapper FOOT_MAP;
	
	private Map< String, NameMapper > mappers;
	private String resourcePath;

	public NameMapperEngine( String directory ) {
		mappers = new HashMap< String, NameMapper >();
		resourcePath = directory;
		
//		MOVIE_MAP = new NameMapper( directory + IConstants.MOVIE_NAMES);
//		FOOT_MAP = new NameMapper( directory + IConstants.FOOT_NAMES);
//		
//		System.err.println("Load data completed !");
	}
	
	public void loadDomainMapper( String domainName, String dataFileName ){
		NameMapper mapper = new NameMapper( resourcePath + "/data/" + dataFileName );
		mappers.put( domainName, mapper );
	}

	public String getFinalName(String type, String name) {
//		String finalName = "";
//		finalName = MOVIE_MAP.getFinalName(type, name);
//		if (finalName!="") {
//			return finalName;
//		}
//		finalName = FOOT_MAP.getFinalName(type, name);
//		if (finalName!="") {
//			return finalName;
//		}
//		return name;
		for( String domainName : mappers.keySet() ){
			String finalName = mappers.get( domainName ).getFinalName(type, name);
			if( finalName != "" ){
				return finalName;
			}
		}
		
		return name;
	}
	
	public String getFinalName(String domain, String type, String name){
		String finalName;
		if( !mappers.containsKey( domain ) && ( finalName = mappers.get( domain ).getFinalName(type, name) ) != "" ){
			return finalName;
		}
		return name;
	}
	
	public Set<String> getVariationNames(String type, String name) {
//		Set<String> varNames = new HashSet<>();
//		varNames = MOVIE_MAP.getVariationNames(type, name);
//		if (varNames != null) {
//			return varNames;
//		}
//		varNames = FOOT_MAP.getVariationNames(type, name);
//		if (varNames != null) {
//			return varNames;
//		}
//		Set<String> set = new HashSet<>();
//		set.add(name);
//		return set;
		
		for( String domainName : mappers.keySet() ){
			Set< String > varNames = mappers.get( domainName ).getVariationNames( type, name );
			if( varNames != null ){
				return varNames;
			}
		}
				
		Set<String> set = new HashSet<>();
		set.add(name);
		return set;		
	}
	
	public List< Pair< String, String > > getAllNames(){
        List< Pair< String, String > > allNames = new ArrayList< Pair< String, String > > ();

        for( String domainName : mappers.keySet() ){
        	allNames.addAll( mappers.get( domainName ).getAllNames() );
        }
        
//        allNames.addAll( MOVIE_MAP.getAllNames() );
//        allNames.addAll( FOOT_MAP.getAllNames() );        
		
		return allNames;
	}

}
