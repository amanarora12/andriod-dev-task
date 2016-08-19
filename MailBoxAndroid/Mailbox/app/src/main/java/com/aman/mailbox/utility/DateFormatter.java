package com.aman.mailbox.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Aman on 19-08-2016.
 */
public class DateFormatter {
    public static String getDate(long ts){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        Date resultdate = new Date(ts);
        return simpleDateFormat.format(resultdate);
    }
    public static String getDateTime(long ts){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM, yyyy, HH:mm:ss a", Locale.getDefault());
        Date resultdate = new Date(ts);
        return simpleDateFormat.format(resultdate);
    }
}
