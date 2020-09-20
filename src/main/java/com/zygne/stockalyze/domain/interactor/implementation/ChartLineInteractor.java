package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.graphics.ChartLine;

import java.util.List;

public interface ChartLineInteractor extends Interactor {

    interface Callback{
        void onChartLineCreated(List<ChartLine> lines);
    }
}
