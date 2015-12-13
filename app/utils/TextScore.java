package utils;

import com.representqueens.lingua.en.Fathom;
import com.representqueens.lingua.en.Readability;
import models.Document;

/**
 * Created by hugo on 13/12/15.
 */
public class TextScore {


    /*Code to measure the Text score. I used a external lib so i don' really know what is a text score. I saw that normally
    it is calculed witht he algorithm Flesch.
    This score rates text on a 100 point scale. The higher the score, the easier it is to understand the text.
    A score of 60 to 70 is considered to be optimal.

    So i am going to truncate it and adapt it to the assigment description

    More info of that lib can be found at: http://www.representqueens.com/fathom/docs/api/ & https://github.com/ogrodnek/java_fathom
     */
    public static float calculateScore(Document doc) {

        float score = 0;
        String description = doc.getDescription();
        if (description != null) {
            Fathom fathom = new Fathom();
            Fathom.Stats stats = fathom.analyze(description);

            score = Readability.calcFlesch(stats);
        }
        return score > 0 ? Math.abs(score - 100) / 10 : 0;
    }

}
