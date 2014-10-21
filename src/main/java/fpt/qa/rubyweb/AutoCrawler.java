package fpt.qa.rubyweb;

import com.fpt.ruby.crawler.CrawlPhimChieuRap;
import com.fpt.ruby.crawler.CrawlerMyTV;
import com.fpt.ruby.crawler.CrawlerVTVCab;
import com.fpt.ruby.crawler.moveek.MoveekCrawler;
import com.fpt.ruby.service.LogService;
import com.fpt.ruby.service.TVProgramService;
import com.fpt.ruby.service.mongo.MovieTicketService;

public class AutoCrawler {

	private MovieTicketService movieTicketService;
	private TVProgramService tvProgramService;
	private LogService logService;
//	private CinemaService cinemaService;
	
	private void init() {
		movieTicketService = new MovieTicketService();
		tvProgramService = new TVProgramService();
		logService = new LogService();
//		cinemaService = new CinemaService();
	}
	
	private void cleanData() {
		try {
			movieTicketService.cleanOldData();
		} catch (Exception e) {
			System.out.println("error clean movie ticket.");
		}
		
		try {
			tvProgramService.cleanOldData();
		} catch (Exception e) {
			System.out.println("error clean movie ticket.");
		}
		
		try {
			movieTicketService.clearDataOnSpecificDay(0);
			tvProgramService.clearDataOnSpecificDay(0);
		} catch (Exception e) {
			System.out.println("Error clear today data. " + e.getMessage());
		}
	}
	
	private void doCrawl() {
		CrawlerVTVCab crawvtvcab = new CrawlerVTVCab();
		CrawlerMyTV crawlmytv = new CrawlerMyTV();
		CrawlPhimChieuRap crawlPhimChieuRap = new CrawlPhimChieuRap();
		
		try {
			crawlmytv.crawlMyTV(tvProgramService);
		} catch (Exception ex) {
			System.out.println("Error crawling my tv!! Message = " + ex.getMessage());
		}
		
		try {
			crawvtvcab.doCrawl(tvProgramService);
		} catch (Exception ex) {
			System.out.println("Eror crawling vtvcab!! Message = " + ex.getMessage());
		}
		
		try {
			MoveekCrawler.doCrawl(movieTicketService);
		} catch (Exception ex) {
			System.out.println("Eror crawling moveek!! Message = " + ex.getMessage());
		}
		
		try {
			crawlPhimChieuRap.crawlHaNoi();
		} catch (Exception ex) {
			System.out.println("Error crawling Phimchieurap!! Message = " + ex.getMessage());
		}
	}

	public static void main(String[] args) {
		AutoCrawler x = new AutoCrawler();
		x.init();
		x.cleanData();
		x.doCrawl();
	}

}
