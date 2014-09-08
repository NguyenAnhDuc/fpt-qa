package com.fpt.ruby.conjunction;

import java.io.File;
import java.util.List;

import mdnlib.struct.pair.Pair;
import modifier.ConjunctionWithType;



public class ConjunctionHelper {
	private ConjunctionWithType conjunctionWithType;
	public ConjunctionHelper(){
		 conjunctionWithType = new ConjunctionWithType();
		 conjunctionWithType.loadConjunctionType( new File( "movies_infor.txt"));
	}
	public ConjunctionHelper(String dir){
		 conjunctionWithType = new ConjunctionWithType();
		 conjunctionWithType.loadConjunctionType( new File(dir + "/movies_infor.txt"));
	}
	
	public List<Pair<String, String>> getConjunction(String text){
		return conjunctionWithType.getRelevantConjunctionsWithType(text); 
	}
	
	/*public List<Pair<String, String>> getConjunction(String text){
		return conjunctionWithType.getOriginRelevantConjunctionsWithType(text); 
	}*/
	
	/*public List<Pair<ArrayList<String>, String>> getListConjunction(String text){
		return conjunctionWithType.getListRelevantConjunctionsWithType(text); 
	}*/

 }
