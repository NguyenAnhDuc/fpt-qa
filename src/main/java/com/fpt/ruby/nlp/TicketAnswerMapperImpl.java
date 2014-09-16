package com.fpt.ruby.nlp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.fpt.ruby.model.MovieTicket;

public class TicketAnswerMapperImpl implements TicketAnswerMapper {
	public String getTypeTicketAnswer(List<MovieTicket> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		String type = "";
		for (MovieTicket tick : ans){
			type += tick.getType() + " + ";
		}
		
		MovieTicket mov = ans.get(0);
		String res = "Phim " + mov.getMovie() + " có phiên bản " + type.substring(0, type.length() - 3);
		return res;
	}
	
	public String getTitleTicketAnswer(List<MovieTicket> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		HashSet<String> movies = new HashSet<String>();
		for (MovieTicket movieTicket : ans){
			movies.add(movieTicket.getMovie().toUpperCase());
		}
		List<String> movieNames = new ArrayList<String>();
		for (String movie : movies){
			movieNames.add(movie);
		}
		String res = "Phim ";
		for (int i = 0; i < movieNames.size(); i++) {
			if (i > 0 && i == movieNames.size() - 1) {
				res += "và " + movieNames.get(i) + " đang được chiếu";
				break;
			} else if (i == movieNames.size() - 1) {
				res += movieNames.get(i) + " đang được chiếu";
				break;
			}
			res += movieNames.get(i);
			if (ans.size() > 1) {
				res += ", ";
			} else {
				res += " ";
			}
		}

		return res;
	}
	
	public String getCinemaTicketAnswer(List<MovieTicket> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		HashSet<String> cinemas = new HashSet<String>();
		for (MovieTicket movieTicket : ans){
			cinemas.add(movieTicket.getCinema());
		}
		String res = "Rạp ";
		for (String cinema : cinemas){
			res += cinema + ", ";
		}
		return res.substring(0, res.length() - 2);
	}
	
	public String getDateTicketAnswer(List<MovieTicket> ans){
		System.out.println("Date answer");
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		HashSet<Date> dates = new HashSet<Date>();
		for (MovieTicket movieTicket : ans){
			dates.add(movieTicket.getDate());
		}
		String res = "Giờ chiếu: ";
		for (Date date : dates){
			res += date.toLocaleString() + ", ";
		}
		return res.substring(0, res.length() - 2);
	}
}
