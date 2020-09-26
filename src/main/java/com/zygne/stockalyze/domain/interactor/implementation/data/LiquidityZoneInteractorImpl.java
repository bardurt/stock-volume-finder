package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquidityZoneInteractor;
import com.zygne.stockalyze.domain.model.Statistics;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiquidityZoneInteractorImpl implements LiquidityZoneInteractor {

    private final Callback callback;
    private final List<VolumePriceGroup> data;
    private final Statistics statistics;

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
            s.volumePercentage = (e.totalSize / (double) statistics.cumulativeVolume) * 100;
            formatted.add(s);
        }

        formatted.sort(new LiquidityZone.VolumeComparator());
        Collections.reverse(formatted);

        for(int i = 0; i < formatted.size(); i++){
            formatted.get(i).rank = i+1;
        }

        callback.onLiquidityZonesCreated(formatted);
    }
}
