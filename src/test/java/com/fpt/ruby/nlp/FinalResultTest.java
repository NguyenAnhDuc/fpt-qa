package com.fpt.ruby.nlp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fpt.ruby.helper.IOHelp;
import com.fpt.ruby.helper.ProcessHelper;
import com.fpt.ruby.service.CinemaService;
import com.fpt.ruby.service.LogService;
import com.fpt.ruby.service.MovieFlyService;
import com.fpt.ruby.service.mongo.MovieTicketService;


public class FinalResultTest {
	@Test
	public void testDomainMovie(){
		NlpHelper.normalizeQuestion("init");
		MovieFlyService movieFlyService = new MovieFlyService();
		MovieTicketService movieTicketService = new MovieTicketService();
		CinemaService cinemaService = new CinemaService();
		LogService logService = new LogService();
		String dir = (new IOHelp()).getClass().getClassLoader().getResource("").getPath();
		List<String> datas = IOHelp.readFileToList(dir  + "/TestFinalResult_Movie.txt");
		List<Data> dataTests = new ArrayList<Data>();
		for (String  data : datas){
			String[] strings = data.split("\\|");
			Data adata = new Data();
			adata.question = strings[0].trim();
			if (strings.length > 1)
				adata.answer = strings[1].trim();
			dataTests.add(adata);
		}
		for (Data data : dataTests)
			if (!data.question.isEmpty()){
				System.out.println("Question: " + data.question);
				String finalAnswer = ProcessHelper.getAnswer(data.question, movieFlyService, movieTicketService, cinemaService, logService).getAnswer();
				if  (!data.answer.isEmpty())
					Assert.assertTrue(finalAnswer.toLowerCase().contains(data.answer.toLowerCase()));
				else Assert.assertTrue(!finalAnswer.toLowerCase().contains("xin lỗi"));
			}
	}
}
