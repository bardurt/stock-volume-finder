package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.TopLiquidityZonesInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopLiquidityZonesInteractorImpl implements TopLiquidityZonesInteractor {

    private final Callback callback;
    private final List<LiquidityZone> data;
    private final int maxAmount;

    public TopLiquidityZonesInteractorImpl(Callback callback, List<LiquidityZone> data, int maxAmount) {
        this.callback = callback;
        this.data = data;
        this.maxAmount = maxAmount;
    }

    @Override
    public void execute() {

        List<LiquidityZone> zones = new ArrayList<>(data);

        zones.sort(new LiquidityZone.VolumeComparator());

        Collections.reverse(zones);

        List<LiquidityZone> filteredList = new ArrayList<>();

        double currentPercent = 0;

        for(LiquidityZone e : zones){
            filteredList.add(e);
            currentPercent += e.volumePercentage;
            if (currentPercent > maxAmount){
                break;
            }
        }

        filteredList.sort(new LiquidityZone.RankComparator());

        callback.onTopLiquidityZonesFound(filteredList);

    }
}
