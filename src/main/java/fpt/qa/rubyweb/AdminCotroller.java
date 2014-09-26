package fpt.qa.rubyweb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.util.logging.resources.logging;

import com.fpt.ruby.crawler.CrawlPhimChieuRap;
import com.fpt.ruby.crawler.CrawlerMyTV;
import com.fpt.ruby.model.Log;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.TVProgram;
import com.fpt.ruby.service.LogService;
import com.fpt.ruby.service.TVProgramService;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.mongodb.BasicDBObject;

@Controller
@RequestMapping("")
public class AdminCotroller {
	@Autowired
	private MovieTicketService movieTicketService;
	@Autowired
	private TVProgramService tvProgramService;
	@Autowired
	private LogService logService;
	
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
	
	@RequestMapping(value="/crawl-mytv", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BasicDBObject crawlMyTV(){
		CrawlerMyTV crawlerMyTV = new CrawlerMyTV();
		try{
			crawlerMyTV.crawlMyTV();
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
	
	@RequestMapping(value="admin", method = RequestMethod.GET)
	public String admin(Model model){
		return "admin";
	}
	
	@RequestMapping(value="/admin-crawl-manual", method = RequestMethod.GET)
	public String crawlManual(Model model){
		return "crawl";
	}
	
	@RequestMapping(value="admin-crawl-phim-chieu-rap", method = RequestMethod.GET)
	public String crawlPhimChieuRap(Model model){
		return "crawlPhimChieuRap";
	}
	
	@RequestMapping(value="admin-crawl-mytv", method = RequestMethod.GET)
	public String crawlMyTV(Model model){
		return "crawl-mytv";
	}
	
	@RequestMapping(value="admin-show-tickets", method = RequestMethod.GET)
	public String showTickets(Model model){
		List<MovieTicket> tickets = movieTicketService.findTicketToShow();
		model.addAttribute("tickets",tickets);
		HashSet<String> movies = new HashSet<String>();
		HashSet<String> cinemas = new HashSet<String>();
		for (MovieTicket movieTicket : tickets){
			movies.add(movieTicket.getMovie());
			cinemas.add(movieTicket.getCinema());
		}
		model.addAttribute("numMovie",movies.size());
		model.addAttribute("numCinema",cinemas.size());
		return "showTicket";
	}
	
	@RequestMapping(value="admin-show-tvprograms", method = RequestMethod.GET)
	public String showTVPrograms(Model model){
		List<TVProgram> tvPrograms = tvProgramService.findProgramToShow();
		model.addAttribute("tvPrograms",tvPrograms);
		return "showTV";
	}
	
	@RequestMapping(value="admin-show-logs", method = RequestMethod.GET)
	public String showLogs(Model model, @RequestParam("num") String numString){
		List<Log> logs = logService.findLogToShow();
		List<Log> results = new ArrayList<Log>();
		int numLog = 0;
		try {
			numLog = Integer.parseInt(numString);
		}
		catch (Exception ex){
			numLog=0;
		}
		if (numLog==0){
			model.addAttribute("logs",logs);
			return "showLog";
		}
		for (int  i=0;i<numLog;i++){
			results.add(logs.get(i));
		}
		return "showLog";
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
