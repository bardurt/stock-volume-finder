package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquidityZoneInteractor;
import com.zygne.stockalyze.domain.model.Statistics;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.ArrayList;
import java.util.List;

public class LiquidityZoneInteractorImpl implements LiquidityZoneInteractor {

    private Callback callback;
    private List<VolumePriceGroup> data;
    private Statistics statistics;

    public LiquidityZoneInteractorImpl(Callback callback, List<VolumePriceGroup> data, Statistics statistics){
        this.callback = callback;
        this.data = data;
        this.statistics = statistics;
    }

    @Override
    public void execute() {

        List<LiquidityZone> formatted = new ArrayList<>();

        for(VolumePriceGroup e : data){
            LiquidityZone s = new LiquidityZone(e.price, e.totalSize, e.orderCount);
            s.relativeVolume = e.totalSize / statistics.mean;
            formatted.add(s);
        }

        callback.onLiquidityZonesCreated(formatted);
    }
}
