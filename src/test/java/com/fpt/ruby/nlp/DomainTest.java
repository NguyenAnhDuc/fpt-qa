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
	public void testDomain(){
		
		String dir = (new IOHelp()).getClass().getClassLoader().getResource("").getPath();
		domainClassifier = new DomainClassifier( dir );
		List<String> questions = IOHelp.readFileToList(dir  + "/TestDomainData.txt");
		for (String question : questions){
			Assert.assertEquals("Movie Domain Expected", "movie", domainClassifier.getDomain(question));
		}
	}
}
