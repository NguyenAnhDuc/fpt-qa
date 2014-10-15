package com.fpt.ruby.nlp;

import java.util.List;

import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.model.TVProgram;
import com.fpt.ruby.service.LogService;

public interface TVAnswerMapper{
	void init();
	RubyAnswer getAnswer ( String question, LogService logService );
	
	String getTime ( List< TVProgram > progs );
	String getTitle ( List< TVProgram > progs );
	String getChannel ( List< TVProgram > progs );
	String getTitleAndTime ( List< TVProgram > progs );
	String getChannelAndProgram ( List< TVProgram > progs );
	String getChannelAndTime ( List< TVProgram > progs );
	String getChannelProgAndTime ( List< TVProgram > progs );
	String getEndDate ( List< TVProgram > progs );
}
