package com.zygne.stockalyze.domain.interactor.implementation.charting;

import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.domain.model.graphics.ChartLine;
import com.zygne.stockalyze.domain.model.graphics.ChartObject;

import java.util.ArrayList;
import java.util.Collections;
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

        List<Node> nodes = new ArrayList<>();

        for(Node n : data){
            if(n.prediction > 80){
                nodes.add(n);
            }
        }

        nodes.sort(new Node.StrengthComparator());
        Collections.reverse(nodes);

        List<ChartObject> lines = new ArrayList<>();

        if (nodes.size() >= 1) {
            Node n = nodes.get(0);
            ChartLine line = new ChartLine();
            line.level = n.level / (double) 100;
            line.size = 3;
            line.color = ChartLine.Color.RED;
            lines.add(line);

        }

        if (nodes.size() >= 2) {
            Node n = nodes.get(1);
            ChartLine line = new ChartLine();
            line.level = n.level / (double) 100;
            line.size = 2;
            line.color = ChartLine.Color.ORANGE;
            lines.add(line);

        }

        if (nodes.size() >= 3) {
            Node n = nodes.get(2);
            ChartLine line = new ChartLine();
            line.level = n.level / (double) 100;
            line.size = 2;
            line.color = ChartLine.Color.ORANGE;
            lines.add(line);

        }

        if (nodes.size() >= 4) {
            Node n = nodes.get(3);
            ChartLine line = new ChartLine();
            line.level = n.level / (double) 100;
            line.size = 2;
            line.color = ChartLine.Color.ORANGE;
            lines.add(line);

        }

        if (nodes.size() >= 5) {
            Node n = nodes.get(4);
            ChartLine line = new ChartLine();
            line.level = n.level / (double) 100;
            line.size = 1;
            line.color = ChartLine.Color.YELLOW;
            lines.add(line);

        }

        if (nodes.size() >= 6) {

            for (int i = 6; i < nodes.size(); i++) {
                Node e = nodes.get(i);

                if (!e.origin) {
                    ChartLine line = new ChartLine();
                    line.level = e.level / (double) 100;
                    line.size = 1;
                    line.color = ChartLine.Color.BLUE;

                    lines.add(line);
                }
            }

        }

        callback.onChartLineCreated(lines);
    }
}
