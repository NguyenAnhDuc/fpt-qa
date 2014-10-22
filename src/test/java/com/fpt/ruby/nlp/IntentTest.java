package com.fpt.ruby.nlp;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fpt.ruby.helper.IOHelp;

import fpt.qa.intent.detection.MovieIntentDetection;
import fpt.qa.intent.detection.TVIntentDetection;

public class IntentTest {
	@Test
	public void testDomainMovie(){
		NlpHelper.normalizeQuestion("init");
		TVIntentDetection tvIntentDetection = new TVIntentDetection();
		String dir = (new IOHelp()).getClass().getClassLoader().getResource("").getPath();
		tvIntentDetection.init( dir + "/qc/tv", dir + "/dicts");
		List<String> datas = IOHelp.readFileToList(dir  + "/TestIntent.txt");
		for (String data : datas)
		{
			String question = data.split("\\|")[0].trim();
			String domain = data.split("\\|")[1].trim();
			String intent_expected = data.split("\\|")[2].trim();
			if (!question.isEmpty()){
				System.out.println("Question: " + question);
				if (domain.toLowerCase().equals("movie"))
					Assert.assertEquals("Intent Expected", intent_expected, MovieIntentDetection.getIntent(question));
				if (domain.toLowerCase().equals("tv"))
					Assert.assertEquals("Intent Expected", intent_expected,tvIntentDetection.getIntent(question));
			}
		}
			
	}

}
