package today.tacademy.com.finalprojecttodayis.common;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

;

/**
 * Created by Tacademy on 2017-11-06.
 */

public class ItemsCommonModule {

    private static SimpleDateFormat fmtMMddhhmma;
    private static SimpleDateFormat fmtHHmm;

    static{
        fmtMMddhhmma = new SimpleDateFormat("MM월 dd일 hh:mm a");
        fmtHHmm = new SimpleDateFormat("HHmm");
    }

    // method to gain today's date and time
    public static String getCurrentDateTime(){
        String currDateTime = fmtMMddhhmma.
                format(GregorianCalendar.getInstance(Locale.KOREA).getTime());
        return currDateTime;
    }

    // method to gain current hour
    public static String getCurrentHour(){
        String currDateTime = fmtHHmm.
                format(GregorianCalendar.getInstance(Locale.KOREA).getTime());
        return currDateTime;
    }

}
