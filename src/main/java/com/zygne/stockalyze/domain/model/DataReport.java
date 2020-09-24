package com.zygne.stockalyze.domain.model;

import com.zygne.stockalyze.domain.model.enums.MarketTime;

import java.util.List;

public class DataReport {

    public String ticker;
    public int timeSpan;
    public List<LiquidityZone> zones;
    public List<LiquidityZone> filteredZones;
    public List<LiquidityZone> range;
    public List<PowerZone> powerZones;
    public List<PowerZone> filteredPowerZones;
    public GapDetails gapDetails;
    public MarketTime marketTime;
    public Statistics statistics;
    public int stockFloat;
    public List<LiquidityZone> topZones;
    public int openPrice;
    public String folder;
}
