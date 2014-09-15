package com.fpt.ruby.crawler.cgv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fpt.ruby.crawler.CrawlerUtils;

import jmdn.struct.pair.Pair;


public class CGVCrawlerUtils {
	
	public static List<Pair<String, String>> GetMoviesByCinemas(String cinId, int lang){
		List<Pair<String, String>> res = new ArrayList<Pair<String, String>>();
		String url = "http://www.cgv.vn/msSessiontimeHandles.aspx";
		String urlParameters = "RequestType=GetMoviesByCinemas&CinemaIDs=" + cinId + "&visLang=" + lang;
		try {
			String response = CrawlerUtils.sendPost(url, urlParameters);
			
			return parseMovieList(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private static List<Pair<String, String>> parseMovieList(String response){
		List<Pair<String, String>> res = new ArrayList<Pair<String, String>>();
		int idx = response.indexOf("<input type=\"checkbox\"");
		while (idx > 0){
			int bvalIdx = response.indexOf("value=\"", idx) + 7;
			int evalIdx = response.indexOf("\"", bvalIdx);
			String val = response.substring(bvalIdx, evalIdx);
			
			int bLblIdx = response.indexOf(">", response.indexOf("<label for=\"", evalIdx) + 3);
			int eLblIdx = response.indexOf("</", bLblIdx);
			String label = response.substring(bLblIdx, eLblIdx);
			res.add(new Pair<String, String>(val, label));
			
			idx = response.indexOf("<input type=\"checkbox\"", eLblIdx);
		}
		
		return res;
	}
	
	public static List<Date> getSessionTime(String cinId, Pair<String, String> movInfo, String requestTime,
			int lang){
		List<Date> res = new ArrayList<Date>();
		String url = "http://www.cgv.vn/msSessiontimeHandles.aspx";
		String urlParameters = "RequestType=GetSessionTime&CinemaIDs="+ cinId + 
				"&Movies=" + movInfo.first + "&MovieAdvs=" + movInfo.second + "&RequestTime=" + requestTime + 
				 "&visLang=" + lang;
		try {
			String response = CrawlerUtils.sendPost(url, urlParameters);
			return parseSessionTime(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private static List<Date> parseSessionTime(String response){
		List<Date> res = new ArrayList<Date>();
		
		int idx = response.indexOf("<div class=\"purple_box\">");
		while(idx > 0){
			int sDayIdx = response.indexOf(">", idx + 10) + 1;
			String day = response.substring(sDayIdx, response.indexOf("<", sDayIdx));
			
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			Date utilDate = null;
			try {
				utilDate = format.parse(day);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			idx = response.indexOf("/visLtyTicketsLogin.aspx", idx + 10);
			
			int endDayIdx = response.indexOf("</ul>", idx);
			while (idx > 0 && idx < endDayIdx){
				int sTime = response.indexOf(">", idx) + 1;
				int eTime = response.indexOf("<", sTime);
				String time = response.substring(sTime, eTime).trim();
				int hour = Integer.parseInt(time.substring(0, 2));
				int mins = Integer.parseInt(time.substring(3, 5));
				
				long addTime = 60 * 60 * 1000 * hour + 60 * 1000 * mins;
				Date sesTime = new Date(utilDate.getTime() + addTime);
				res.add(sesTime);
				idx = response.indexOf("/visLtyTicketsLogin.aspx", eTime + 10);
				
			}
			
			idx = response.indexOf("<div class=\"purple_box\">", endDayIdx);
		}
		
		return res;
	}

}
