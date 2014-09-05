/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.intent.detection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        if (tunedSent.contains("nước nào") && !tunedSent.contains("tiếng nước nào") || 
                tunedSent.contains("quốc gia nào")) {
            return IntentConstants.MOV_COUNTRY;
        }

        if (tunedSent.contains("của đạo diễn nào")) {
            return IntentConstants.MOV_DIRECTOR;
        }

        if (tunedSent.contains("sản xuất năm nào")) {
            return IntentConstants.MOV_YEAR;
        }

        if (tunedSent.contains("khởi chiếu") || tunedSent.contains("công chiếu")
                || tunedSent.contains("ra mắt") || tunedSent.contains("bắt đầu chiếu")) {
            return IntentConstants.MOV_RELEASE;
        }

        if (tunedSent.contains("giải thưởng nào")) {
            return IntentConstants.MOV_AWARD;
        }

        if (tunedSent.contains("ngôn ngữ nào") || tunedSent.contains("ngôn ngữ gì")
                || tunedSent.contains("ngôn ngữ được dùng trong phim")
                || tunedSent.contains("ngôn ngữ dùng trong phim") ||
                tunedSent.contains("tiếng gì") || tunedSent.contains("tiếng nước nào")) {
            return IntentConstants.MOV_LANG;
        }

        if (tunedSent.startsWith("PRI\t")) {
//            return "(rạp, how much)";
            return IntentConstants.TICKET_PRICE;
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
            if (tunedSent.contains("phim") || tunedSent.contains("suất chiếu") || 
                    tunedSent.contains("xuất chiếu")) {
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
            if (tunedSent.contains("cách đây") || tunedSent.contains("mất bao lâu") ||
                    tunedSent.contains("mất bao nhiêu phút")){
                return IntentConstants.UNDEF;
            }
            return IntentConstants.CIN_SERVICETIME;
        }
        
        if (tunedSent.indexOf("HUM\t") == 0){
            if (tunedSent.contains("đạo diễn")){
                return IntentConstants.MOV_DIRECTOR;
            }
            if (tunedSent.contains("diễn viên") || tunedSent.contains("sao nào") || tunedSent.contains("dàn sao")){
                return IntentConstants.MOV_ACTOR;
            }
            return IntentConstants.MOV_AUDIENCE;
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

            if (tunedSent.indexOf("đạo diễn nào") > 0 || idx3 > 0 && (idx3 < idx1
                    || tunedSent.contains("ai là") || tunedSent.contains("là ai"))) {
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
            if (tunedSent.contains("phim") && (tunedSent.contains("2D")||tunedSent.contains("3D") )) {
                return IntentConstants.MOV_TYPE;
            }
            
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
                return IntentConstants.UNDEF;
            }
            if (tunedSent.contains("phim") && (tunedSent.contains(" 2D") || tunedSent.contains(" 3D"))) {
                return IntentConstants.MOV_TYPE;
            }
            return IntentConstants.UNDEF;
        }

        return IntentConstants.MOV_TITLE;
    }

    public static void main(String[] args) {
        init("/home/ngan/Work/AHongPhuong/Intent_detection/data/qc/1", 
                "/home/ngan/Work/AHongPhuong/Intent_detection/data/dicts");
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/home/ngan/Work/AHongPhuong/Intent_detection/data/whole.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/ngan/Work/AHongPhuong/Intent_detection/data/test.result"));
            
            String line;
            while((line = reader.readLine()) != null){
                if (line.isEmpty()){
                    continue;
                }
                writer.write(getIntent(line) + "\t" + classifier.classify(line.trim()) + "\t" + line + "\n");
            }
            
            System.out.println(getTunedSent("phim lucy sử dụng tiếng gì"));
            writer.close();
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(MovieIntentDetection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
