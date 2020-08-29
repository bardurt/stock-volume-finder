package com.zygne.stockalyze.domain.model;

public class GapDetails {

    public double minGap = 0;
    public double maxGap = 0;
    public double currentGap = 0;
    public double gap10 = 0;
    public double gap20 = 0;
    public double gapBull = 0;
    public int gapCount = 0;

    public boolean isGapper(){
        return currentGap >= minGap;
    }
}
