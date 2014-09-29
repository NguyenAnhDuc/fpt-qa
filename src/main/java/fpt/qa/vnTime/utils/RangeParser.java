package fpt.qa.vnTime.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fpt.qa.vnTime.vntime.TimeRange;

public class RangeParser {

	@SuppressWarnings("unused")
	//private static final String DTIME_REGEX = "\\d{4}-\\d{1,2}-\\d{1,2}(T\\d{1,2}:\\d{2})*";

	public RangeParser() {
		// /
	}

	public static TimeRange parser(String timeString) throws ParseException {
		TimeRange timeRange = new TimeRange();
		try {
			List<String> list = new ArrayList<String>();
			Pattern pattern = Pattern.compile(IConstants.DTIME_REGEX);
			Matcher matcher = pattern.matcher(timeString);
			while(matcher.find()) {
				list.add(matcher.group().replace("T", " "));
			}
			if(list.size()>=2) {
				timeRange.setfDate(list.get(0));
				timeRange.setsDate(list.get(1));
			} else if(list.size()==1) {
				timeRange.setfDate(list.get(0));
				timeRange.setsDate(list.get(0));
			} else {
				
			}
			
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return timeRange; 
	}

	
}
