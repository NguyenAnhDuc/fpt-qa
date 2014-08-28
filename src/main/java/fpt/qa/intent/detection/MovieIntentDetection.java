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
        	return IntentConstants.MOV_COUNTRY;
        }
        
        if (tunedSent.contains("của đạo diễn nào")){
        	return IntentConstants.MOV_DIRECTOR;
        }
        
        if (tunedSent.contains("sản xuất năm nào")){
        	return IntentConstants.MOV_YEAR;
        }
        
        if (tunedSent.contains("khởi chiếu") || tunedSent.contains("công chiếu") || 
        		tunedSent.contains("ra mắt")){
        	return IntentConstants.MOV_RELEASE;
        }
        
        if (tunedSent.contains("giải thưởng nào")){
        	return IntentConstants.MOV_AWARD;
        }
        
        if (tunedSent.contains("ngôn ngữ nào") || tunedSent.contains("ngôn ngữ gì") ||
        		tunedSent.contains("ngôn ngữ được dùng trong phim") || 
        		tunedSent.contains("ngôn ngữ dùng trong phim")){
        	return IntentConstants.MOV_LANG;
        }
        
        if (tunedSent.startsWith("PRI\t")) {
//            return "(rạp, how much)";
        	return IntentConstants.TICKET_PRICE ;
        }
        if (tunedSent.startsWith("CAL\t")) {
//            return "(phim, when)";
            return IntentConstants.MOV_DATE;
        }
        if (tunedSent.indexOf("DES\t") == 0 && (tunedSent.contains("nội dung ")
                || tunedSent.contains("về cái gì"))) {
            return IntentConstants.MOV_PLOT;
        }
        if (tunedSent.indexOf("DTI\t") == 0) {
            if (tunedSent.contains("phim")) {
                return IntentConstants.MOV_DATE;
            }
            return IntentConstants.CIN_DATE;
        }

        if (tunedSent.indexOf("DIS\t") == 0) {
            return IntentConstants.CIN_DIS;
        }

        if (tunedSent.indexOf("ADD\t") == 0) {
            return IntentConstants.CIN_ADD;
        }

        if (tunedSent.indexOf("DUR\t") == 0) {
            if (tunedSent.contains("kéo dài") || tunedSent.contains("thời lượng")) {
                return IntentConstants.MOV_RUNTIME;
            }
            return IntentConstants.CIN_SERVICETIME;
        }

        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("thuộc thể loại")) {
            return IntentConstants.MOV_GENRE;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("phim gì") || tunedSent.contains("phim nào"))) {
            return IntentConstants.MOV_TITLE;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("rạp nào") || tunedSent.contains("rạp gì"))) {
            return IntentConstants.CIN_NAME;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("diễn viên nào")) {
            return IntentConstants.MOV_ACTOR;
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
                return IntentConstants.MOV_TITLE;
            }
            if (idx2 < idx1 && idx2 < idx3) {
                return IntentConstants.CIN_NAME;
            }
            return IntentConstants.MOV_ACTOR;
        }

        if (tunedSent.indexOf("NAM\t") == 0) {
            int idx1 = tunedSent.indexOf("phim");
            int idx2 = tunedSent.indexOf("rạp");
            if (idx2 > 0 && idx1 < 0) {
                return IntentConstants.CIN_NAME;
            }
            int idx3 = tunedSent.indexOf("diễn viên");
            if (idx3 < 0) {
                idx3 = tunedSent.indexOf("sao");
            }
            if (tunedSent.indexOf("diễn viên nào") > 0 || idx3 > 0 && idx3 < idx1) {
                return IntentConstants.MOV_ACTOR;
            }
            idx3 = tunedSent.indexOf("đạo diễn");
            
            if (tunedSent.indexOf("đạo diễn nào") > 0 || idx3 > 0 && (idx3 < idx1 ||
            		tunedSent.contains("ai là") || tunedSent.contains("là ai"))) {
                return IntentConstants.MOV_DIRECTOR;
            }
            if (idx2 > 0 && idx2 < idx1) {
                return IntentConstants.CIN_NAME;
            }
            return IntentConstants.MOV_TITLE;
        }

        if (tunedSent.indexOf("DES\t") == 0) {
            if (tunedSent.indexOf("đường đến") == 4 || tunedSent.indexOf("chỉ đường") == 4) {
                return IntentConstants.CIN_MAP;
            }
            return IntentConstants.MOV_IMDBRATING;
        }

        if (tunedSent.indexOf("SEL\t") == 0) {
            if (tunedSent.contains("phim")) {
                return IntentConstants.MOV_GENRE;
            }
            return IntentConstants.UNDEF;
        }
        if (tunedSent.indexOf("NUM\t") == 0) {
            if (tunedSent.contains("imdb")) {
                return IntentConstants.MOV_IMDBRATING;
            }
            return IntentConstants.UNDEF;
        }

        if (tunedSent.indexOf("POL\t") == 0) {
            if (tunedSent.contains("đặt vé") || tunedSent.contains("còn")) {
                return IntentConstants.TICKET_STATUS;
            }

            if (tunedSent.contains("ghế")) {
//                return "(ghế, yesno)";
            	return IntentConstants.UNDEF;
            }
            if (tunedSent.contains("phim")) {
//                return "(phim, yesno)";
            	return IntentConstants.UNDEF;
            }
            return IntentConstants.UNDEF;
//            return "(rạp, yesno)";
        }
        
        return IntentConstants.MOV_TITLE;
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
