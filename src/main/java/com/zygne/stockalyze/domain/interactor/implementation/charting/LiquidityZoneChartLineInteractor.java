package com.zygne.stockalyze.domain.interactor.implementation.charting;

import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.graphics.ChartLine;
import com.zygne.stockalyze.domain.model.graphics.ChartObject;

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

        List<LiquidityZone> zones = new ArrayList<>();
        zones.addAll(data);

        List<ChartObject> lines = new ArrayList<>();

        if (zones.size() > 1) {
            LiquidityZone n = zones.get(0);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 3;
            line.color = ChartLine.Color.RED;
            lines.add(line);
        }

        if (zones.size() >= 2) {
            LiquidityZone n = zones.get(1);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 2;
            line.color = ChartLine.Color.ORANGE;
            lines.add(line);
        }

        if (zones.size() >= 3) {
            LiquidityZone n = zones.get(2);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 2;
            line.color = ChartLine.Color.ORANGE;
            lines.add(line);
        }

        if (zones.size() >= 4) {
            LiquidityZone n = zones.get(3);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 1;
            line.color = ChartLine.Color.YELLOW;
            lines.add(line);
        }

        if (zones.size() >= 5) {
            LiquidityZone n = zones.get(4);
            ChartLine line = new ChartLine();
            line.level = n.price / (double) 100;
            line.size = 1;
            line.color = ChartLine.Color.YELLOW;
            lines.add(line);
        }

        if (zones.size() >= 6) {

            for (int i = 5; i < zones.size(); i++) {
                LiquidityZone e = zones.get(i);

                ChartLine line = new ChartLine();
                line.level = e.price / (double) 100;
                line.size = 1;
                line.color = ChartLine.Color.BLUE;

                lines.add(line);

            }

        }

        callback.onChartLineCreated(lines);
    }
}
