package com.zygne.stockalyze.domain.utils;

public class Timer {

    private long startTime = 0L;
    private long stopTime = 0L;

    public void start(){
        startTime = System.currentTimeMillis();
    }

    public void stop(){
        stopTime = System.currentTimeMillis();
    }

    public long getDuration(){
        return stopTime - startTime;
    }
}
