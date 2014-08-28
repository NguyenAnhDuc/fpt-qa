/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.intent.detection;

import fpt.qa.intent.detection.qc.VnIntentDetection;

/**
 *
 * @author ngan
 */
public class MovieIntentDetection {
	// constants
	public static final String MOV_TITLE = "mov_title";
	public static final String MOV_GENRE = "mov_genre";
	public static final String MOV_ACTOR = "mov_actor";
	public static final String MOV_WRITER = "mov_writer";
	public static final String MOV_LANG = "mov_lang";
	public static final String MOV_COUNTRY = "mov_country";
	public static final String MOV_AWARD = "mov_award";
	public static final String MOV_DIRECTOR = "mov_director";
	public static final String MOV_RUNTIME = "mov_runtime";
	public static final String MOV_IMDBRATING = "mov_imdbrating";
	public static final String MOV_PLOT = "mov_plot";
	public static final String MOV_YEAR = "mov_year";
	public static final String MOV_DATE = "mov_date";
	public static final String MOV_TYPE = "mov_type";
	public static final String MOV_AUDIENCE = "mov_audience";
	public static final String MOV_RELEASE = "mov_release";
	
	public static final String CIN_NAME = "cin_name";
	public static final String CIN_ADD = "cin_address";
	public static final String CIN_SERVICETIME = "cin_servicetime";
	public static final String CIN_DATE = "cin_date";
	public static final String CIN_DIS = "cin_dis";
	public static final String CIN_MAP = "cin_map";

	public static final String TICKET_STATUS = "ticket_status";
	public static final String TICKET_PRICE = "ticket_price";
	
	public static final String UNDEF = "undefined";
	// end constants declaration

    static VnIntentDetection classifier;

    public static void init(String qcDir, String dictDir) {
    	FreqConjDict.loadConjList(dictDir + "/conjunction.txt"); 
        classifier = new VnIntentDetection(qcDir);
        classifier.init();

        StopWords.loadList(dictDir + "/stopwords.txt");
    }

    private static String getTunedSent(String sent) {
        String cleanSent = StopWords.removeAllStopWords(" " + sent + " ");
        if (cleanSent.indexOf("có ") == 0) {
            cleanSent = cleanSent.substring(3);
        }
        if (cleanSent.lastIndexOf(" có") == cleanSent.length() - 3) {
            cleanSent = cleanSent.substring(0, cleanSent.length() - 3);
        }
        if (cleanSent.length() > 6 && cleanSent.lastIndexOf(" không") == cleanSent.length() - 6) {
            cleanSent = cleanSent.substring(0, cleanSent.length() - 6);
        }
        return classifier.classify(sent.trim()) + "\t" + cleanSent;
    }

