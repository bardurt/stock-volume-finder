package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;
import com.zygne.stockalyze.domain.model.SupplyZone;

import java.util.ArrayList;
import java.util.List;

public class SupplyZoneCreatorInteractor implements Interactor {

    private Callback callback;
    private List<VolumePriceGroup> data;

    public SupplyZoneCreatorInteractor(Callback callback, List<VolumePriceGroup> data){
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        List<SupplyZone> formatted = new ArrayList<>();

        for(VolumePriceGroup e : data){
            SupplyZone s = new SupplyZone(e.price, e.totalSize, e.orderCount);
            formatted.add(s);
        }

        callback.onSupplyZoneCreated(formatted);
    }

    public interface Callback{
        void onSupplyZoneCreated(List<SupplyZone> data);
    }
}
