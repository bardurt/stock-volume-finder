package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.TopLiquidityZonesInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopLiquidityZonesInteractorImpl implements TopLiquidityZonesInteractor {

    private Callback callback;
    private List<LiquidityZone> data;
    private int maxAmount;

    public TopLiquidityZonesInteractorImpl(Callback callback, List<LiquidityZone> data, int maxAmount) {
        this.callback = callback;
        this.data = data;
        this.maxAmount = maxAmount;
    }

    @Override
    public void execute() {

        List<LiquidityZone> zones = new ArrayList<>(data);

        Collections.sort(zones, new LiquidityZone.VolumeComparator());
        Collections.reverse(zones);

        List<LiquidityZone> filteredList = new ArrayList<>();

        int count = 0;

        for(LiquidityZone e : zones){
            filteredList.add(e);
            count++;
            if (count > maxAmount){
                break;
            }
        }

        callback.onTopLiquidityZonesFound(filteredList);

    }
}
