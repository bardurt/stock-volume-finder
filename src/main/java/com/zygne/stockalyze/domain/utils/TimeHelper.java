package com.zygne.stockalyze.domain.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {

    private static final String timeFormat = "yyyy-MM-dd";

    public static int getMonthsDifference(String start, String end){

        DateFormat df = new SimpleDateFormat(timeFormat);

        Date startDate;
        try {
             startDate = df.parse(start);
        } catch (ParseException e) {
            return 0;
        }

        Date endDate;

        try {
            endDate = df.parse(end);
        } catch (ParseException e) {
            return 0;
        }

        int yearStart = startDate.getYear();
        int yearEnd = endDate.getYear();

        if(yearEnd > yearStart){
            return (yearEnd-yearStart)*12;
        } else if(yearStart > yearEnd){
            return (yearStart-yearEnd)*12;
        } else {
            return 12;
        }
    }
}
