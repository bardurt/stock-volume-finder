package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.model.SupplyZone;

import java.text.SimpleDateFormat;
import java.util.List;

public class TradingViewScriptInteractor implements ScriptInteractor {


    private SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
    private Callback callback;
    private String ticker;
    private List<SupplyZone> data;

    public TradingViewScriptInteractor(Callback callback, String ticker, List<SupplyZone> data){
        this.callback = callback;
        this.ticker = ticker;
        this.data = data;
    }

    @Override
    public void execute() {

        String time = simpleDateFormat.format(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("// Pine Script for Trading View");
        stringBuilder.append("\n");
        stringBuilder.append("// This source code is subject to the terms of the Mozilla Public License 2.0 at https://mozilla.org/MPL/2.0/");
        stringBuilder.append("\n");
        stringBuilder.append("// © Barthur").append(" - ").append(time);
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("study(\"Supply Zones - " + ticker+ "\", overlay=true)");
        for (SupplyZone e : data) {
            String color = "blue";
            String title = "low";
            String trans = "5";

            if(e.relativeVolume < 2){
                color = "orange";
                title = "low";
                trans = "50";
            }
            if(e.relativeVolume > 2){
                color = "teal";
                title = "medium";
                trans = "30";
            }
            if(e.relativeVolume > 2.5){
                color = "blue";
                title = "high";
                trans = "10";
            }
            if(e.relativeVolume > 4){
                color = "green";
                title = "very high";
            }

            stringBuilder.append("\n");
            stringBuilder.append("plot(" + (double) e.price / 100 + ", color=" + color +", transp=" + trans + ", title=\"" + title + "\")");

        }

        callback.onScriptCreated(stringBuilder.toString());
    }

}
