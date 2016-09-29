package DataObjects;

import java.util.Comparator;

/**
 * Created by Hans on 29-09-2016.
 */
public class AverageComperator implements Comparator<UserScore> {

    @Override
    public int compare(UserScore us1, UserScore us2) {
        Double s1 = us1.getAvg();
        Double s2 = us2.getAvg();

        return s2.compareTo(s1);
    }

}
