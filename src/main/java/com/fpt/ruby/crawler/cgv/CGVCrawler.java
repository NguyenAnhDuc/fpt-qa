package com.fpt.ruby.crawler.cgv;

import java.util.Date;
import java.util.List;

import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.service.mongo.MovieTicketService;

import jmdn.struct.pair.Pair;

public class CGVCrawler {

	public static final String REQUESTIME_TODAY = "Today";
	public static final String REQUESTIME_NEXT7DAYS = "Next7Days";
	public static final String REQUESTIME_ALL = "All";
	
	static String[] cin_ids = {"1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009", 
								"1010", "1011", "1012", "1013", "1014", "1015", "1016"};
	
	static String[] cin_names = {"CGV Vincom City Towers", "CGV Thùy Dương Plaza", "CGV Biên Hòa","CGV Hùng Vương Plaza", 
		"CGV Vĩnh Trung Plaza", "CGV CT Plaza", "CGV Parkson Paragon", "CGV MIPEC Tower", "CGV Crescent Mall", 
		"CGV Pandora City", "CGV Marine Plaza", "CGV Celadon Tân Phú", "CGV Sense City",
		"CGV Lam Sơn Square", "CGV Kim Cúc Plaza", "CGV Bình Dương Square"};
	
	static String [] cities = {"Hà Nội", "Hải Phòng", "Biên Hòa", "Hồ Chí Minh", "Đà Nẵng", "Hồ Chí Minh",
		"Hồ Chí Minh", "Hà Nội", "Hồ Chí Minh", "Hồ Chí Minh", "Hạ Long", 
		"Hồ Chí Minh", "Cần Thơ", "Vũng Tàu", "Quy Nhơn", "Bình Dương"};

	
	public static void doCrawl(MovieTicketService mts){
		// Get all movie names
		for (int i = 0; i < cin_ids.length; i++){
			String cinId = cin_ids[i];
			String cinName = cin_names[i];
			String city = cities[i];
			
			List<Pair<String, String>> movies = CGVCrawlerUtils.GetMoviesByCinemas(cinId, 1);
			for (Pair<String, String> movie : movies){
				List<Date> slots = CGVCrawlerUtils.getSessionTime(cinId, movie, REQUESTIME_ALL, 1);
				
				String movTitle = movie.first;// the english movie title
				String type = "2D";// the movie type
				
				if (movTitle.startsWith("3D")){
					type = "3D";
				}
				
				for (Date slot : slots){
					MovieTicket newTicket = new MovieTicket();
					newTicket.setCinema(cinName);
					newTicket.setCity(city);
					newTicket.setMovie(standardizeMovName(movTitle));
					newTicket.setDate(slot);
					newTicket.setType(type);
					if (!mts.existedInDb(newTicket)){
						mts.save(newTicket);
					}
				}
			}
		}
		// end get all movie names
		
	}
	
	private static String standardizeMovName(String movName){
		String res = movName.replaceAll("\\(.*?\\) ?", "").replaceAll("\\+", " ");
		
		if (movName.startsWith("3D")){
			res = res.substring(2);
		}
		
		return res.replaceAll("\\s+", " ").trim();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "3D+(Dub)+The+Boxtrolls";
//		str = str.replaceAll("\\(.*?\\) ?", "");
		System.out.println(standardizeMovName(str));
	}

}
