package fpt.qa.rubyweb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import com.fpt.ruby.crawler.CrawlPhimChieuRap;
import com.fpt.ruby.crawler.CrawlerMyTV;
import com.fpt.ruby.crawler.moveek.MoveekCrawler;
import com.fpt.ruby.model.Cinema;
import com.fpt.ruby.model.Log;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.model.TVProgram;
import com.fpt.ruby.model.chart.DataChart;
import com.fpt.ruby.service.CinemaService;
import com.fpt.ruby.service.LogService;
import com.fpt.ruby.service.TVProgramService;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.google.common.collect.Lists;
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
	@Autowired
	private CinemaService cinemaService;
	
	@RequestMapping(value="/crawlPhimChieuRap", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BasicDBObject crawlPhimChieuRap(){
		CrawlPhimChieuRap crawlPhimChieuRap = new CrawlPhimChieuRap();
		try{
			movieTicketService.cleanOldData();
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
			tvProgramService.cleanOldData();
			crawlerMyTV.crawlMyTV(tvProgramService);
		}
		catch (Exception ex){
			System.out.println("Done");
			return new BasicDBObject().append("status", "failed");
		}
		return new BasicDBObject().append("status", "success");
	}
	
	@RequestMapping(value="/crawl-moveek", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BasicDBObject crawlMoveek(){
		try{
			movieTicketService.cleanOldData();
			MoveekCrawler.doCrawl( movieTicketService );
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
		}
		model.addAttribute("status","success");
		List<MovieTicket> tickets = movieTicketService.findTicketToShow(0);
		model.addAttribute("tickets",tickets);
		return "showTicket";
	}
	
	@RequestMapping(value="/addCinema", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
	public String addCinema(@RequestParam("cin_name") String cinName, @RequestParam("cin_address") String cin_address,
											@RequestParam("mobile") String mobile,Model model){
		try{
			Cinema cinema = new Cinema();
			cinema.setName( cinName.trim() );
			cinema.setAddress( cin_address.trim() );
			cinema.setMobile( mobile.trim() );
			cinemaService.save( cinema );
		}
		catch (Exception ex){
			ex.printStackTrace();
			model.addAttribute("status","failed");
			return "";
		}
		model.addAttribute("status","success");
		List<Cinema> cinemas = cinemaService.findAll();
		model.addAttribute("cinemas",cinemas);
		return "showCinema";
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
	
	@RequestMapping(value="admin-crawl-moveek", method = RequestMethod.GET)
	public String crawlMoveek(Model model){
		return "crawlMoveek";
	}
	
	@RequestMapping(value="admin-crawl-mytv", method = RequestMethod.GET)
	public String crawlMyTV(Model model){
		return "crawl-mytv";
	}
	
	@RequestMapping(value="admin-add-cinema", method = RequestMethod.GET)
	public String addCinema(Model model){
		return "addCinema";
	}
	
	@RequestMapping(value="admin-show-tickets", method = RequestMethod.GET)
	public String showTickets(@RequestParam("day") String  day, Model model){
		int numday = 0 ;
		try{
			numday = Integer.parseInt(day);
		}
		catch (Exception ex){
		}

		List<MovieTicket> tickets = movieTicketService.findTicketToShow(numday);
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
	public String showTVPrograms(@RequestParam("day") String day, Model model){
		int numday = 0 ;
		try{
			numday = Integer.parseInt(day);
		}
		catch (Exception ex){
		}
		List<TVProgram> tvPrograms = tvProgramService.findProgramToShow(numday);
		model.addAttribute("tvPrograms",tvPrograms);
		return "showTV";
	}
	
	@RequestMapping(value="admin-show-cinemas", method = RequestMethod.GET)
	public String showCinemas(Model model){
		List<Cinema> cinemas = cinemaService.findAll();
		model.addAttribute("cinemas",cinemas);
		return "showCinema";
	}
	
	@RequestMapping(value="admin-show-logs", method = RequestMethod.GET)
	public String showLogs(Model model, @RequestParam("num") String numString){
		System.out.println("Admin Show Logs");
		List<Log> logs = Lists.reverse(logService.findLogToShow());
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
		
		model.addAttribute("logs",logs.subList(0, numLog));
		return "showLog";
	}

	@RequestMapping(value = "/deleteTicket", method = RequestMethod.GET)
	public String deleteBot(@RequestParam("ticketId") String ticketId,Model model) {
		MovieTicket movieTicket = movieTicketService.findById(ticketId);
		movieTicketService.delete(movieTicket);
		List<MovieTicket> tickets = movieTicketService.findTicketToShow(0);
		model.addAttribute("tickets",tickets);
		return "showTicket";
	}
	@RequestMapping(value = "/testChartData", method = RequestMethod.POST)
	public List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("Jan");data.add("Feb");
		return data;
	}
	
	@RequestMapping(value = "/testChart", method = RequestMethod.GET)
	public String testChart(Model model) {
		int ONE_DAY = 24 * 3600 * 1000;
		//cal.set(Calendar.DAY_OF_MONTH,0);cal.set(Calendar.HOUR, 0);cal.set(Calendar.MINUTE, 0);cal.set(Calendar.SECOND, 0);
		Date today = new Date();
		today.setHours(0);today.setMinutes(0);today.setSeconds(0);
		Date firstdayOfMonth = new Date(today.getTime() - (today.getDate() -1) * ONE_DAY);
		List<String> months = new ArrayList<String>();
		List<DataChart> series = new ArrayList<DataChart>();
		DataChart dataChart = new DataChart();
		dataChart.name = "All";
		dataChart.data = new ArrayList<Integer>();
		List<Log> logs = logService.findLogGtTime(firstdayOfMonth);
		for (int i = firstdayOfMonth.getDate(); i <= today.getDate(); i++ ){
			months.add(""+i);
			Date before = new Date(firstdayOfMonth.getTime() + i*ONE_DAY);
			Date after = new Date(firstdayOfMonth.getTime() + (i-1)*ONE_DAY);
			Integer numData = (int) logs.stream().filter(l -> ( l.getDate().after(after) &&  l.getDate().before(before))).count();
			dataChart.data.add(numData);
		}
		series.add(dataChart);
		
		
		System.out.println();
		ObjectMapper mapper = new ObjectMapper();
		String json = "", jsonS = "";
		
		
		try {
			json = mapper.writeValueAsString(months);
			jsonS = mapper.writeValueAsString(series);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("data", json);
		model.addAttribute("jsonS", jsonS);
		return "testChart";
	}
}
