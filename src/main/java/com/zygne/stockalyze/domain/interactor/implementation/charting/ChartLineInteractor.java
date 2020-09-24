package com.zygne.stockalyze.domain.interactor.implementation.charting;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.graphics.ChartLine;
import com.zygne.stockalyze.domain.model.graphics.ChartObject;

import java.util.List;

public interface ChartLineInteractor extends Interactor {

    interface Callback{
        void onChartLineCreated(List<ChartObject> lines);
    }
}
