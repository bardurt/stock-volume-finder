package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquidityZoneFilterInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.ArrayList;
import java.util.List;

public class LiquidityZoneFilterInteractorImpl implements LiquidityZoneFilterInteractor {

    private Callback callback;
    private List<LiquidityZone> data;
    private double mean;
    private double sd;

    public LiquidityZoneFilterInteractorImpl(Callback callback, List<LiquidityZone> data, double mean, double sd) {
        this.callback = callback;
        this.data = data;
        this.mean = mean;
        this.sd = sd;
    }

    @Override
    public void execute() {

        double limit = 3;

        List<LiquidityZone> filtered = new ArrayList<>();

        for(LiquidityZone e : data){
            if(e.relativeVolume > limit){
                filtered.add(e);
            }
        }

        callback.onLiquidityZonesFiltered(filtered);
    }
}
