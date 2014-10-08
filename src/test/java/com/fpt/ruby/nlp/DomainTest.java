package com.fpt.ruby.nlp;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fpt.ruby.helper.IOHelp;
import com.fpt.ruby.helper.RedisHelper;

import fpt.qa.domainclassifier.DomainClassifier;

public class DomainTest {
	DomainClassifier domainClassifier;
	@Test
	public void testDomainMovie(){
		String dir = (new IOHelp()).getClass().getClassLoader().getResource("").getPath();
		domainClassifier = new DomainClassifier( dir );
		List<String> questions = IOHelp.readFileToList(dir  + "/TestDomainData_Movie.txt");
		for (String question : questions)
			if (!question.isEmpty()){
				System.out.println("Question: " + question);
				Assert.assertEquals("Movie Domain Expected", "movie", domainClassifier.getDomain(question));
			}
	}
	
	@Test
	public void testDomainTV(){
		String dir = (new IOHelp()).getClass().getClassLoader().getResource("").getPath();
		domainClassifier = new DomainClassifier( dir );
		List<String> questions = IOHelp.readFileToList(dir  + "/TestDomainData_TV.txt");
		for (String question : questions)
			if (!question.isEmpty()){
			Assert.assertEquals("TV Domain Expected", "tv", domainClassifier.getDomain(question));
			}
	}
}
