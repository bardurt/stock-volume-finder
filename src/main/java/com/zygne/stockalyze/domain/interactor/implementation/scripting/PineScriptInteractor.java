package com.zygne.stockalyze.domain.interactor.implementation.scripting;

import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.text.SimpleDateFormat;
import java.util.List;

public class PineScriptInteractor implements ScriptInteractor {

    private static final String EXTENSION = ".pine";

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
    private final Callback callback;
    private final String ticker;
    private final List<LiquidityZone> data;

    public PineScriptInteractor(Callback callback, String ticker, List<LiquidityZone> data) {
        this.callback = callback;
        this.ticker = ticker;
        this.data = data;
    }

    @Override
    public void execute() {

        String scriptName = "liquidity_zones_" + ticker + EXTENSION;

        String time = simpleDateFormat.format(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("//@version=4");
        stringBuilder.append("\n");
        stringBuilder.append("// Liquidity zones for : ").append(ticker);
        stringBuilder.append("\n");
        stringBuilder.append("// Author : Barthur").append(" - ").append(time);
        stringBuilder.append("\n");
        stringBuilder.append("study(\"Liquidity Zones - ").append(ticker).append("\",").append(" overlay=true)");

        double maxStrength = 0;

        for (LiquidityZone e : data) {
            if (e.relativeVolume > maxStrength) {
                maxStrength = e.relativeVolume;
            }
        }

        for (LiquidityZone e : data) {
            String color = "color.aqua";
            int trans = 25;
            int lineWidth = 1;

            if (e.relativeVolume > (maxStrength * 0.7)) {
                lineWidth = 2;
                color = "color.yellow";
            }

            if (e.relativeVolume > (maxStrength * 0.9)) {
                lineWidth = 2;
                color = "color.orange";
            }

            if (e.relativeVolume >= (maxStrength)) {
                lineWidth = 3;
                color = "color.red";
            }

            stringBuilder.append("\n");
            stringBuilder.append("plot(");
            stringBuilder.append((double) e.price / 100);
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
