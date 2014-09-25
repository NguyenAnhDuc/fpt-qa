GITLABQA / rubyweb

 User activity
Files
Commits
Network
Graphs
Issues 0
Merge Requests 0
Wiki
Settings
develop

 rubyweb   ..   helper   ProcessHelper.java
c8fe5fc3a   xu ly khong dau cho movie intent detection Browse Code »
ngandt about 24 hours ago  
 ProcessHelper.java 5.99 KB editrawblamehistory remove
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86
87
88
89
90
91
92
93
94
95
96
97
98
99
100
101
102
103
104
105
106
107
108
109
110
111
112
113
114
115
116
117
118
119
120
121
122
123
124
125
126
127
128
129
130
131
132
133
134
135
136
137
138
139
140
141
142
143
144
145
146
147
148
149
package com.fpt.ruby.helper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.model.TimeExtract;
import com.fpt.ruby.nlp.AnswerMapper;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.service.MovieFlyService;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.fpt.ruby.service.mongo.QuestionStructureService;

import fpt.qa.intent.detection.MovieIntentDetection;
import fpt.qa.intent.detection.NonDiacriticMovieIntentDetection;
import fpt.qa.mdnlib.util.string.DiacriticConverter;

public class ProcessHelper {

	public static RubyAnswer getAnswer(String question,QuestionStructure questionStructure,
										MovieFlyService movieFlyService, MovieTicketService movieTicketService)  {
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setAnswer("this is answer of ruby");
		//rubyAnswer.setAnswer(getAnswer(question, movieFlyService, movieTicketService));
		//rubyAnswer.setAnswer(getSimsimiResponse(question));
		rubyAnswer.setQuestionStructure(questionStructure);
		return rubyAnswer;
	}
	
	public static RubyAnswer getAnswer(String question, MovieFlyService movieFlyService, MovieTicketService movieTicketService) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		String intent = MovieIntentDetection.getIntent(question);
		String intent2 = NonDiacriticMovieIntentDetection.getIntent(question);
		System.out.println("Intent 2: " + intent2);
		if (!DiacriticConverter.hasDiacriticAccents(question)){
			intent = intent2;
		}
		System.out.println("Intent: " + intent);
		rubyAnswer.setQuestion(question);
		rubyAnswer.setIntent(intent);
		String questionType = AnswerMapper.getTypeOfAnswer(intent, question);
		rubyAnswer.setAnswer("Xin lỗi, tôi không trả lời câu hỏi này được");
		System.out.println("Question Type: " + questionType);
		// static question
		try{
			if (questionType.equals(AnswerMapper.Static_Question)){
				String movieTitle = NlpHelper.getMovieTitle(question);
				System.out.println("Movie Title: " + movieTitle);
				List<MovieFly> movieFlies = movieFlyService.findByTitle(movieTitle);
				if (movieFlies.size() == 0){
					movieFlies = new ArrayList<MovieFly>();
					MovieFly movieFly = movieFlyService.searchOnImdbByTitle(movieTitle);
					if (movieFly != null){					
						movieFlyService.save(movieFly);
						movieFlies.add(movieFly);
					}
				}
				rubyAnswer.setAnswer(AnswerMapper.getStaticAnswer(intent, movieFlies));
				rubyAnswer.setQuestionType(AnswerMapper.Static_Question);
				
				
				rubyAnswer.setMovieTitle(movieTitle);
			}
			else if (questionType.equals(AnswerMapper.Dynamic_Question)){
				System.out.println("Dynamic ....");
				MovieTicket matchMovieTicket = NlpHelper.getMovieTicket(question);
				TimeExtract timeExtract = NlpHelper.getTimeCondition(question);
				List<MovieTicket> movieTickets = movieTicketService.findMoviesMatchCondition
												(matchMovieTicket, timeExtract.getBeforeDate(), timeExtract.getAfterDate());
				System.out.println("Size: " + movieTickets.size());
				if (timeExtract.getBeforeDate() != null) rubyAnswer.setBeginTime(timeExtract.getBeforeDate().toLocaleString());
				if (timeExtract.getAfterDate() != null) rubyAnswer.setEndTime(timeExtract.getAfterDate().toLocaleString());
				rubyAnswer.setAnswer(AnswerMapper.getDynamicAnswer(intent, movieTickets));
				rubyAnswer.setQuestionType(AnswerMapper.Dynamic_Question);
				rubyAnswer.setMovieTicket(matchMovieTicket);
				System.out.println("DONE Process");
			} 
			else {
				System.out.println("Feature ..");
				MovieTicket matchMovieTicket = new MovieTicket();
				matchMovieTicket.setCinema("BHD Star Cineplex Icon 68");
				Date today = new Date();
				System.out.println("afterdate: " + today);
				
				// list movie tickets for the duration of one day
				List<MovieTicket> movieTickets = movieTicketService.findMoviesMatchCondition(matchMovieTicket, today, new Date(today.getTime() + 86400000));
				System.out.println("No of returned tickets: " + movieTickets.size());
				rubyAnswer.setAnswer(AnswerMapper.getFeaturedAnswer(question, movieTickets, movieFlyService));
				rubyAnswer.setQuestionType(AnswerMapper.Featured_Question);
				rubyAnswer.setMovieTicket(matchMovieTicket);
			}
		}
		catch (Exception ex){
			System.out.println("Exception! " + ex.getMessage());
			ex.printStackTrace();
		}
		// Log
				Log log = new Log();
				log.setQuestion(question);
				log.setIntent(rubyAnswer.getIntent());
				log.setAnswer(rubyAnswer.getAnswer());
				log.setDate(new Date());
				QueryParamater queryParamater = new QueryParamater();
				queryParamater.setBeginTime(rubyAnswer.getBeginTime());
				queryParamater.setEndTime(rubyAnswer.getEndTime());
				queryParamater.setMovieTitle(rubyAnswer.getMovieTitle());
				queryParamater.setMovieTicket(rubyAnswer.getMovieTicket());
				log.setQueryParamater(queryParamater);
				logService.save(log);
		return rubyAnswer;
	}
	
	public static String getSimsimiResponse(String question){
		System.out.println("Simsimi get answer ....");
		System.out.println("question: " + question);
		try{
			String url = "http://sandbox.api.simsimi.com/request.p?key=9cac0c6c-1810-447b-ad5c-c1c76b2aadeb&lc=vn&text=" 
					+  URLEncoder.encode(question,"UTF-8");
			String jsonString = HttpHelper.sendGet(url);
			System.out.println("response: " + jsonString);
			JSONObject json = new JSONObject(jsonString);
			String answer = json.getString("response");
			return  answer;
		}
		catch (Exception ex){
			return "Em mệt rồi, không chơi nữa, đi ngủ đây!";
		}
		
		
	}
	
	public static RubyAnswer getAnswerFromSimsimi(String question, QuestionStructure questionStructure) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setQuestionStructure(questionStructure);
		return rubyAnswer;
	}
	
	public static QuestionStructure getQuestionStucture(String question,
			QuestionStructureService questionStructureService) {
		QuestionStructure questionStructure = new QuestionStructure();
		String key = NlpHelper.normalizeQuestion(question);
		// If in cache
		if (questionStructureService.isInCache(key)) {
			questionStructure = questionStructureService.getInCache(key);
			return questionStructure;
		}
		// If not in cache
		questionStructure = NlpHelper.processQuestionStructure(question);
		// Cache new question
		questionStructureService.cached(questionStructure);
		// Save to mongodb
		questionStructureService.save(questionStructure);
		return questionStructure;
	}
}