package fpt.qa.vnTime.vntime.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import fpt.qa.vnTime.utils.RangeParser;
import fpt.qa.vnTime.vntime.TimeRange;
import fpt.qa.vnTime.vntime.VnTimeParser;

public class VnSutimeTest {
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

		VnTimeParser vnSutime = new VnTimeParser("src/main/resources/vnsutime");
		// get current date time with Date()
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String curDate = dateFormat.format(date);
		dateFormat = new SimpleDateFormat("HH:mm:ss");
		curDate += " " + dateFormat.format(date);
		//System.out.println("Ngày giờ hệ thống : " + curDate);
		List<String> questionList = fileReader( "questions_tv" );
		for(String question : questionList) {
			//System.out.println("=========================================================");
			System.out.print(question+"\t");
			//System.out.println(vnSutime.parser(question, curDate));
			List<TimeRange> rangeList = vnSutime.parser3(question, curDate);
			if(rangeList.isEmpty()) {
				System.out.println("null,null");
			}
			for(TimeRange str : rangeList) {
				System.out.println(str.getfDate()+","+str.getsDate());
			}
		}
	}
//	public static void main(String[] args) {
//		String question = "sắp tới chiếu phim gì ? , sắp có phim gì ? rạp Lotte có phim gì?";
////		List<TimeRange> timeRanges = IConstants.VN_TIME_PARSER.parser3(question, IConstants.CURRENT_DATE);
////		int i = 0 ;
////		for(TimeRange timeRange : timeRanges) {
////			System.out.println(i++ + " | "+ timeRange.toString());
////		}
//		System.out.println(new VnTimeParser().parser(question, IConstants.CURRENT_DATE));
//	}

	public static List<String> fileReader(String path) {
		List<String> list = new ArrayList<String>();
		try {
			FileInputStream is = new FileInputStream(path);
			Scanner input = new Scanner(is, "UTF-8");
			while (input.hasNextLine()) {
				String line = input.nextLine();
				if(line.equalsIgnoreCase(""));
					list.add(line.trim());
			}
			is.close();
			input.close();
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
			//e.printStackTrace();
		}
		return list ;
	}
}
