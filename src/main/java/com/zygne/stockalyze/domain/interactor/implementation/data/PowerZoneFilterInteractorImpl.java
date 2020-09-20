package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.PowerZoneFilterInteractor;
import com.zygne.stockalyze.domain.model.PowerZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowerZoneFilterInteractorImpl implements PowerZoneFilterInteractor {

    private Callback callback;
    private List<PowerZone> data;
    private int currentPrice = 0;

    public PowerZoneFilterInteractorImpl(Callback callback, List<PowerZone> data, int currentPrice ) {
        this.callback = callback;
        this.data = data;
        this.currentPrice = currentPrice;
    }

    @Override
    public void execute() {

        data.sort(new PowerZone.TimeComparator());
        Collections.reverse(data);

        List<PowerZone> filteredList = new ArrayList<>();

        int count = 0;
        for(PowerZone e : data){

            if(currentPrice > e.end){
                if(currentPrice / (double) e.end < 1.1){
                    filteredList.add(e);
                }
            } else if (currentPrice < e.start){
                if(e.start / (double) currentPrice < 1.1){
                    filteredList.add(e);
                }
            } else {
                if(currentPrice <= e.end && currentPrice >= e.start){
                    filteredList.add(e);
                }
            }
        }

        callback.onPowerZoneFiltered(filteredList);

    }

}
