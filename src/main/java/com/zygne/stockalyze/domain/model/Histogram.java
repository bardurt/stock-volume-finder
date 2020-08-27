package com.zygne.stockalyze.domain.model;

public class Histogram implements Comparable{
    public long timeStamp;
    public int open;
    public int high;
    public int low;
    public int close;
    public long volume;
    public TimeFrame timeFrame;

    public Trend getTrend(){
        if(open < close){
            return Trend.Up;
        } else if (open > close){
            return Trend.Down;
        } else {
            return Trend.Consolidation;
        }
    }

    @Override
    public int compareTo(Object o) {
        long timeB = ((Histogram)o).timeStamp;
        return Long.compare(timeStamp, timeB);
    }
}
