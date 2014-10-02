package fpt.qa.rubyweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fpt.ruby.helper.ProcessHelper;
import com.fpt.ruby.helper.RedisHelper;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.nlp.TVAnswerMapper;
import com.fpt.ruby.nlp.TVAnswerMapperImpl;
import com.fpt.ruby.service.CinemaService;
import com.fpt.ruby.service.LogService;
import com.fpt.ruby.service.MovieFlyService;
import com.fpt.ruby.service.PersonService;
import com.fpt.ruby.service.mongo.MovieTicketService;

import fpt.qa.domainclassifier.DomainClassifier;

@Controller
@RequestMapping("/")
public class AppController {
	@Autowired PersonService personService;
	@Autowired
	MovieTicketService movieTicketService;
	@Autowired
	MovieFlyService movieFlyService;
	@Autowired
	CinemaService cinemaService;
	@Autowired
	LogService logService;
	static TVAnswerMapper tam = new TVAnswerMapperImpl();
	static DomainClassifier classifier;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	static {
		tam.init();
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		classifier = new DomainClassifier( dir );
	}
	
	/*
	@RequestMapping(value="/listCachedQuestion", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<String> getListQuestion(){
		return app.getListCachedQuestion();
		//return "haha";
	}*/
	
	@RequestMapping(value="/getAnswer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RubyAnswer prototypeGetAnswer(@RequestParam("question") String question){
		String key = NlpHelper.normalizeQuestion(question);
		RubyAnswer rubyAnswer = new RubyAnswer();
		String domain = classifier.getDomain( key );
		//rubyAnswer.setInCache(this.questionStructureService.isInCache(key));
		//rubyAnswer.setQuestion(question);
		// Process question
		//QuestionStructure questionStructure = ProcessHelper.getQuestionStucture(question, questionStructureService );
		//QuestionStructure questionStructure = new QuestionStructure();
		// Process answer
		System.out.println(movieFlyService.test);
		//if (domain.equals( "tv" )){
		if ( question.startsWith( "tv" ) ){
			System.err.println( "[AppController] Domain TV" );
			rubyAnswer = tam.getAnswer( question );
		}
		else{
			System.err.println( "[AppController] Domain Movie" );
			rubyAnswer =  ProcessHelper.getAnswer(question,movieFlyService,movieTicketService,cinemaService,logService);
		}
		return rubyAnswer;
		//return app.getAnswer(question);
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String home(Model model){
		logger.info(personService.name);
		return "chat";
	}
	
	@RequestMapping(value="/test", method = RequestMethod.GET)
	public String test(Model model){
		return "testCombo";
	}
	
	
	
	/*@RequestMapping(value="/allCinema", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BasicDBObject testMovie(){
		BasicDBObject result = new BasicDBObject();
		List<MovieTicket> movieTickets = mongoOperation.findAll(MovieTicket.class);
		Set<String> cinemas = new HashSet<String>();
		Set<String> movies = new HashSet<String>();
		for (MovieTicket movieTicket : movieTickets) {
			System.out.println(movieTicket.getCinema() + " " + movieTicket.getMovie());
			cinemas.add(movieTicket.getCinema());
			movies.add(movieTicket.getMovie());
		}
		result.append("cinemas", cinemas);
		result.append("movies", movies );
		return result;
	}*/
	
}
