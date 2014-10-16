
package fpt.qa.additionalinformation.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import jmdn.nlp.diacritic.DiacriticConverter;

public class ConvertDiacriticDirectory{

	public static void main( String[] args ) {
		convertDomainClassifier( "/home/dungx/git/rubyweb/src/main/resources/domains_classifier" );
		convertNameMapperDirectory( "/home/dungx/git/rubyweb/src/main/resources/data-name-mapper" );
		convertSurroundWordsDirectory( "/home/dungx/git/rubyweb/src/main/resources/surroundWords" );
	}

	private static String processNameMapperLine( String line ) {
		StringBuilder strBuilder = new StringBuilder();

		Set< String > tokens = new HashSet< String >();

		strBuilder.append( line.split( "\t" )[ 0 ] );
		strBuilder.append( "\t" );

		String content = line.split( "\t" )[ 1 ];
		if( content.split( ";\\s*" ).length == 1 ){
			strBuilder.append( content.split( ";\\s*" )[ 0 ] );
			strBuilder.append( "; " );
			if( DiacriticConverter.hasDiacriticAccents( content.split( ";\\s*" )[ 0 ] ) ){
				strBuilder.append( DiacriticConverter.removeDiacritics( content.split( ";\\s*" )[ 0 ] ) );
			}
			return strBuilder.toString();
		}

		strBuilder.append( content.split( ";\\s*" )[ 0 ] );
		strBuilder.append( ";" );
		tokens.add( content.split( ";\\s*" )[ 0 ] );

		if( DiacriticConverter.hasDiacriticAccents( content.split( ";\\s*" )[ 0 ] )
				&& !tokens.contains( DiacriticConverter.removeDiacritics( content.split( ";\\s*" )[ 0 ] ) ) ){
			strBuilder.append( DiacriticConverter.removeDiacritics( content.split( ";\\s*" )[ 0 ] ) );
			strBuilder.append( "," );
			tokens.add( DiacriticConverter.removeDiacritics( content.split( ";\\s*" )[ 0 ] ) );
		}

		for( String element : content.split( ";\\s*" )[ 1 ].split( ",\\s*" ) ){
			if( !tokens.contains( element ) ){
				strBuilder.append( element );
				strBuilder.append( "," );
				tokens.add( element );
			}
			if( DiacriticConverter.hasDiacriticAccents( element )
					&& !tokens.contains( DiacriticConverter.removeDiacritics( element ) ) ){
				strBuilder.append( DiacriticConverter.removeDiacritics( element ) );
				strBuilder.append( "," );
				tokens.add( DiacriticConverter.removeDiacritics( element ) );
			}
		}

		if( strBuilder.charAt( strBuilder.length() - 1 ) == ',' ){
			strBuilder.deleteCharAt( strBuilder.length() - 1 );
		}

		return strBuilder.toString();
	}

	private static void convertNameMapperDirectory( String originPath ) {
		File originDir = new File( originPath );
		for( File file : originDir.listFiles() ){
			if( file.isFile() && file.getName().endsWith( ".txt" ) ){
				try{
					BufferedReader reader = new BufferedReader( new FileReader( file ) );
					PrintWriter writer = new PrintWriter( new FileWriter( new File( originDir, file.getName().concat(
							".new" ) ) ) );

					String line;
					while( ( line = reader.readLine() ) != null ){
						if( line.startsWith( "#" ) || line.equalsIgnoreCase( "" ) ){
							writer.println( line );
							continue;
						}
						writer.println( processNameMapperLine( line ) );
					}

					writer.close();
					reader.close();

					// Rename Files
					String originFileName = file.getName();
					file.renameTo( new File( originDir, file.getName().concat(
							".old." + System.currentTimeMillis() / 1000L ) ) );
					new File( originDir, originFileName.concat( ".new" ) ).renameTo( new File( originDir,
							originFileName ) );
				}catch ( IOException e ){
					e.printStackTrace();
				}
			}
		}
	}

	private static void convertSurroundWordsDirectory( String originPath ) {
		File originDir = new File( originPath );
		for( File file : originDir.listFiles() ){
			if( file.isFile() && file.getName().endsWith( ".txt" ) ){
				try{
					BufferedReader reader = new BufferedReader( new FileReader( file ) );
					PrintWriter writer = new PrintWriter( new FileWriter( new File( originDir, file.getName().concat(
							".new" ) ) ) );

					String line;
					while( ( line = reader.readLine() ) != null ){
						if( line.startsWith( "#" ) ){
							writer.println( line );
							continue;
						}
						writer.println( processSurroundWordsLine( line ) );
					}

					writer.close();
					reader.close();

					// Rename Files
					String originFileName = file.getName();
					file.renameTo( new File( originDir, file.getName().concat(
							".old." + System.currentTimeMillis() / 1000L ) ) );
					new File( originDir, originFileName.concat( ".new" ) ).renameTo( new File( originDir,
							originFileName ) );
				}catch ( IOException e ){
					e.printStackTrace();
				}
			}
		}
	}

	private static String processSurroundWordsLine( String line ) {
		StringBuilder strBuilder = new StringBuilder();

		String[] elements = line.split( ",\\s*" );
		strBuilder.append( elements[ 0 ] );

		Set< String > tokens = new HashSet< String >();
		for( int i = 1; i < elements.length; i++ ){
			if( !tokens.contains( elements[ i ] ) ){
				strBuilder.append( ", " );
				strBuilder.append( elements[ i ] );
				tokens.add( elements[ i ] );
				if( DiacriticConverter.hasDiacriticAccents( elements[ i ] )
						&& !tokens.contains( DiacriticConverter.removeDiacritics( elements[ i ] ) ) ){
					strBuilder.append( ", " );
					strBuilder.append( DiacriticConverter.removeDiacritics( elements[ i ] ) );
					tokens.add( DiacriticConverter.removeDiacritics( elements[ i ] ) );
				}
			}
		}

		return strBuilder.toString();
	}

	private static void convertDomainClassifier( String originPath ) {
		File originDir = new File( originPath );
		for( File file : originDir.listFiles() ){
			if( file.isFile() && file.getName().endsWith( ".txt" ) ){
				try{
					BufferedReader reader = new BufferedReader( new FileReader( file ) );
					PrintWriter writer = new PrintWriter( new FileWriter( new File( originDir, file.getName().concat(
							".new" ) ) ) );

					Set< String > tokens = new HashSet< String >();

					String line;
					while( ( line = reader.readLine() ) != null ){
						line = line.trim();
						if( line.startsWith( "#" ) || line.equals( "" ) ){
							writer.println( line );
							continue;
						}
						if( !tokens.contains( line ) ){
							writer.println( line );
							tokens.add( line );
							if( DiacriticConverter.hasDiacriticAccents( line )
									&& !tokens.contains( DiacriticConverter.removeDiacritics( line ) ) ){
								writer.println( DiacriticConverter.removeDiacritics( line ) );
								tokens.add( DiacriticConverter.removeDiacritics( line ) );
							}
						}
					}

					writer.close();
					reader.close();

					// Rename Files
					String originFileName = file.getName();
					file.renameTo( new File( originDir, file.getName().concat(
							".old." + System.currentTimeMillis() / 1000L ) ) );
					new File( originDir, originFileName.concat( ".new" ) ).renameTo( new File( originDir,
							originFileName ) );
				}catch ( IOException e ){
					e.printStackTrace();
				}
			}
		}
	}
}