    public static String getIntent(String sent) {
        String tunedSent = getTunedSent(sent);
        if (tunedSent.contains("nước nào") || tunedSent.contains("quốc gia nào")){
        	return MOV_COUNTRY;
        }
        
        if (tunedSent.contains("của đạo diễn nào")){
        	return MOV_DIRECTOR;
        }
        
        if (tunedSent.contains("sản xuất năm nào")){
        	return MOV_YEAR;
        }
        
        if (tunedSent.contains("khởi chiếu") || tunedSent.contains("công chiếu") || 
        		tunedSent.contains("ra mắt")){
        	return MOV_RELEASE;
        }
        
        if (tunedSent.contains("giải thưởng nào")){
        	return MOV_AWARD;
        }
        
        if (tunedSent.contains("ngôn ngữ nào") || tunedSent.contains("ngôn ngữ gì") ||
        		tunedSent.contains("ngôn ngữ được dùng trong phim") || 
        		tunedSent.contains("ngôn ngữ dùng trong phim")){
        	return MOV_LANG;
        }
        
        if (tunedSent.startsWith("PRI\t")) {
//            return "(rạp, how much)";
        	return TICKET_PRICE ;
        }
        if (tunedSent.startsWith("CAL\t")) {
//            return "(phim, when)";
            return MOV_DATE;
        }
        if (tunedSent.indexOf("DES\t") == 0 && (tunedSent.contains("nội dung ")
                || tunedSent.contains("về cái gì"))) {
            return MOV_PLOT;
        }
        if (tunedSent.indexOf("DTI\t") == 0) {
            if (tunedSent.contains("phim")) {
                return MOV_DATE;
            }
            return CIN_DATE;
        }

        if (tunedSent.indexOf("DIS\t") == 0) {
            return CIN_DIS;
        }

        if (tunedSent.indexOf("ADD\t") == 0) {
            return CIN_ADD;
        }

        if (tunedSent.indexOf("DUR\t") == 0) {
            if (tunedSent.contains("kéo dài") || tunedSent.contains("thời lượng")) {
                return MOV_RUNTIME;
            }
            return CIN_SERVICETIME;
        }

        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("thuộc thể loại")) {
            return MOV_GENRE;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("phim gì") || tunedSent.contains("phim nào"))) {
            return MOV_TITLE;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("rạp nào") || tunedSent.contains("rạp gì"))) {
            return CIN_NAME;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("diễn viên nào")) {
            return MOV_ACTOR;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("nào")) {
            int idx = tunedSent.indexOf("nào");
            int idx1 = idx - tunedSent.indexOf("phim");
            int idx2 = idx - tunedSent.indexOf("rạp");
            int idx3 = idx - tunedSent.indexOf("diễn viên");
            int idx4 = idx - tunedSent.indexOf("sao");

            if (idx3 > idx4) {
                idx3 = idx4;
            }
            if (idx1 < idx2 && idx1 < idx3) {
                return MOV_TITLE;
            }
            if (idx2 < idx1 && idx2 < idx3) {
                return CIN_NAME;
            }
            return MOV_ACTOR;
        }

        if (tunedSent.indexOf("NAM\t") == 0) {
            int idx1 = tunedSent.indexOf("phim");
            int idx2 = tunedSent.indexOf("rạp");
            if (idx2 > 0 && idx1 < 0) {
                return CIN_NAME;
            }
            int idx3 = tunedSent.indexOf("diễn viên");
            if (idx3 < 0) {
                idx3 = tunedSent.indexOf("sao");
            }
            if (tunedSent.indexOf("diễn viên nào") > 0 || idx3 > 0 && idx3 < idx1) {
                return MOV_ACTOR;
            }
            idx3 = tunedSent.indexOf("đạo diễn");
            
            if (tunedSent.indexOf("đạo diễn nào") > 0 || idx3 > 0 && (idx3 < idx1 ||
            		tunedSent.contains("ai là") || tunedSent.contains("là ai"))) {
                return MOV_DIRECTOR;
            }
            if (idx2 > 0 && idx2 < idx1) {
                return CIN_NAME;
            }
            return MOV_TITLE;
        }

        if (tunedSent.indexOf("DES\t") == 0) {
            if (tunedSent.indexOf("đường đến") == 4 || tunedSent.indexOf("chỉ đường") == 4) {
                return CIN_MAP;
            }
            return MOV_IMDBRATING;
        }

        if (tunedSent.indexOf("SEL\t") == 0) {
            if (tunedSent.contains("phim")) {
                return MOV_GENRE;
            }
            return UNDEF;
        }
        if (tunedSent.indexOf("NUM\t") == 0) {
            if (tunedSent.contains("imdb")) {
                return MOV_IMDBRATING;
            }
            return UNDEF;
        }

        if (tunedSent.indexOf("POL\t") == 0) {
            if (tunedSent.contains("đặt vé") || tunedSent.contains("còn")) {
                return TICKET_STATUS;
            }

            if (tunedSent.contains("ghế")) {
//                return "(ghế, yesno)";
            	return UNDEF;
            }
            if (tunedSent.contains("phim")) {
//                return "(phim, yesno)";
            	return UNDEF;
            }
            return UNDEF;
//            return "(rạp, yesno)";
        }
        
        return MOV_TITLE;
    }
    
    
    public static void main(String []args){
    	init("data/qc", "data/dicts");
    	System.out.println(getIntent("phim lucy của đạo diễn nào"));
    	System.out.println(getIntent("phim lucy của nước nào"));
    	System.out.println(getIntent("phim lucy sản xuất năm nào"));
    	System.out.println(getIntent("phim lucy của khởi chiếu ngày nào"));
    	System.out.println(getIntent("phim lucy đã đạt được những giải thưởng nào"));
    	System.out.println(getIntent("tối nay rạp chiếu phim gì"));
    	System.out.println(getIntent("rạp quốc gia chiếu phim lucy vào những khung giờ nào"));
    	System.out.println(getIntent("phim lucy kéo dài bao lâu"));
    	System.out.println(getIntent("ai là đạo diễn phim lucy"));
    	System.out.println(getIntent("diễn viên trong phim lucy có những ai"));
    	System.out.println(getIntent("đang có phim gì chiếu rạp"));
    	System.out.println(getIntent("phim lucy được đánh giá thế nào"));
    	System.out.println(getIntent("phim lucy sản xuất năm nào"));
    	System.out.println(getIntent("phim lucy ra mắt ngày nào"));
    	System.out.println(getIntent("phim lucy thuộc thể loại nào"));
    	System.out.println(getIntent("phim lucy dùng ngôn ngữ nào"));
    	System.out.println(getIntent("phim lucy là phim hài hay phim hành động"));
    	System.out.println(getIntent("phim lucy là phim 2D hay phim 3D"));
    	System.out.println(getIntent("phim lucy có phiên bản 3d không"));
    	System.out.println(getIntent("phim lucy có dành cho trẻ em không"));
    	System.out.println(getIntent("rạp quốc gia có ghế dành cho trẻ em không"));
    	
    }
}
