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
        if (tunedSent.startsWith("PRI\t")) {
            return "(rạp, how much)";
        }
        if (tunedSent.startsWith("CAL\t")) {
            return "(phim, when)";
        }
        if (tunedSent.indexOf("DES\t") == 0 && (tunedSent.contains("nội dung ")
                || tunedSent.contains("về cái gì"))) {
            return "(nội dung, what)";
        }
        if (tunedSent.indexOf("DTI\t") == 0) {
            if (tunedSent.contains("phim")) {
                return "(phim, when)";
            }
            return "(rạp, when)";
        }

        if (tunedSent.indexOf("DIS\t") == 0) {
            return "(rạp, how far)";
        }

        if (tunedSent.indexOf("ADD\t") == 0) {
            return "(rạp, where)";
        }

        if (tunedSent.indexOf("DUR\t") == 0) {
            if (tunedSent.contains("kéo dài") || tunedSent.contains("thời lượng")) {
                return "(phim, how long)";
            }
            return "(rạp, how long)";
        }

        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("thuộc thể loại")) {
            return "(phim, which)";
        }
        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("phim gì") || tunedSent.contains("phim nào"))) {
            return "(phim, what)";
        }
        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("rạp nào") || tunedSent.contains("rạp gì"))) {
            return "(rạp, what)";
        }
        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("diễn viên nào")) {
            return "(diễn viên, who)";
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
                return "(phim, what)";
            }
            if (idx2 < idx1 && idx2 < idx3) {
                return "(rạp, what)";
            }
            return "(diễn viên, who)";
        }

        if (tunedSent.indexOf("NAM\t") == 0) {
            int idx1 = tunedSent.indexOf("phim");
            int idx2 = tunedSent.indexOf("rạp");
            if (idx2 > 0 && idx1 < 0) {
                return "(rạp, what)";
            }
            int idx3 = tunedSent.indexOf("diễn viên");
            if (idx3 < 0) {
                idx3 = tunedSent.indexOf("sao");
            }
            if (idx3 > 0 && idx3 < idx1) {
                return "(diễn viên, who)";
            }
            idx3 = tunedSent.indexOf("đạo diễn");
            if (idx3 > 0 && idx3 < idx1) {
                return "(đạo diễn, who)";
            }
            if (idx2 > 0 && idx2 < idx1) {
                return "(rạp, what)";
            }
            return "(phim, what)";
        }

        if (tunedSent.indexOf("DES\t") == 0) {
            if (tunedSent.indexOf("đường đến") == 4 || tunedSent.indexOf("chỉ đường") == 4) {
                return "(rạp, map)";
            }
            return "(phim, how)";
        }

        if (tunedSent.indexOf("SEL\t") == 0) {
            if (tunedSent.contains("phim")) {
                return "(phim, selection)";
            }
            return "(rạp, selection)";
        }
        if (tunedSent.indexOf("NUM\t") == 0) {
            if (tunedSent.contains("imdb")) {
                return "(rating, how)";
            }
            return "(giảm giá, how)";
        }

        if (tunedSent.indexOf("POL\t") == 0) {
            if (tunedSent.contains("đặt vé") || tunedSent.contains("còn")) {
                return "(vé, yesno)";
            }

            if (tunedSent.contains("ghế")) {
                return "(ghế, yesno)";
            }
            if (tunedSent.contains("phim")) {
                return "(phim, yesno)";
            }
            return "(rạp, yesno)";
        }
        return "(phim, what)";
    }
}
