package com.zygne.stockalyze.domain.model;

public class CandleStick {

    public boolean bullish;
    public int bodySize;
    public int lowerWick;
    public int upperWick;
    public int top;
    public int bottom;
    public int bodyTop;
    public int bodyBottom;
    public long timeStamp;

    public CandleStick(Histogram histogram){
        bullish = histogram.close > histogram.open;;
        bodySize = Math.abs(histogram.close - histogram.open);
        top = histogram.high;
        bottom = histogram.low;

        if(bullish){
            upperWick = histogram.high - histogram.close;
            lowerWick = histogram.open - histogram.low;
            bodyTop = histogram.close;
            bodyBottom = histogram.open;
        } else {
            upperWick = histogram.high - histogram.open;
            lowerWick = histogram.close - histogram.low;
            bodyTop = histogram.open;
            bodyBottom = histogram.close;
        }

        timeStamp = histogram.timeStamp;
    }

}
