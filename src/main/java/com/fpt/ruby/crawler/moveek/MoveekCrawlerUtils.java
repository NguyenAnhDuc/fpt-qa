package com.fpt.ruby.crawler.moveek;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jmdn.struct.pair.Pair;

import com.fpt.ruby.crawler.CrawlerUtils;

public class MoveekCrawlerUtils {
	public static List<Pair<String, String>> GetMoviesByCinemas(String url, String urlParameters){
		List<Pair<String, String>> res = new ArrayList<Pair<String, String>>();
		try {
			String response = CrawlerUtils.sendGet(url, urlParameters);
			
			return parseMovieList(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private static List<Pair<String, String>> parseMovieList(String response){
		List<Pair<String, String>> res = new ArrayList<Pair<String, String>>();
		
		int idx = response.indexOf("<a href=\"/lich-chieu-rap");
		while (idx > 0){
			// get the-november-man from <a href="/lich-chieu-rap/cgv-marine-plaza/the-november-man/">
			int bquote = response.indexOf("\"", idx) + 1;
			int equote = response.indexOf("\"", bquote) - 1;
			String link = response.substring(bquote, equote);
			int bLink = link.lastIndexOf("/");
			String val = link.substring(bLink + 1);
			
			idx = response.indexOf("<span class=\"title-main\">", equote);
			int bLblIdx = response.indexOf(">", idx) + 1;
			int eLblIdx = response.indexOf("</", bLblIdx);
			String enTitle = response.substring(bLblIdx, eLblIdx);
			
			res.add(new Pair<String, String>(val, enTitle));
			
			idx = response.indexOf("<a href=\"/lich-chieu-rap", eLblIdx);
		}
		
		return res;
	}
	
	public static List<Pair<String, Date>> getSessionTime(String url, String mov){
		List<Pair<String, Date>> res = new ArrayList<Pair<String, Date>>();
	
		try {
			String response = CrawlerUtils.sendGet(url, "p[]=" + mov);
			return parseSessionTime(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private static List<Pair<String, Date>> parseSessionTime(String response){
		List<Pair<String, Date>> res = new ArrayList<Pair<String, Date>>();
		
		int idx = response.indexOf("<a href=\"#\" class=\"btn btn-primary btn-xs\">");
		
		while(idx > 0){
			int bType = response.indexOf(">", idx) + 1;
			int eType = response.indexOf("</", bType);
			String type = response.substring(bType, eType);
			
			int idx1 = response.indexOf("<a onClick=\"ga('send'", eType);
			eType = idx1;
			int endIdx = response.indexOf("<a href=\"#\" class=\"btn btn-primary btn-xs\">", eType);
			while(idx1 > 0 && (endIdx < 0) || idx1 < endIdx){
				int bTime = response.indexOf("'time' : '", idx1) + 10;
				int eTime = response.indexOf("'", bTime);
				String time = response.substring(bTime, eTime);
				
				if (!Character.isDigit(time.charAt(0))){
					break;
				}
				idx1 = response.indexOf("<a onClick=\"ga('send'", eTime + 20);
				res.add(new Pair<String, Date>(type, parseDate(time)));
			}
			
			idx = response.indexOf("<a href=\"#\" class=\"btn btn-primary btn-xs\">", eType);
		}
		
		return res;
	}

	private static Date parseDate(String dateStr){
		if (dateStr == null || dateStr.length() < 14)
			return null;
		
		int hour = Integer.parseInt(dateStr.substring(0, 2));
		int mins = Integer.parseInt(dateStr.substring(3, 5));
		String date = dateStr.substring(6);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date tmp = format.parse(date);
			return new Date(tmp.getTime() + 60 * 60 * 1000 * hour + 60 * 1000 * mins);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String url = "http://moveek.com/ajax/showtimesCinema/ha-noi?t=megastar-mipec-tower";
		String mov = "the-equalizer";
		List<Pair<String, Date>> slots = getSessionTime(url, mov);
		for (Pair<String, Date> slot : slots){
			System.out.println(slot.first + " | " + slot.second);
		}
	}

}
