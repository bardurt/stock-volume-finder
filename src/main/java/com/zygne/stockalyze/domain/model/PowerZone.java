package com.zygne.stockalyze.domain.model;

import java.util.Comparator;

public class PowerZone {

    public static final int RECJECT = 0;
    public static final int ACCEPT = 1;

    public int type;
    public int start;
    public int end;

    public long timeStamp;


    public boolean inZone(int value){

        return value <= end && value >= start;
    }

    @Override
    public String toString() {
        String value = "";

        if(type == RECJECT){
            value = "Reject";
        } else {
            value = "Accept";
        }

        return value + " " + start + " " + end + " " + timeStamp;
    }

    public static final class TimeComparator implements Comparator<PowerZone> {

        @Override
        public int compare(PowerZone o1, PowerZone o2) {
            return Long.compare(o1.timeStamp, o2.timeStamp);
        }
    }
}
