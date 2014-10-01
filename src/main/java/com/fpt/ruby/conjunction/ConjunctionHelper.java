package com.fpt.ruby.conjunction;

import java.io.File;
import java.util.List;

import fpt.qa.additionalinformation.modifier.ConjunctionWithType;
import fpt.qa.mdnlib.struct.pair.Pair;



public class ConjunctionHelper {
	private ConjunctionWithType conjunctionWithType;
	public ConjunctionHelper(){
		 conjunctionWithType = new ConjunctionWithType( "" );
	}
	public ConjunctionHelper(String dir){
		 conjunctionWithType = new ConjunctionWithType( dir );
	}
	
	public List<Pair<String, String>> getConjunction(String text){
		return conjunctionWithType.getOriginRelevantConjunctionsWithType(text); 
	}
}