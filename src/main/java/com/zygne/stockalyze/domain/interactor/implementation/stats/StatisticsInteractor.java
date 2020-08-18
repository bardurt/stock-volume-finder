package com.zygne.stockalyze.domain.interactor.implementation.stats;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Histogram;

import java.util.List;

public class StatisticsInteractor implements Interactor {

    private Callback callback;
    private List<Histogram> data;

    public StatisticsInteractor(Callback callback, List<Histogram> data){
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        int totalShares = 0;
        int sampleSize = data.size();

        for(Histogram e : data){
            totalShares += e.volume;
        }

        double mean = totalShares / sampleSize;

        int variance = 0;

        for(Histogram e : data){
            variance += (Math.pow((e.volume - mean), 2)/sampleSize);
        }

        double standardDeviation = Math.sqrt(variance);

        callback.onStatisticsCalculated(data, mean, standardDeviation);
    }

    public interface Callback{
        void onStatisticsCalculated(List<Histogram> data, double mean, double standardDeviation);
    }
}
