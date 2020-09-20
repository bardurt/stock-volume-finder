package com.zygne.stockalyze.domain.interactor.implementation.scripting;

import com.zygne.stockalyze.domain.model.PowerZone;

import java.text.SimpleDateFormat;
import java.util.List;

public class PineScriptZoneInteractor implements ScriptInteractor {

    private static final String EXTENSION = ".pine";

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
    private final ScriptInteractor.Callback callback;
    private final String name;
    private final String ticker;
    private final List<PowerZone> zones;

    public PineScriptZoneInteractor(ScriptInteractor.Callback callback, String name, String ticker, List<PowerZone> zones) {
        this.callback = callback;
        this.name = name;
        this.ticker = ticker;
        this.zones = zones;
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
        stringBuilder.append("\n");

        int count = 1;

        for (PowerZone e : zones) {

            String name1 = "h" + count + "_1";
            String name2 = "h" + count + "_2";

            stringBuilder.append(name1).append("=hline(").append((double) e.end / 100).append(")");
            stringBuilder.append("\n");
            stringBuilder.append(name2).append("=hline(").append((double) e.start / 100).append(")");
            stringBuilder.append("\n");

            stringBuilder.append("fill(").append(name1).append(", ").append(name2).append(", ");

            if (e.type == PowerZone.ACCEPT) {
                stringBuilder.append("color=color.green, ");
            } else {
                stringBuilder.append("color=color.red, ");
            }

            stringBuilder.append("transp=95)");
            stringBuilder.append("\n");

            count++;

        }

        stringBuilder.append("plot(close)");

        callback.onScriptCreated(stringBuilder.toString(), scriptName);
    }

}
