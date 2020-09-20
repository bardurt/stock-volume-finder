package com.zygne.stockalyze.domain.interactor.implementation.scripting;

import java.text.SimpleDateFormat;

public class PineScriptEmaInteractor implements ScriptInteractor {

    private static final String EXTENSION = ".pine";

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
    private final ScriptInteractor.Callback callback;
    private final String name;
    private final String ticker;

    public PineScriptEmaInteractor(Callback callback, String name, String ticker) {
        this.callback = callback;
        this.name = name;
        this.ticker = ticker;
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
        stringBuilder.append("\n");
        stringBuilder.append("sEma9 = ema(close, 9)");
        stringBuilder.append("\n");
        stringBuilder.append("sEma20 = ema(close, 20)");
        stringBuilder.append("\n");
        stringBuilder.append("sSma200 = sma(close, 200)");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("plot(sEma9, title=\"Ema 9\", color = color.orange, linewidth = 1, transp=0)");
        stringBuilder.append("\n");
        stringBuilder.append("plot(sEma20, title=\"Ema 20\", color = color.blue, linewidth = 1, transp=0)");
        stringBuilder.append("\n");
        stringBuilder.append("plot(sSma200, title=\"Sma 200\", color = color.green, linewidth = 2, transp=0)");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("longCond = crossover(close, sEma9) and (close > sEma9)");
        stringBuilder.append("\n");
        stringBuilder.append("shortCond = crossunder(close, sEma9) and (close < sEma9)");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("plotshape(series=longCond, title=\"Long\", style=shape.triangleup, location=location.belowbar, color=color.green, text=\"BUY\", size=size.small)");
        stringBuilder.append("\n");
        stringBuilder.append("plotshape(series=shortCond, title=\"Short\", style=shape.triangledown, location=location.abovebar, color=color.red, text=\"SELL\", size=size.small)");

    }
}
