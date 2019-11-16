package webapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jakub on 15.11.2019.
 */
public class TimeUtil {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");


    public static String formatDate(Date date) {
        return format.format(date);
    }
}
