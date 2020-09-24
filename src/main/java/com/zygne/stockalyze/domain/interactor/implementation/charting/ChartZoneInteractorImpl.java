package com.zygne.stockalyze.domain.interactor.implementation.charting;

import com.zygne.stockalyze.domain.model.PowerZone;
import com.zygne.stockalyze.domain.model.graphics.ChartObject;
import com.zygne.stockalyze.domain.model.graphics.ChartZone;

import java.util.ArrayList;
import java.util.List;

public class ChartZoneInteractorImpl implements ChartZoneInteractor {

    private final Callback callback;
    private final List<PowerZone> data;

    public ChartZoneInteractorImpl(Callback callback, List<PowerZone> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        List<ChartObject> zones = new ArrayList<>();

        for(PowerZone powerZone : data){
            ChartZone zone = new ChartZone();

            if(powerZone.type == PowerZone.RECJECT){
                zone.color = ChartObject.Color.RED;
            } else {
                zone.color = ChartObject.Color.GREEN;
            }

            zone.top = (double) powerZone.end / 100;
            zone.bottom = (double) powerZone.start / 100;
            zone.transparency = 95;

            zones.add(zone);
        }


        callback.onChartZoneCreated(zones);

    }
}
