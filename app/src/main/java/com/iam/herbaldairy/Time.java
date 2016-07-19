package com.iam.herbaldairy;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Time {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static Date earliestOfDates(Date[] dates) {
        Date result = dates[0];
        for (Date date : dates) {
            if (date.before(result)) result = date;
        }
        return result;
    }

    public static Date now() {
        return new Date();
    }

    public static Date today() {
        Date date = new Date();

        try {
            date = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static long interval(Date fromDate, Date toDate, TimeUnit timeUnit) {
        Date date = toDate == null ? new Date() : toDate;
        long diffInMillies = date.getTime() - fromDate.getTime();
        return convert(diffInMillies,timeUnit);
    }

    public static long convert(long diff, TimeUnit timeUnit) {
        switch (timeUnit) {
            case SECONDS:
                return diff / 1000;
            case MINUTES:
                return diff / 1000 / 60;
            case HOURS:
                return diff / 1000 / 60 / 60;
            case DAYS:
                return diff / 1000 / 3600 / 24;
            case MILLISECONDS:
                return diff;
        }
        throw new RuntimeException("Unknown TImeUnit in Time.convert(long,TimeUnit)");
    }

    public static Date clearHMSPrecision(Date distillDate) {
        try {
            return simpleDateFormat.parse(simpleDateFormat.format(distillDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("What the fuck! simpleDateFormat and simpleDateFormat is not the same");
    }

    public static Date addPeriod(Date date, int quan, TimeUnit what) {
        Calendar shiftDay = Calendar.getInstance();
        shiftDay.setTime(date);
        switch (what) {
            case HOURS:
                shiftDay.add(Calendar.HOUR, quan);
                break;
            case DAYS:
                shiftDay.add(Calendar.DAY_OF_MONTH, quan);
                break;
        }
        return shiftDay.getTime();
    }

    public static int weekdayNum(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        return weekday - 1 == 0 ? 7 : weekday - 1;
    }

    public static int monthDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        return monthDay;
    }

    public static int monthNum(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        return month + 1;
    }

    public static String weekdayName(Date date) {
        int num = weekdayNum(date);
        switch (num) {
            case 1: return "Понедельник";
            case 2: return "Вторник";
            case 3: return "Среда";
            case 4: return "Четверг";
            case 5: return "Пятница";
            case 6: return "Суббота";
            case 7: return "Воскресенье";
            default: return "undefined";
        }
    }

    public static String monthName(Date date) {
         int num = monthNum(date);
        switch (num) {
            case 1: return "Января";
            case 2: return "Февраля";
            case 3: return "Марта";
            case 4: return "Апреля";
            case 5: return "Мая";
            case 6: return "Июня";
            case 7: return "Июля";
            case 8: return "Августа";
            case 9: return "Сентября";
            case 10: return "Октября";
            case 11: return "Ноября";
            case 12: return "Декабря";
            default: return "undefined";
        }
    }

    public static int year(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }
}
