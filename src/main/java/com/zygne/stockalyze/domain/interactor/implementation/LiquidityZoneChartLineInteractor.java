package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.model.graphics.ChartLine;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.ArrayList;
import java.util.List;

public class LiquidityZoneChartLineInteractor implements ChartLineInteractor {

    private Callback callback;
    private final List<LiquidityZone> data;

    public LiquidityZoneChartLineInteractor(Callback callback, List<LiquidityZone> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        List<ChartLine> lines = new ArrayList<>();

        for(LiquidityZone e : data){
            ChartLine line = new ChartLine();
            line.level = e.price / (double)100;
            line.strength = e.relativeVolume;

            lines.add(line);
        }

        callback.onChartLineCreated(lines);
    }
}
