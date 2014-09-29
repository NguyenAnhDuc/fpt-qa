package com.fpt.ruby.conjunction;

import java.io.File;
import java.util.List;

import fpt.qa.additionalinformation.modifier.ConjunctionWithType;
import fpt.qa.mdnlib.struct.pair.Pair;



public class ConjunctionHelper {
	private ConjunctionWithType conjunctionWithType;
	public ConjunctionHelper(){
		 conjunctionWithType = new ConjunctionWithType( "" );
		 conjunctionWithType.loadConjunctionType( new File( "movies_infor.txt"));
	}
	public ConjunctionHelper(String dir){
		 conjunctionWithType = new ConjunctionWithType( dir );
		 conjunctionWithType.loadConjunctionType( new File(dir + "data/movieNames.txt"));
		 conjunctionWithType.loadConjunctionType( new File(dir + "data/tv_domain.txt"));
	}
	
	public List<Pair<String, String>> getConjunction(String text){
		return conjunctionWithType.getOriginRelevantConjunctionsWithType(text); 
	}
	
	/*public List<Pair<String, String>> getConjunction(String text){
		return conjunctionWithType.getOriginRelevantConjunctionsWithType(text); 
	}*/
	
	/*public List<Pair<ArrayList<String>, String>> getListConjunction(String text){
		return conjunctionWithType.getListRelevantConjunctionsWithType(text); 
	}*/

 }