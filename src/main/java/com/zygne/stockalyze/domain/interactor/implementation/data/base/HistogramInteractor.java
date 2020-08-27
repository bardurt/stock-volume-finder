package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Histogram;

import java.util.List;

public interface HistogramInteractor extends Interactor {

    interface Callback{
        void onHistogramCreated(List<Histogram> data, int months);
    }
}
