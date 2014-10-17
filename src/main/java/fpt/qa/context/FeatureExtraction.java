package fpt.qa.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FeatureExtraction{
	
	private static final String dataFile = "/home/dungx/git/rubyweb/src/main/resources/context/Dialogue-Contexts.txt";

	public static void main( String[] args ) {
		try{
			File file = new File( dataFile );
			System.out.println( file.exists() );
			
			BufferedReader reader = new BufferedReader( new FileReader( file ) );
			String line;
			while( ( line = reader.readLine() ) != null ){
				if( line.startsWith( " " ) );
			}
			
			reader.close();
		}catch( Exception e ){
			e.printStackTrace();
		}
	}

	
	
}
