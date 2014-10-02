
package fpt.qa.additionalinformation.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.additionalinformation.modifier.AbsoluteTime.TimeResult;

public class AbsoluteTimeTest{
	public static void testFile(String fileIn, String fileOut, AbsoluteTime absoluteTime){
		try{
			BufferedReader reader = new BufferedReader( new FileReader( fileIn ) );
			BufferedWriter writer = new BufferedWriter( new FileWriter( fileOut ) );
			
			String line;
			while ((line = reader.readLine()) != null){
				TimeResult timeResult = absoluteTime.getAbsoluteTime( line.replaceAll("(\\d+)(h)", "$1 giờ ").replaceAll( "\\s+", " " ) );
				System.out.println("PROCESSING: " + line);
				writer.write( line + "\n\t" + timeResult.getBeginTime() + "\n\t" + timeResult.getEndTime() + "\n\n" );
			}
			
			writer.close();
			reader.close();
		}catch ( IOException e ){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main( String[] args ) {
		AbsoluteTime absoluteTime = new AbsoluteTime( "src/main/resources/vnsutime" );
//		
//		testFile( "/home/ngan/Work/AHongPhuong/Intent_detection/data/tv/AIML_tvd_questions.txt",
//				"/home/ngan/Work/AHongPhuong/Intent_detection/data/tv/AIML_tvd_questions.out", absoluteTime );
		
		
		TimeResult timeResult = absoluteTime.getAbsoluteTime( "sáng mai chiếu chương trình gì" );
		System.out.println( timeResult.getBeginTime() );
		System.out.println( timeResult.getEndTime() );
		
//		System.out.println();
//		System.out.println();
//		timeResult = absoluteTime.getAbsoluteTime( " 9 giờ 30 tối mai có chương trình gì" );
//		System.out.println( timeResult.getBeginTime() );
//		System.out.println( timeResult.getEndTime() );
//		
//		System.out.println();
//		System.out.println();
//		timeResult = absoluteTime.getAbsoluteTime( "mai có chương trình gì" );
//		System.out.println( timeResult.getBeginTime() );
//		System.out.println( timeResult.getEndTime() );
//		
//		
//		System.out.println();
//		System.out.println();
//		timeResult = absoluteTime.getAbsoluteTime( "9 giờ ngày mai có chương trình gì" );
//		System.out.println( timeResult.getBeginTime() );
//		System.out.println( timeResult.getEndTime() );
	}
}
