package com.iam.herbaldairy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public static Date theEarliestDate(Date[] dates) {
        Date result = dates[0];
        for (Date date : dates) {
            if (date.before(result)) result = date;
        }
        return result;
    }
}
