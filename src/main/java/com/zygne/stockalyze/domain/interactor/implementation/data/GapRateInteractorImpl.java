package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.GapRateInteractor;
import com.zygne.stockalyze.domain.model.GapDetails;
import com.zygne.stockalyze.domain.model.Histogram;

import java.util.List;

public class GapRateInteractorImpl implements GapRateInteractor {

    private Callback callback;
    private List<Histogram> data;
    private int currentPrice;

    public GapRateInteractorImpl(Callback callback, List<Histogram> data, int currentPrice) {
        this.callback = callback;
        this.data = data;
        this.currentPrice = currentPrice;
    }

    @Override
    public void execute() {

        double currentGap;

        GapDetails gapDetails = new GapDetails();

        if(!data.isEmpty()){

            int lastClose = data.get(data.size()-1).close;

            currentGap = currentPrice / (double)lastClose;
            gapDetails.currentGap = currentGap;
        }


        int numberOfGaps = 0;
        int bullGaps = 0;
        int gap10 = 0;
        int gap20 = 0;

        for(int i = 1; i < data.size(); i++){

            if(data.get(i-1).close < data.get(i).open){
                int lastClose = data.get(i-1).close;
                int currentOpen = data.get(i).open;
                int currentHigh = data.get(i).high;
                int currentClose = data.get(i).close;

                double gapUp = currentOpen / (double)lastClose;
                double gapHigh = currentHigh / (double) currentOpen;

                if(gapUp >= GapDetails.minGap){
                    numberOfGaps++;

                    if(currentClose > currentOpen){
                        bullGaps++;
                    }
                    if(gapHigh > 1.20){
                        gap20++;
                    }
                    if(gapHigh > 1.1){
                        gap10++;
                    }
                }

            }
        }

        if(numberOfGaps > 0) {
            gapDetails.gapCount = numberOfGaps;
            gapDetails.gapBull = bullGaps / (double) numberOfGaps;
            gapDetails.gap10 = gap10 / (double) numberOfGaps;
            gapDetails.gap20 = gap20 / (double) numberOfGaps;

        }

        callback.onGapRateCalculated(gapDetails, data);

    }
}
