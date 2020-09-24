package com.zygne.stockalyze.domain.interactor.implementation.charting;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.graphics.ChartObject;

import java.util.List;

public interface ChartZoneInteractor extends Interactor {

    public interface Callback{
        void onChartZoneCreated(List<ChartObject> zones);
    }
}
