package com.fpt.ruby.nlp;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.fpt.ruby.App;
import com.fpt.ruby.model.MovieFly;

public class MovieAnswerMapperImpl implements MovieAnswerMapper {
	public String getTitleMovieAnswer(List<MovieFly> ans) {
		String res = "";
		res = "Phim ";
		if (ans.size() == 0) {
			return "Xin lỗi, chúng tôi không tìm được kết quả thích hợp";
		}
		for (int i = 0; i < ans.size(); i++) {
			if (i > 0 && i == ans.size() - 1) {
				res += "và " + ans.get(i).getTitle() + " đang được chiếu";
				break;
			} else if (i == ans.size() - 1) {
				res += ans.get(i).getTitle();
				break;
			}
			res += ans.get(i).getTitle();
			if (ans.size() > 1) {
				res += ", ";
			} else {
				res += " ";
			}
		}

		return res;
	}

	public String getGenreMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " thuộc thể loại " + mov.getGenre();
		return res;
	}

	public String getActorMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " có sự góp mặt của diễn viên " + mov.getActor();
		return res;
	}

	public String getDirectorMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Tôi không biết nhưng hẳn phải là 1 đạo diễn nổi tiếng đấy";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " của đạo diễn " + mov.getDirector();
		return res;
	}

	public String getLangMovieAnswer(List<MovieFly> ans){
		MovieFly mov = ans.get(0);
		if (ans.size() == 0){
			return "Tôi không biết nhưng theo tôi " + mov.getTitle() + " là một bộ phim tiếng Anh";
		}
		String res = "Phim " + mov.getTitle() + " sử dụng tiếng " + mov.getLanguage();
		return res;
	}

	public String getCountryMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " là phim " + mov.getCountry();
		return res;
	}

	public String getAwardMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " đã nhận được giải thưởng " + mov.getAwards();
		return res;
	}

	public String getPlotMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = mov.getPlot();
		return res;
	}

	public String getYearMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " được sản xuất năm " + mov.getYear();
		return res;
	}

	public String getRuntimeMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " kéo dài " + mov.getRuntime();
		return res;
	}

	public String getAudienceMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " không phù hợp với trẻ em";
		return res;
	}

	public String getReleaseMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " được công chiếu ngày " + mov.getReleased();
		return res;
	}

	public String getImdbRatingMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " có imdb rating khoảng " + mov.getImdbRating();
		return res;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		App app = new App();
		MovieAnswerMapper map = new MovieAnswerMapperImpl();
		String title = "Guardians of the galaxy";
		List<MovieFly> mov = app.getMovieFlyService().searchOnImdb(title);
		
		System.out.println(map.getActorMovieAnswer(mov));
		System.out.println(map.getAudienceMovieAnswer(mov));
		System.out.println(map.getAwardMovieAnswer(mov));
		System.out.println(map.getCountryMovieAnswer(mov));
		System.out.println(map.getDirectorMovieAnswer(mov));
		System.out.println(map.getGenreMovieAnswer(mov));
		System.out.println(map.getImdbRatingMovieAnswer(mov));
		System.out.println(map.getLangMovieAnswer(mov));
		System.out.println(map.getPlotMovieAnswer(mov));
		System.out.println(map.getReleaseMovieAnswer(mov));
		System.out.println(map.getRuntimeMovieAnswer(mov));
		System.out.println(map.getTitleMovieAnswer(mov));
		System.out.println(map.getYearMovieAnswer(mov));
	}
}
