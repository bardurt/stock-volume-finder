package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.GapDetails;
import com.zygne.stockalyze.domain.model.Histogram;

import java.util.List;

public interface GapRateInteractor extends Interactor {

    interface Callback{
        void onGapRateCalculated(GapDetails gapDetails, List<Histogram> data);
    }
}
