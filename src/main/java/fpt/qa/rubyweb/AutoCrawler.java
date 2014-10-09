package fpt.qa.rubyweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fpt.ruby.crawler.CrawlPhimChieuRap;
import com.fpt.ruby.crawler.CrawlerMyTV;
import com.fpt.ruby.crawler.moveek.MoveekCrawler;
import com.fpt.ruby.service.CinemaService;
import com.fpt.ruby.service.LogService;
import com.fpt.ruby.service.TVProgramService;
import com.fpt.ruby.service.mongo.MovieTicketService;

public class AutoCrawler {

	private MovieTicketService movieTicketService;
	private TVProgramService tvProgramService;
	private LogService logService;
	private CinemaService cinemaService;
	
	private void init() {
		movieTicketService = new MovieTicketService();
		tvProgramService = new TVProgramService();
		logService = new LogService();
		cinemaService = new CinemaService();
	}
	
	public String crawlPhimChieuRap() {
		CrawlPhimChieuRap crawlPhimChieuRap = new CrawlPhimChieuRap();
		try {
			movieTicketService.cleanOldData();
			crawlPhimChieuRap.crawlHaNoi();

		} catch (Exception ex) {
			System.out.println("Error");
			System.out.println(ex.getMessage());
			return "failed";
		}
		return "success";
	}

	public String crawlMyTV() {
		CrawlerMyTV crawlerMyTV = new CrawlerMyTV();
		try {
			tvProgramService.cleanOldData();
			crawlerMyTV.crawlMyTV(tvProgramService);
		} catch (Exception ex) {
			System.out.println("error");
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			return "failed";
		}
		return "success";
	}

	public String crawlMoveek() {
		try {
			movieTicketService.cleanOldData();
			MoveekCrawler.doCrawl(movieTicketService);
		} catch (Exception ex) {
			System.out.println("Error");
			System.out.println(ex.getMessage());
			return "failed";
		}
		return "success";
	}

	public static void main(String[] args) {
		
		AutoCrawler x = new AutoCrawler();
		x.init();
		
		System.out.println("crawl phim chieu rap");
		x.crawlPhimChieuRap();
		
		System.out.println("crawl moveek");
		x.crawlMoveek();
		
		System.out.println("crawl myTV");
		x.crawlMyTV();
		
		System.out.println("Done ALL!!");
	}

}
