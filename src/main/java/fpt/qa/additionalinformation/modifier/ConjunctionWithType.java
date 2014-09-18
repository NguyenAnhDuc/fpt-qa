package fpt.qa.additionalinformation.modifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fpt.qa.additionalinformation.name_mapper.NamedEngineImp;
import fpt.qa.mdnlib.nlp.vn.vntokenizer.VnTokenizer;
import fpt.qa.mdnlib.struct.conjunction.ConjunctionChecker;
import fpt.qa.mdnlib.struct.pair.Pair;

public class ConjunctionWithType extends ConjunctionChecker{
	private Map< String, String > conjunctionType;
	private NamedEngineImp nameMapperEngine;

	public ConjunctionWithType( String resourcePath ) {
		conjunctionType = new HashMap< String, String >();
		nameMapperEngine = new NamedEngineImp( resourcePath );

        loadConjunctionFromNameMapper( nameMapperEngine );
        
        VnTokenizer.loadSpecialChars( resourcePath + "/dicts/specialchars/special-chars.xml" );
		VnTokenizer.loadRegexXMLFile( resourcePath + "/regexes/regular-expressions.xml" );
	}

	public void loadConjunctionType( File file ) {
		try{
			BufferedReader reader = new BufferedReader( new FileReader( file ) );

			String line = null;
			while( ( line = reader.readLine() ) != null ){
				String[] elements = line.split( "\t" );
				int numberOfElements = elements.length;

				String type = elements[ 0 ];

				if( numberOfElements == 2 ){
					String conjunction = elements[ 1 ];
					addConjunctionWithType( conjunction, type );
				}else{
					for( int i = 1; i < numberOfElements; i++ ){
						String conjunction = elements[ i ];
						addConjunctionWithType( conjunction, type );
					}
				}
			}

			reader.close();
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}

    private void loadConjunctionFromNameMapper(  NamedEngineImp nameMapperEngine ){
        for( Pair <String, String> pair : nameMapperEngine.getAllNames() ){
            String name = pair.first;
            String type = pair.second;
            addConjunctionWithType( name, type );
        }
    }

	public void addConjunctionWithType( String str, String type ) {
		addConjunction( "{" + str + "}" );
		conjunctionType.put( str.toLowerCase(), type );
	}

	public String getConjunctionType( String conjunction ) {
		return conjunctionType.get( conjunction.toLowerCase().substring( 1, conjunction.length() - 1 ) );
	}

	public List< Pair< String, String > > getOriginRelevantConjunctionsWithType( String text ) {
		List< Pair< String, String > > relConjunctions = new ArrayList< Pair< String, String > >();

		Set< String > originSets = new HashSet< String >();

		for( String conj : getRelevantConjunctions( VnTokenizer.tokenize( text ) , true ) ){
			String origin = nameMapperEngine.getFinalName( getConjunctionType( conj ),
					conj.substring( 1, conj.length() - 1 ) );
			if( !originSets.contains( origin ) ){
				relConjunctions.add( new Pair< String, String >( origin, getConjunctionType( conj ) ) );
				originSets.add( origin );
			}
		}

		return relConjunctions;
	}

	public List< Pair< ArrayList< String >, String > > getListRelevantConjunctionsWithType( String text ) {
		List< Pair< String, String > > origins = getOriginRelevantConjunctionsWithType( text );
		List< Pair< ArrayList< String >, String > > relConjunctions = new ArrayList< Pair< ArrayList< String >, String > >();

		for( Pair< String, String > conjWithType : origins ){
			String conj = conjWithType.first;
			String type = conjWithType.second;
			relConjunctions.add( new Pair< ArrayList< String >, String >( new ArrayList< String >( nameMapperEngine
					.getVariationNames( type, conj ) ), type ) );
		}

		return relConjunctions;
	}
	
	public List< Pair< String, String > > pruneMultipleResults( List< Pair< String, String > > rawResult ){
		return rawResult;
	}
	
	public List< String > getLongerOverlappedResult( List< String > conjunctionList ){
		List< String > newList = new ArrayList< String >();
		newList.addAll( conjunctionList );
		
		for( int i = 0; i < newList.size() - 1; i++ ){
			for( int j = i + 1; j < newList.size(); j++ ){
				if( isSmallerSubsetOf( newList.get( j ) , newList.get( i ) ) ){
					newList.remove( j );
					j--;
					continue;
				}
				if( isSmallerSubsetOf( newList.get( i ) , newList.get( j ) ) ){
					String temp = newList.get( i );
					newList.set( i, newList.get( j ) );
					newList.set( j, temp );
					j--;
				}
			}
		}
		
		return newList;
	}
	
	private boolean isSmallerSubsetOf( String lhs, String rhs ){
		Set< String > lhsSet = new HashSet< String >( Arrays.asList( lhs.split( "[\\s\\{\\}]+" ) ) );
		Set< String > rhsSet = new HashSet< String >( Arrays.asList( rhs.split( "[\\s\\{\\}]+" ) ) );
		return rhsSet.containsAll( lhsSet ) && rhsSet.size() > lhsSet.size();
	}
	
	public static void main( String[] args ){
//		System.out.println( isSmallerSubsetOf( "{Va toi cung yeu em }", "{ Va   toi   cung   yeu   em   rat   nhieu  }") );
//		List< String > testList = new ArrayList< String >();
//		testList.add( "{ Va   toi   cung   yeu   em   rat   nhieu  }" );
//		testList.add( "{Va toi cung yeu em }" );
//		System.out.println( getLongerOverlappedResult( testList ) );
	}
}
