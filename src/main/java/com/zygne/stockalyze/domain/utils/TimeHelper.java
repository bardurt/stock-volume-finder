package com.zygne.stockalyze.domain.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeHelper {

    private static final int MS_IN_SECONDS = 1000;

    private static final int SEC_IN_DAY = 86400;

    private static final String timeFormat = "yyyy-MM-dd";

    public static int getDaysDifference(String start, String end) {

        long time1 = getTimeStamp(start);
        long time2 = getTimeStamp(end);

        return getDaysDifference(time1, time2);
    }

    public static int getDaysDifference(long start, long end) {

        long diff = start - end;

        long days = diff / (MS_IN_SECONDS * SEC_IN_DAY);

        return (int) days;
    }

    public static long getTimeStamp(String dateString) {
        DateFormat df = new SimpleDateFormat(timeFormat);

        Date date;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            return 0;
        }

        return date.getTime();
    }
}
