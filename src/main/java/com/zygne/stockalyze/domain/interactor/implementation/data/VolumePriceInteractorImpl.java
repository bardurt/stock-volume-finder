package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.VolumePriceLevel;

import java.util.ArrayList;
import java.util.List;

public class VolumePriceInteractorImpl implements VolumePriceInteractor {

    private final Callback callback;
    private final List<Histogram> histogramList;

    public VolumePriceInteractorImpl(Callback callback, List<Histogram> histogramList) {
        this.callback = callback;
        this.histogramList = histogramList;
    }

    @Override
    public void execute() {

        List<VolumePriceLevel> data = new ArrayList<>();

        for(Histogram e : histogramList){

            long volume = (long) (e.volume * e.decay);
            VolumePriceLevel vp1 = new VolumePriceLevel(e.open, (long) (volume * 0.2));
            VolumePriceLevel vp2 = new VolumePriceLevel(e.high, (long) (volume * 0.2));
            VolumePriceLevel vp3 = new VolumePriceLevel(e.low, (long) (volume * 0.2));
            VolumePriceLevel vp4 = new VolumePriceLevel(e.close, (long) (volume * 0.2));

            int middle = (e.high - e.low) / 2;
            VolumePriceLevel vp5 = new VolumePriceLevel(middle, (long) (volume * 0.2));

            data.add(vp1);
            data.add(vp2);
            data.add(vp3);
            data.add(vp4);
            data.add(vp5);
        }


        callback.onVolumePriceCreated(data);

    }
}
