package com.zygne.stockalyze.domain.model;

public class PredictionData implements StockState {

    private final double meanVolume = 0.0d;
    public int currentPrice = -1;
    public int firstHourVolume = 0;
    public int newsCatalyst = 1;
    public int fiveDayTrend = 0;
    public int formerRunner = 0;
    public int gapUp = 0;

    @Override
    public int runner() {
        return formerRunner;
    }

    @Override
    public Trend trend() {
        return Trend.fromInt(fiveDayTrend);
    }

    @Override
    public News news() {
        return News.fromInt(newsCatalyst);
    }

    @Override
    public int gap() {
        return gapUp;
    }

    @Override
    public void normalize() {

    }

    @Override
    public int currentPrice() {
        return currentPrice;
    }

    @Override
    public int significantVolume() {
        return firstHourVolume;
    }

    @Override
    public int avgVolume() {
        return (int) meanVolume;
    }
}
