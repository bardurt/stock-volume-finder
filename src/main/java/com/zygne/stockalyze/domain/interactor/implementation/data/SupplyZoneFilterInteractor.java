package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.SupplyZone;

import java.util.ArrayList;
import java.util.List;

public class SupplyZoneFilterInteractor implements Interactor {

    private Callback callback;
    private List<SupplyZone> data;
    private double mean;
    private double sd;

    public SupplyZoneFilterInteractor(Callback callback, List<SupplyZone> data, double mean, double sd) {
        this.callback = callback;
        this.data = data;
        this.mean = mean;
        this.sd = sd;
    }

    @Override
    public void execute() {

        int limit = (int) (mean + sd);

        List<SupplyZone> filtered = new ArrayList<>();

        for(SupplyZone e : data){
            if(e.totalSize > limit){
                e.relativeVolume = (e.totalSize / mean);
                filtered.add(e);
            }
        }

        callback.onSupplyZoneFiltered(filtered);
    }

    public interface Callback {
        void onSupplyZoneFiltered(List<SupplyZone> data);
    }
}
