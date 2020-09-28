package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquidityZoneFilterInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiquidityZoneFilterInteractorImpl implements LiquidityZoneFilterInteractor {

    private final Callback callback;
    private final List<LiquidityZone> data;
    private final Statistics statistics;

    public LiquidityZoneFilterInteractorImpl(Callback callback, List<LiquidityZone> data, Statistics statistics) {
        this.callback = callback;
        this.data = data;
        this.statistics = statistics;
    }

    @Override
    public void execute() {

        double limit = 40;

        List<LiquidityZone> filtered = new ArrayList<>();
        data.sort(new LiquidityZone.PriceComparator());
        Collections.reverse(data);

        filtered.add(data.get(0));

        data.sort(new LiquidityZone.VolumeComparator());
        Collections.reverse(data);

        for(int i = 1; i < data.size(); i++){
            LiquidityZone e = data.get(i);

            if(e.percentile < limit){
                filtered.add(e);
            }
        }

        filtered.sort(new LiquidityZone.PriceComparator());
        Collections.reverse(filtered);

        callback.onLiquidityZonesFiltered(filtered);
    }
}
