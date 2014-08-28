package com.fpt.ruby.nlp;

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
		String res = "";
		res = "Phim ";
		for (int i = 0; i < ans.size(); i++) {
			if (i > 0 && i == ans.size() - 1) {
				res += "và " + ans.get(i).getMovie() + " đang được chiếu";
				break;
			} else if (i == ans.size() - 1) {
				res += ans.get(i).getMovie() + " đang được chiếu";
				break;
			}
			res += ans.get(i).getMovie();
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
		String res = "Rạp ";
		for (MovieTicket tick : ans){
			res += tick.getCinema() + ", ";
		}
		return res.substring(0, res.length() - 3);
	}
	
	public String getDateTicketAnswer(List<MovieTicket> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		return "";
	}
}
