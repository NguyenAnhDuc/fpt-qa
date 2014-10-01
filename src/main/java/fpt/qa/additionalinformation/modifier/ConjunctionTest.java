
package fpt.qa.additionalinformation.modifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class ConjunctionTest{

	public static void main( String[] args ) {
		ConjunctionWithType conjunction = new ConjunctionWithType( "/home/dungx/git/rubyweb/src/main/resources" );

		try{
			BufferedReader reader = new BufferedReader( new FileReader( new File(
					"/home/dungx/git/rubyweb/src/main/resources/data/AIML_tvd_questions.txt" ) ) );
			
			String line = null;
			while( ( line = reader.readLine() ) != null ){
				System.out.println( line + conjunction.getOriginRelevantConjunctionsWithType( line ) );
				new Scanner( System.in ).nextLine();
			}
			reader.close();
		}catch ( Exception e ){
			e.printStackTrace();
		}
	}

}
