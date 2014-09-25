
package fpt.qa.additionalinformation.test;

import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.additionalinformation.modifier.AbsoluteTime.TimeResult;

public class AbsoluteTimeTest{

	public static void main( String[] args ) {
		AbsoluteTime absoluteTime = new AbsoluteTime( "src/main/resources/vnsutime" );

		TimeResult timeResult = absoluteTime.getAbsoluteTime( "ngày thành lập đảng cộng sản việt nam có phim gì?" );

		System.out.println( timeResult.getBeginTime() );
		System.out.println( timeResult.getEndTime() );
	}
}
