package com.zygne.stockalyze.domain.model;

import com.zygne.stockalyze.domain.model.enums.MarketTime;

import java.util.List;

public class DataReport {

    public String ticker;
    public int timeSpan;
    public List<LiquidityZone> zones;
    public GapDetails gapDetails;
    public MarketTime marketTime;
    public Statistics statistics;
    public int stockFloat;
    public List<LiquidityZone> topZones;
    public int openPrice;
}
