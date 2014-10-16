package fpt.qa.rubyweb;

import java.util.Date;

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
import com.fpt.ruby.model.Log;
import com.fpt.ruby.model.QueryParamater;
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
import fpt.qa.mdnlib.util.string.DiacriticConverter;

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
		if (domain.equals( "tv" )){
//		if ( question.startsWith( "tv" ) ){
			System.err.println( "[AppController] Domain TV" );
			rubyAnswer = tam.getAnswer( key,logService );
			// Neu khong tra loi duoc cau hoi co dau, thi chuyen cau hoi do ve cau hoi khong dau va xu ly
			if (DiacriticConverter.hasDiacriticAccents(key) && rubyAnswer.getAnswer().contains(TVAnswerMapperImpl.UDF_ANS)){
				rubyAnswer = tam.getAnswer(DiacriticConverter.removeDiacritics(key),logService);
			}
		}
		else{
			System.err.println( "[AppController] Domain Movie" );
			rubyAnswer =  ProcessHelper.getAnswer(key,movieFlyService,movieTicketService,cinemaService,logService);
		}
		rubyAnswer.setDomain(domain);
		// Log
		Log log = new Log();
		log.setQuestion( question );
		log.setDomain( rubyAnswer.getDomain() );
		log.setIntent( rubyAnswer.getIntent() );
		log.setAnswer( rubyAnswer.getAnswer() );
		log.setDate( new Date() );
		QueryParamater queryParamater = new QueryParamater();
		queryParamater.setBeginTime( rubyAnswer.getBeginTime() );
		queryParamater.setEndTime( rubyAnswer.getEndTime() );
		queryParamater.setMovieTitle( rubyAnswer.getMovieTitle() );
		queryParamater.setMovieTicket( rubyAnswer.getMovieTicket() );
		log.setQueryParamater( rubyAnswer.getQueryParamater() );
		logService.save( log );
				
		return rubyAnswer;
		//return app.getAnswer(question);
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String home(Model model){
		logger.info("HOME CONTROLLER");
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
