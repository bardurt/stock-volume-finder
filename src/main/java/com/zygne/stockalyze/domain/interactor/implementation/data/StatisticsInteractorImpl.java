package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.StatisticsInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.Statistics;

import java.util.List;

public class StatisticsInteractorImpl implements StatisticsInteractor {

    private final Callback callback;
    private final List<Histogram> data;

    public StatisticsInteractorImpl(Callback callback, List<Histogram> data){
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        Statistics statistics = new Statistics();

        long totalShares = 0;
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

        statistics.sampleSize = sampleSize;
        statistics.mean = mean;
        statistics.variance = variance;
        statistics.standardDeviation = standardDeviation;
        statistics.cumulativeVolume = totalShares;

        callback.onStatisticsCalculated(statistics);
    }
}
