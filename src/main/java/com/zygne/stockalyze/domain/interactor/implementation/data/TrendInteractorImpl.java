package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.TrendInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.TimeFrame;
import com.zygne.stockalyze.domain.model.Trend;

import java.util.List;

public class TrendInteractorImpl implements TrendInteractor {

    private Callback callback;
    private List<Histogram> data;

    public TrendInteractorImpl(Callback callback, List<Histogram> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {
        if(data.isEmpty()){
            callback.onTrendCalculated(Trend.Consolidation, data);
            return;
        }

        TimeFrame timeFrame = data.get(0).timeFrame;

        Trend trend = Trend.Consolidation;

        if(timeFrame == TimeFrame.Day){

            Histogram day1 = data.get(data.size()-6);
            Histogram day2 = data.get(data.size()-1);

            if(day1.close >  day2.close){
                trend = Trend.Down;
            } if(day1.close < day2.close){
                trend = Trend.Up;
            }

        }

        callback.onTrendCalculated(trend,data);
    }
}
