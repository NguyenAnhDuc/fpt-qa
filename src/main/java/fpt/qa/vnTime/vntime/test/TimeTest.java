package fpt.qa.vnTime.vntime.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fpt.qa.vnTime.utils.IConstants;
import fpt.qa.vnTime.vntime.TimeRange;

public class TimeTest {

	public TimeTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws ParseException{
		List<String> list = fileReader("question.txt"); 
		for(String line : list) {
			List<TimeRange> timeRanges = IConstants.VN_TIME_PARSER.parser3(line, IConstants.CURRENT_DATE);
			for (TimeRange timeRange : timeRanges) {
				System.out.println(timeRange.toString());
			}
		}
	}
	
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
