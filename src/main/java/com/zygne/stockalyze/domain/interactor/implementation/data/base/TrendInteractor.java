package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.Trend;

import java.util.List;

public interface TrendInteractor extends Interactor {

    interface Callback{
        void onTrendCalculated(Trend trend, List<Histogram> data);
    }
}
