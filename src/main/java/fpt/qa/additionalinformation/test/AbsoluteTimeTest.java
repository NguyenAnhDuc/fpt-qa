
package fpt.qa.additionalinformation.test;

import edu.stanford.nlp.time.SUTime.Time;
import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.additionalinformation.modifier.AbsoluteTime.TimeResult;

public class AbsoluteTimeTest{

	public static void main( String[] args ) {
		AbsoluteTime absoluteTime = new AbsoluteTime( "src/main/resources/vnsutime" );

		TimeResult timeResult = absoluteTime.getAbsoluteTime( "8 giờ tối trên kênh discovery chiếu chương trình gì" );
		//System.out.println( timeResult.getBeginTime() );
		//System.out.println( timeResult.getEndTime() );
	}
}
