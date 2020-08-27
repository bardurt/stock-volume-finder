package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.GapCheckInteractor;
import com.zygne.stockalyze.domain.model.Histogram;

import java.util.List;

public class GapCheckInteractorImpl implements GapCheckInteractor {

    private Callback callback;
    private List<Histogram> data;
    private int currentPrice;

    public GapCheckInteractorImpl(Callback callback, List<Histogram> data, int currentPrice) {
        this.callback = callback;
        this.data = data;
        this.currentPrice = currentPrice;
    }

    @Override
    public void execute() {
        boolean gapUp = false;

        if(!data.isEmpty()){

            int lastClose = data.get(data.size()-1).close;

            double diff = currentPrice / lastClose;

            if(diff > 1.2){
                gapUp = true;
            }

        }

        callback.onGapValidated(gapUp);
    }
}
