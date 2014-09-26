
package fpt.qa.domainclassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fpt.qa.mdnlib.nlp.vn.vntokenizer.VnTokenizer;
import fpt.qa.mdnlib.struct.conjunction.ConjunctionChecker;
import fpt.qa.mdnlib.struct.pair.Pair;

public class DomainClassifier{

	private Map< String, ConjunctionChecker > domainDataType1;
	private Map< String, ConjunctionChecker > domainDataType2;
	private double importantRatio;
	private VnTokenizer vnTokenizer;
	private boolean printDebug;

	public double getImportantRatio() {
		return importantRatio;
	}

	public void setImportantRatio( double importantRatio ) {
		this.importantRatio = importantRatio;
	}

	public DomainClassifier() {
		domainDataType1 = new HashMap< String, ConjunctionChecker >();
		domainDataType2 = new HashMap< String, ConjunctionChecker >();
		
		vnTokenizer = new VnTokenizer();
		
		importantRatio = 2.0;
		
		printDebug = true;
	}

	public boolean isPrintDebug() {
		return printDebug;
	}

	public void setPrintDebug( boolean printDebug ) {
		this.printDebug = printDebug;
	}

	public DomainClassifier( String resourcePath ) {
		this();
		loadDomainData( resourcePath + "/domains_classifier" );
	}

	private void loadDomainConjunction( String domainName, String type, File file ) {
		Map< String, ConjunctionChecker > domainData;

		if( type.equalsIgnoreCase( "1" ) ){
			domainData = domainDataType1;
		}else{
			domainData = domainDataType2;
		}

		if( !domainData.containsKey( domainName ) ){
			domainData.put( domainName, new ConjunctionChecker() );
		}

		ConjunctionChecker conjunction = domainData.get( domainName );
		try{
			BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ) ) );

			String line = null;
			while( ( line = reader.readLine() ) != null ){
				conjunction.addConjunction( line );
			}

			reader.close();
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}

	private void loadDomainData( String directory ) {
		File dataDirectory = new File( directory );
		if( !dataDirectory.isDirectory() ){
			System.err.println( "[Domain Classifier] " + directory + " is not directory" );
			return;
		}
		for( File dataFile : dataDirectory.listFiles() ){
			if( !dataFile.isDirectory() ){
				if( dataFile.getName().endsWith( ".1" ) ){
					String fileName = dataFile.getName();
					String domainName = fileName.substring( 0, fileName.lastIndexOf( "." ) );
					loadDomainConjunction( domainName, "1", dataFile );
				}else if( dataFile.getName().endsWith( ".2" ) ){
					String fileName = dataFile.getName();
					String domainName = fileName.substring( 0, fileName.lastIndexOf( "." ) );
					loadDomainConjunction( domainName, "2", dataFile );
				}
			}
		}
	}

	public String getDomain( String text ) {
		if( isPrintDebug() ) System.err.print( "[Domain Classifier] " + text + " " );
		Map< String, Double > type1Score = computeScore( text, "1" );
		Map< String, Double > type2Score = computeScore( text, "2" );
		Map< String, Double > finalScore = computeFinalScore( type1Score, type2Score );

		for( String domain : finalScore.keySet() ){
			if( isPrintDebug() ) System.err.print( " {" + domain + ", " + finalScore.get( domain ) + "} " );
		}

		if( isPrintDebug() ) System.err.println();
		if( isPrintDebug() ) System.err.flush();
		return getHighestScore( finalScore ).first;
	}

	private Map< String, Double > computeScore( String text, String type ) {
		Map< String, Double > score = new HashMap< String, Double >();

		Map< String, ConjunctionChecker > domainData;
		if( type.equalsIgnoreCase( "1" ) ){
			domainData = domainDataType1;
		}else{
			domainData = domainDataType2;
		}

		for( String domain : domainData.keySet() ){
			double s = domainData.get( domain ).getRelevantConjunctions( text, true ).size();
			score.put( domain, s );
		}

		return score;
	}

	private Map< String, Double > computeFinalScore( Map< String, Double > score1, Map< String, Double > score2 ) {
		Map< String, Double > finalScore = new HashMap< String, Double >();

		Set< String > domains = new HashSet( score1.keySet() );
		domains.addAll( score2.keySet() );

		for( String domain : domains ){
			double s1 = 0;
			double s2 = 0;
			if( score1.containsKey( domain ) ){
				s1 = score1.get( domain );
			}
			if( score2.containsKey( domain ) ){
				s2 = score2.get( domain );
			}
			finalScore.put( domain, s1 * getImportantRatio() + s2 );
		}

		return finalScore;
	}

	private Pair< String, Double > getHighestScore( Map< String, Double > score ) {
		double maxValue = -1;
		String maxDomain = "";

		for( String domain : score.keySet() ){
			if( score.get( domain ) > maxValue ){
				maxValue = score.get( domain );
				maxDomain = domain;
			}
		}

		return new Pair< String, Double >( maxDomain, maxValue );
	}

	public static void main( String[] args ) {
		DomainClassifier testClassifier = new DomainClassifier( "/home/dungx/git/rubyweb/src/main/resources" );
		testClassifier.setPrintDebug( false );

		try{
			
			int count = 0;
			
			File movieFile = new File( "500_movie_2.txt" );
			BufferedReader reader = new BufferedReader( new FileReader( movieFile ) );
			String line = null;
			while( ( line = reader.readLine() ) != null ){
				if( !testClassifier.getDomain( line ).equalsIgnoreCase( "movie" ) ){
					System.out.println( "True:Movie Predict:" + testClassifier.getDomain( line ) + " " + line );
					count++;
				}
			}
			
			File tvFile = new File( "tv_quest.txt" );
			reader = new BufferedReader( new FileReader( tvFile ) );
			line = null;
			while( ( line = reader.readLine() ) != null ){
				if( !testClassifier.getDomain( line ).equalsIgnoreCase( "tv" ) ){
					System.out.println( "True:tv Predict:" + testClassifier.getDomain( line ) + " " + line );
					count++;
				}
			}
			
			System.out.println( "Total Errors: " + count );
		}catch ( IOException e ){
			e.printStackTrace();
		}

	}

}
