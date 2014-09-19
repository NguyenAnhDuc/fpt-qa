package fpt.qa.rubyweb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fpt.ruby.crawler.CrawlPhimChieuRap;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.mongodb.BasicDBObject;

@Controller
@RequestMapping("/admin")
public class AdminCotroller {
	private MovieTicketService movieTicketService;
	
	@PostConstruct
	public void init(){
		this.movieTicketService = new MovieTicketService();
	}
	
	@RequestMapping(value="/crawlPhimChieuRap", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BasicDBObject crawlPhimChieuRap(){
		CrawlPhimChieuRap crawlPhimChieuRap = new CrawlPhimChieuRap();
		try{
			crawlPhimChieuRap.crawlHaNoi();
			
		}
		catch (Exception ex){
			System.out.println("Done");
			return new BasicDBObject().append("status", "failed");
		}
		return new BasicDBObject().append("status", "success");
	}
	@RequestMapping(value="/old", method = RequestMethod.GET)
	public String testCombo(Model model){
		/*AbsoluteTime absoluteTime = new AbsoluteTime();
		TimeResult timeResult = absoluteTime.getAbsoluteTime("hôm nay là ngày gì");
		System.out.println(timeResult.getBeginTime());
		System.out.println(timeResult.getEndTime());*/
		return "admin";
	}
	
	private void saveSchedule(String cinName, String movieTitle, String time, Date date){
		cinName = cinName.trim();movieTitle = movieTitle.trim(); time = time.trim();
		String[] times = time.split("\\s+");
		for (String etime : times){
			MovieTicket movieTicket = new MovieTicket();
			movieTicket.setCinema(cinName.trim());
			movieTicket.setMovie(movieTitle.trim());
				date.setHours(Integer.parseInt(etime.trim().split(":")[0]));
				date.setMinutes(Integer.parseInt(etime.trim().split(":")[1]));
				date.setSeconds(0);
				movieTicket.setDate(date);
				if (!cinName.isEmpty() && !movieTitle.isEmpty())
					movieTicketService.save(movieTicket);
			
		}
	}
	
	@RequestMapping(value="/crawlManual", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
	public String crawlPhimChieuRap(@RequestParam("cin_name") String cinName, @RequestParam("mov_title") String movieTitle,
											@RequestParam("time") String time, @RequestParam("date") String date ,Model model){
		try{
			System.out.println("cin name: " + cinName);
			Date adate = new Date();
			System.out.println("Date: " + adate.toLocaleString());
			String[] dates = date.trim().split("\\.");
			System.out.println(dates[2] + " | " + dates[1] + " | " + dates[0]);
			adate.setYear(Integer.parseInt(dates[2]) - 1900);
			adate.setMonth(Integer.parseInt(dates[1]) - 1);
			adate.setDate(Integer.parseInt(dates[0]));
			System.out.println("Date: " + adate.toLocaleString());
			saveSchedule(cinName, movieTitle, time, adate);
		}
		catch (Exception ex){
			ex.printStackTrace();
			model.addAttribute("status","failed");
			return "crawl";
			//return new BasicDBObject().append("status", "failed");
		}
		model.addAttribute("status","success");
		List<MovieTicket> tickets = movieTicketService.findTicketToShow();
		model.addAttribute("tickets",tickets);
		return "showTicket";
		//return new BasicDBObject().append("status", "success");
	}
	
	@RequestMapping(value="", method = RequestMethod.GET)
	public String admin(Model model){
		return "admin";
	}
	
	@RequestMapping(value="/crawl", method = RequestMethod.GET)
	public String crawlManual(Model model){
		return "crawl";
	}
	
	@RequestMapping(value="/crawlPhimChieuRap", method = RequestMethod.GET)
	public String crawlPhimChieuRap(Model model){
		return "crawlPhimChieuRap";
	}
	
	
	@RequestMapping(value="show", method = RequestMethod.GET)
	public String showTickets(Model model){
		List<MovieTicket> tickets = movieTicketService.findTicketToShow();
		model.addAttribute("tickets",tickets);
		return "showTicket";
	}
	
	@RequestMapping(value = "/deleteTicket", method = RequestMethod.GET)
	public String deleteBot(@RequestParam("ticketId") String ticketId,Model model) {
		MovieTicket movieTicket = movieTicketService.findById(ticketId);
		movieTicketService.delete(movieTicket);
		List<MovieTicket> tickets = movieTicketService.findTicketToShow();
		model.addAttribute("tickets",tickets);
		return "showTicket";
	}


}
