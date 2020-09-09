package com.zygne.stockalyze.domain.model;

import com.zygne.stockalyze.domain.model.enums.TimeFrame;

public class Histogram implements Comparable{
    public long timeStamp;
    public int open;
    public int high;
    public int low;
    public int close;
    public long volume;
    public TimeFrame timeFrame;
    public double decay = 1;

    @Override
    public int compareTo(Object o) {
        long timeB = ((Histogram)o).timeStamp;
        return Long.compare(timeStamp, timeB);
    }
}
