package fpt.qa.additionalinformation.test;

import java.util.ArrayList;

import fpt.qa.additionalinformation.modifier.ConjunctionWithType;
import fpt.qa.mdnlib.struct.pair.Pair;

public class ConjunctionWithTypeTest{
    private static final String TEXT = "rạp dân chủ ở đâu?";
    private static final String TEXT2 = "rạp chủ dân ở đâu?";
    private static final String TEXT3 = "phim quái vật frankenstein chiếu mấy giờ?";
    private static final String TEXT4 = "trung tâm chiếu phim quốc gia ở đường nào ?";
    private static final String TEXT5 = "hôm nay có phim gì không lucy?";

    public static void main( String[] args ){

        ConjunctionWithType conjunctionWithType = new ConjunctionWithType( "SRC/MAIN/resources/" );
        //conjunctionWithType.loadConjunctionType( new File( "resources/conjunction/movies_infor.txt" ) );

        System.out.println( TEXT5 );
        for( Pair< String, String > element : conjunctionWithType.getOriginRelevantConjunctionsWithType( TEXT3 ) ){
            System.out.println( element.first + " " + element.second );
        }

//        for( Pair< ArrayList< String >, String > element : conjunctionWithType.getListRelevantConjunctionsWithType( TEXT5 ) ){
//            System.out.println( element.first + " " + element.second );
//        }
    }
}
