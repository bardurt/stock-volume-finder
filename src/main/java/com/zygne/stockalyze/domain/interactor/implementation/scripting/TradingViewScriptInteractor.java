package com.zygne.stockalyze.domain.interactor.implementation.scripting;

import com.zygne.stockalyze.domain.interactor.implementation.scripting.ScriptInteractor;
import com.zygne.stockalyze.domain.model.Node;

import java.text.SimpleDateFormat;
import java.util.List;

public class TradingViewScriptInteractor implements ScriptInteractor {


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
    private Callback callback;
    private String ticker;
    private List<Node> data;

    public TradingViewScriptInteractor(Callback callback, String ticker, List<Node> data) {
        this.callback = callback;
        this.ticker = ticker;
        this.data = data;
    }

    @Override
    public void execute() {

        String scriptName = "trading_view_supply_zone_" + ticker + ".txt";

        String time = simpleDateFormat.format(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("// Pine Script for Trading View");
        stringBuilder.append("\n");
        stringBuilder.append("// This source code is subject to the terms of the Mozilla Public License 2.0 at https://mozilla.org/MPL/2.0/");
        stringBuilder.append("\n");
        stringBuilder.append("// © Barthur").append(" - ").append(time);
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("//@version=4");
        stringBuilder.append("\n");
        stringBuilder.append("study(\"Liquidity Zones - ").append(ticker).append("\",").append(" overlay=true)");

        double maxStrength = 0;

        for (Node e : data) {
            if (e.strength > maxStrength) {
                maxStrength = e.strength;
            }
        }

        for (Node e : data) {
            String color = "color.aqua";
            int trans = 5;
            int lineWidth = 1;

            if (e.origin && e.pull < 1) {
                continue;
            }

            if (e.strength > (maxStrength * 0.6)) {

                lineWidth = 2;
                color = "color.green";

                if (e.probability > 90) {
                    color = "color.orange";
                }
            }

            if (e.strength >= (maxStrength)) {

                lineWidth = 3;
                color = "color.red";
            }


            trans = 25;


            stringBuilder.append("\n");
            stringBuilder.append("plot(");
            stringBuilder.append((double) e.level / 100);
            stringBuilder.append(", color=");
            stringBuilder.append(color);
            stringBuilder.append(", transp=");
            stringBuilder.append(trans);
            stringBuilder.append(", linewidth=");
            stringBuilder.append(lineWidth);
            stringBuilder.append(")");

        }

        callback.onScriptCreated(stringBuilder.toString(), scriptName);
    }

}