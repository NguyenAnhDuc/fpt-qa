package fpt.qa.additionalinformation.name_mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fpt.qa.mdnlib.struct.pair.Pair;

/**
 * @author Thien BUI-DUC, hus.ict@gmail.com
 *         <p>
 *         4 Sep 2014, 14:42:30
 *         <p>
 *         For testing.
 */
public class NamedEngineImp implements NamedEngine {

	private NameMapper MOVIE_MAP;

	private NameMapper FOOT_MAP; 

	public NamedEngineImp( String directory ) {
		MOVIE_MAP = new NameMapper( directory + IConstants.MOVIE_NAMES);
		FOOT_MAP = new NameMapper( directory + IConstants.FOOT_NAMES);
		System.err.println("Load data completed !");
	}

	@Override
	public String getFinalName(String type, String name) {
		String finalName = "";
		finalName = MOVIE_MAP.getFinalName(type, name);
		if (finalName!="") {
			return finalName;
		}
		finalName = FOOT_MAP.getFinalName(type, name);
		if (finalName!="") {
			return finalName;
		}
		return name;
	}
	
	
	@Override
	public Set<String> getVariationNames(String type, String name) {
		Set<String> varNames = new HashSet<>();
		varNames = MOVIE_MAP.getVariationNames(type, name);
		if (varNames != null) {
			return varNames;
		}
		varNames = FOOT_MAP.getVariationNames(type, name);
		if (varNames != null) {
			return varNames;
		}
		Set<String> set = new HashSet<>();
		set.add(name);
		return set;
	}
	
	@Override
	public List< Pair< String, String > > getAllNames(){
        List< Pair< String, String > > allNames = new ArrayList< Pair< String, String > > ();

        allNames.addAll( MOVIE_MAP.getAllNames() );
        allNames.addAll( FOOT_MAP.getAllNames() );        
		
		return allNames;
	}

}
