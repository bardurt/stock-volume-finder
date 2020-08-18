package com.zygne.stockalyze.domain.interactor.implementation.stats;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Score;
import com.zygne.stockalyze.domain.model.StockState;
import com.zygne.stockalyze.domain.model.SupplyZone;

import java.util.List;

public class ScoreInteractor implements Interactor {

    private final Callback callback;
    private final List<SupplyZone> data;
    private final StockState stockState;

    public ScoreInteractor(Callback callback, List<SupplyZone> data, StockState stockState) {
        this.callback = callback;
        this.data = data;
        this.stockState = stockState;
    }

    @Override
    public void execute() {
        Score score = new Score();

        stockState.normalize();

        if(stockState.significantVolume() > (stockState.avgVolume() / 10)){
            score.addPoint();
        }

        if(stockState.news() == StockState.News.Significant){
            score.addPoint();
            score.addPoint();
        } else if(stockState.news() == StockState.News.Medium){
            score.addPoint();
        }

        if(stockState.trend() == StockState.Trend.Up){
            score.addPoint();
            score.addPoint();
        }

        if(stockState.runner() > 0){
            score.addPoint();
            score.addPoint();
        }

        if(stockState.gap() > 50){
            score.addPoint();
            score.addPoint();
        } else if(stockState.gap() > 30){
            score.addPoint();
        }

        score.compute();
        callback.onScoreCalculated(data, score);
    }

    public interface Callback {
        void onScoreCalculated(List<SupplyZone> data, Score score);
    }
}
