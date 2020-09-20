package com.zygne.stockalyze.domain.interactor.implementation.scripting;

import com.zygne.stockalyze.domain.model.graphics.ChartLine;

import java.text.SimpleDateFormat;
import java.util.List;

public class PineScriptLineInteractor implements ScriptInteractor {

    private static final String EXTENSION = ".pine";

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
    private final Callback callback;
    private final String name;
    private final String ticker;
    private final List<ChartLine> data;

    public PineScriptLineInteractor(Callback callback, String name, String ticker, List<ChartLine> data) {
        this.callback = callback;
        this.name = name;
        this.ticker = ticker;
        this.data = data;
    }

    @Override
    public void execute() {

        String scriptName = name + "_" + ticker + EXTENSION;

        String time = simpleDateFormat.format(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("//@version=4");
        stringBuilder.append("\n");
        stringBuilder.append("// Ticker : ").append(ticker);
        stringBuilder.append("\n");
        stringBuilder.append("// Author : Barthur").append(" - ").append(time);
        stringBuilder.append("\n");
        stringBuilder.append("study(\"").append(name).append(" -  ").append(ticker).append("\",").append(" overlay=true)");

        double maxStrength = 0;

        for (ChartLine e : data) {
            if (e.strength > maxStrength) {
                maxStrength = e.strength;
            }
        }

        for (ChartLine e : data) {
            String color = "color.aqua";
            int trans = 25;
            int lineWidth = 1;

            if (e.strength > (maxStrength * 0.5)) {
                lineWidth = 2;
                color = "color.yellow";
            }

            if (e.strength > (maxStrength * 0.7)) {
                lineWidth = 2;
                color = "color.orange";
            }

            if (e.strength >= (maxStrength)) {
                lineWidth = 3;
                color = "color.red";
            }

            stringBuilder.append("\n");
            stringBuilder.append("hline(");
            stringBuilder.append(e.level);
            stringBuilder.append(", color=");
            stringBuilder.append(color);
            stringBuilder.append(", linewidth=");
            stringBuilder.append(lineWidth);
            stringBuilder.append(", linestyle=hline.style_solid");
            stringBuilder.append(")");

        }
        stringBuilder.append("\n");

        stringBuilder.append("plot(close)");

        callback.onScriptCreated(stringBuilder.toString(), scriptName);
    }

}
