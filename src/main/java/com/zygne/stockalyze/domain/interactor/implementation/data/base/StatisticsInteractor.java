package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Statistics;

public interface StatisticsInteractor extends Interactor {

    interface Callback{
        void onStatisticsCalculated(Statistics statistics);
    }
}
