package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.VolumePriceLevel;

import java.util.ArrayList;
import java.util.List;

public class VolumePriceLevelCreatorInteractor implements Interactor {

    private Callback callback;
    private List<Histogram> histogramList;

    public VolumePriceLevelCreatorInteractor(Callback callback, List<Histogram> histogramList) {
        this.callback = callback;
        this.histogramList = histogramList;
    }

    @Override
    public void execute() {

        List<VolumePriceLevel> data = new ArrayList<>();

        for(Histogram e : histogramList){
            VolumePriceLevel vp1 = new VolumePriceLevel(e.open, e.volume / 4);
            VolumePriceLevel vp2 = new VolumePriceLevel(e.high, e.volume / 4);
            VolumePriceLevel vp3 = new VolumePriceLevel(e.low, e.volume / 4);
            VolumePriceLevel vp4 = new VolumePriceLevel(e.close, e.volume / 4);

            data.add(vp1);
            data.add(vp2);
            data.add(vp3);
            data.add(vp4);
        }


        callback.onVolumePriceCreated(data);

    }

    public interface Callback {
        void onVolumePriceCreated(List<VolumePriceLevel> data);
    }
}
