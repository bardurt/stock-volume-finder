package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.interactor.implementation.data.NodeInteractorImpl;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.NodeInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.*;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.DragInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.GapBiasInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.PointInteractor;
import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.model.enums.MarketTime;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;

import java.util.List;

public class PredictionPresenterImpl implements PredictionPresenter, NodeInteractor.Callback,
        PredictionInteractor.Callback,
        ProbabilityInteractor.Callback,
        ChangeInteractor.Callback,
        PointInteractor.Callback,
        DragInteractor.Callback,
        TrendBiasInteractor.Callback,
        NewsBiasInteractor.Callback,
        GapBiasInteractor.Callback,
        StrongestPullBiasInteractor.Callback {

    private final PredictionPresenter.View view;

    private final MarketTime marketTime;
    private final GapDetails gapDetails;
    private final int currentPrice;
    private final String ticker;
    private final List<LiquidityZone> liquidityZoneList;

    public PredictionPresenterImpl(View view, DataReport dataReport){
        this.view = view;
        this.marketTime = dataReport.marketTime;
        this.gapDetails = dataReport.gapDetails;
        this.currentPrice = dataReport.openPrice;
        this.ticker = dataReport.ticker;
        this.liquidityZoneList = dataReport.zones;
    }

    @Override
    public void start() {
        new NodeInteractorImpl(this, liquidityZoneList, currentPrice).execute();
    }

    @Override
    public void onNodesCreated(List<Node> data) {
        new ChangeInteractor(this, data).execute();
    }

    @Override
    public void onNewsBiasCreated(List<Node> data) {
        if (marketTime == MarketTime.MARKET_OPEN || marketTime == MarketTime.PRE_MARKET) {
            new GapUpBiasInteractor(this, data, gapDetails).execute();
        } else {
            new PredictionInteractor(this, data).execute();
        }
    }

    @Override
    public void onPredictionComplete(List<Node> data) {
        view.onPredictionCompleted(data);
    }

    @Override
    public void onProbabilityCreated(List<Node> data) {

    }

    @Override
    public void onStrongestPullBiasCreated(List<Node> data) {
        new TrendBiasInteractor(this, data, 0).execute();
    }

    @Override
    public void onTrendBiasCreated(List<Node> data) {
        new NewsBiasInteractor(this, data, 0).execute();
    }

    @Override
    public void onDragCreated(List<Node> data) {
        new StrongestPullBiasInteractor(this, data).execute();
    }

    @Override
    public void onGapBiasCreated(List<Node> data) {
        new PredictionInteractor(this, data).execute();
    }

    @Override
    public void onPointsCreated(List<Node> data) {
        new DragInteractorImpl(this, data).execute();
    }

    @Override
    public void onChangeCalculated(List<Node> data) {
        new PointInteractorImpl(this, data).execute();
    }
}
