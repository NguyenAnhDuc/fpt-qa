package fpt.qa.rubyweb;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fpt.ruby.App;
import com.fpt.ruby.model.RubyAnswer;

@Controller
@RequestMapping("/")
public class AppController {
	private App app;
	
	@PostConstruct
	public void init(){
		app = new App();
	}
	
	@RequestMapping(value="/listCachedQuestion", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<String> getListQuestion(){
		return app.getListCachedQuestion();
		//return "haha";
	}
	
	@RequestMapping(value="/getAnswer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RubyAnswer prototypeGetAnswer(@RequestParam("question") String question){
		return app.getAnswer(question);
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String home(Model model){
		List<String> questions = app.getListCachedQuestion();
		model.addAttribute("questions", questions);
		return "chat";
	}
	
	@RequestMapping(value="/testCombo", method = RequestMethod.GET)
	public String testCombo(Model model){
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
