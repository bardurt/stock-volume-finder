package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.PowerZoneInteractor;
import com.zygne.stockalyze.domain.model.CandleStick;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.PowerZone;

import java.util.ArrayList;
import java.util.List;

public class PowerZoneInteractorImpl implements PowerZoneInteractor {

    private final Callback callback;
    private final List<Histogram> data;

    public PowerZoneInteractorImpl(Callback callback, List<Histogram> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        List<CandleStick> sticks = new ArrayList<>();

        for (Histogram e : data) {
            sticks.add(new CandleStick(e));
        }

        List<PowerZone> zones = new ArrayList<>();

        for (CandleStick e : sticks) {

            if (e.upperWick / (double) e.bodySize > 0.6) {

                PowerZone p = new PowerZone();
                p.start = e.bodyTop;
                p.end = e.top;
                p.type = PowerZone.RECJECT;
                p.timeStamp = e.timeStamp;
                zones.add(p);

            }

            if (e.lowerWick / (double) e.bodySize > 0.6) {

                PowerZone p = new PowerZone();
                p.start = e.bottom;
                p.end = e.bodyBottom;
                p.type = PowerZone.ACCEPT;
                p.timeStamp = e.timeStamp;
                zones.add(p);
            }
        }

        callback.onPowerZonesCreated(zones);
    }
}
