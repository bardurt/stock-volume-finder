package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquidityZoneInteractor;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.ArrayList;
import java.util.List;

public class LiquidityZoneInteractorImpl implements LiquidityZoneInteractor {

    private Callback callback;
    private List<VolumePriceGroup> data;
    private double mean;

    public LiquidityZoneInteractorImpl(Callback callback, List<VolumePriceGroup> data, double mean){
        this.callback = callback;
        this.data = data;
        this.mean = mean;
    }

    @Override
    public void execute() {

        List<LiquidityZone> formatted = new ArrayList<>();

        for(VolumePriceGroup e : data){
            LiquidityZone s = new LiquidityZone(e.price, e.totalSize, e.orderCount);
            s.relativeVolume = e.totalSize / mean;
            formatted.add(s);
        }

        callback.onLiquidityZonesCreated(formatted);
    }
}
