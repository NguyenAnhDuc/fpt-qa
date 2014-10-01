package fpt.qa.additionalinformation.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import fpt.qa.mdnlib.diacritic.DiacriticConverter;

public class ConvertDiacriticDirectory{

	public static void main( String[] args ) {
		//convert( "/home/dungx/git/rubyweb/src/main/resources/data-name-mapper", "/home/dungx/git/rubyweb/src/main/resources/non-diacritic/data-name-mapper" );
		convert( "/home/dungx/git/rubyweb/src/main/resources/surroundWords", "/home/dungx/git/rubyweb/src/main/resources/non-diacritic/surroundWords" );
	}
	
	public static void convert( String originPath, String destPath ){
		File originDir = new File( originPath );
		File destDir = new File( destPath );
		
		if( !originDir.isDirectory() ){
			System.err.println( originPath + " is not valid" );
			return;
		}
		
		if( !destDir.exists() || !destDir.isDirectory() ){
			destDir.mkdir();
		}
		
		for( File file : originDir.listFiles() ){
			if( file.isFile() ){
				convertFile( file, new File( destDir, file.getName() ) );
			}else{
				convert( file.getAbsolutePath(), destPath + "/" + file.getName() );
			}
		}
	}
	
	public static void convertFile( File origin, File dest ){
		try{
			BufferedReader reader = new BufferedReader( new FileReader( origin) );
			PrintWriter writer = new PrintWriter( new FileWriter( dest ) );
			
			String line;
			while( ( line = reader.readLine() ) != null ){
				//writer.println( process( line ) );
				writer.println( DiacriticConverter.removeDiacritics( line ) );
			}
			
			reader.close();
			writer.close();
		}catch( IOException e ){
			e.printStackTrace();
		}
	}
	
	public static String process( String line ){
		if( line.startsWith( "#" ) || line.length() == 0 ){
			return "";
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append( line.split( "\t" )[0] );
		strBuilder.append( "\t" );
		
		String content = line.split( "\t" )[1];
		if( content.split( ";" ).length == 1 ){
			strBuilder.append( content.split( ";" )[0] );
			strBuilder.append( "; " );
			if( DiacriticConverter.hasDiacriticAccents( content.split( ";" )[0] ) ){
				strBuilder.append( DiacriticConverter.removeDiacritics( content.split( ";" )[0] ) );
			}
			return strBuilder.toString();
		}
		
		strBuilder.append( content.split( ";" )[0] );
		strBuilder.append( "; " );
		if( DiacriticConverter.hasDiacriticAccents( content.split( ";" )[0] ) ){
			strBuilder.append( DiacriticConverter.removeDiacritics( content.split( ";" )[0] ) );
			strBuilder.append( "," );
		}
		
		for( String element : content.split( ";" )[1].split( "," ) ){
			strBuilder.append( DiacriticConverter.removeDiacritics( element ) );
			strBuilder.append( "," );
		}
		
		if( strBuilder.charAt( strBuilder.length() - 1 ) == ',' ){
			strBuilder.deleteCharAt( strBuilder.length() - 1 );
		}
		
		return strBuilder.toString();
	}

}
