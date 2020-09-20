package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.model.graphics.ChartLine;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeChartLineInteractor implements ChartLineInteractor {

    private Callback callback;
    private List<Node> data;

    public NodeChartLineInteractor(Callback callback, List<Node> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        List<ChartLine> lines = new ArrayList<>();

        for (Node e : data) {
            if (!e.origin) {
                if (e.prediction > 75) {
                    ChartLine line = new ChartLine();
                    line.level = e.level / (double) 100;
                    line.strength = e.strength;

                    lines.add(line);
                }
            }
        }

        callback.onChartLineCreated(lines);
    }
}
