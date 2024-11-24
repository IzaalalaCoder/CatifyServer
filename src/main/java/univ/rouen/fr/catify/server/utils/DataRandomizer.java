package univ.rouen.fr.catify.server.utils;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class DataRandomizer {
    public static Date generateRandomDate(Date startDate, Date endDate) {
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        long randomMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
        return new Date(randomMillis);
    }
}