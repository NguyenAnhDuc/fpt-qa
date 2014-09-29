package fpt.qa.vnTime.vntime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRange {

	private String expression;
	private Date fDate;
	private Date sDate;

	public TimeRange(String expression, String fDate, String sDate)
			throws ParseException {
		super();
		this.expression = expression;
		this.fDate = parsefDate(fDate);
		this.sDate = parsesDate(sDate);
	}

	public TimeRange() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	private Date parsefDate(String dateString) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date utilDate = null;
		try {
			utilDate = format.parse(dateString);
		} catch (ParseException e) {
			utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			utilDate = new Date(utilDate.getTime());
			//System.out.println("!!!!!"+utilDate);
			try {
				utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
				utilDate = new Date(utilDate.getTime()); 
			} catch (ParseException ex) {
				utilDate = new SimpleDateFormat("yyyy-MM").parse(dateString);
				utilDate.setDate(30);
				dateString = new SimpleDateFormat("yyyy-MM-dd").format(utilDate);
				utilDate = parsefDate(dateString);
				//System.out.println("~~~~"+dateString);
				//utilDate = new Date(utilDate.getTime()+ 30 * (60 * 60 * 1000 * 23 + 60 * 1000 * 59 + 59 * 1000)); 
			}
		}
		return utilDate;
	}

	@SuppressWarnings("deprecation")
	private Date parsesDate ( String dateString) throws ParseException {
		//System.out.println(dateString);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date utilDate = null;
		try {
			utilDate = format.parse(dateString);
		} catch (ParseException e) {
			try {
				utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
				utilDate = new Date(utilDate.getTime()+ 60 * 60 * 1000 * 23 + 60 * 1000 * 59 + 59 * 1000); 
			} catch (ParseException ex) {
				utilDate = new SimpleDateFormat("yyyy-MM").parse(dateString);
				utilDate.setDate(30);
				dateString = new SimpleDateFormat("yyyy-MM-dd").format(utilDate);
				utilDate = parsesDate(dateString);
				System.out.println(dateString);
				//utilDate = new Date(utilDate.getTime()+ 30 * (60 * 60 * 1000 * 23 + 60 * 1000 * 59 + 59 * 1000)); 
			}
		}
		return utilDate;
	}
	
	public static void main(String[] args) throws ParseException {
		String date = "2014-9-21";
		System.out.println("Date = "+ new TimeRange().parsefDate(date));
		System.out.println("Date = "+ new TimeRange().parsesDate(date));
	}
	
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Date getfDate() {
		return fDate;
	}

	public void setfDate(String fDate) throws ParseException {
		this.fDate = parsefDate(fDate);
	}

	public Date getsDate() {
		return sDate;
	}
	
	public void setsDate(String sDate) throws ParseException {
		this.sDate = parsesDate(sDate);
	}

	@Override
	public String toString() {
		return "Chuỗi thời gian :" + expression + "\n Cận trên : " + this.fDate
				+ "\n Cận dưới :" + sDate;
	}
}
