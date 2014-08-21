package fpt.qa.rubyweb;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fpt.ruby.App;
import com.fpt.ruby.model.RubyAnswer;

@Controller
@RequestMapping("/app")
public class AppController {
	private App app;
	
	@PostConstruct
	public void init(){
		app = new App();
	}
	
	
	
	@RequestMapping(value="/getAnswer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RubyAnswer prototypeGetAnswer(@RequestParam("question") String question){
		return app.getAnswer(question);
	}
	
	@RequestMapping(value="/test1", method = RequestMethod.GET)
	@ResponseBody
	public String test1(){
	return "test";
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
