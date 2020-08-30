package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.interactor.implementation.data.GapRateInteractorImpl;
import com.zygne.stockalyze.domain.interactor.implementation.data.NodeInteractorImpl;
import com.zygne.stockalyze.domain.interactor.implementation.data.VolumePriceInteractorImpl;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.GapRateInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.NodeInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.TrendInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.*;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.DragInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.GapBiasInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.PointInteractor;
import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;

import java.util.List;

public class PredictionPresenterImpl implements PredictionPresenter, NodeInteractor.Callback,
        PredictionInteractor.Callback,
        ProbabilityInteractor.Callback,
        PointInteractor.Callback,
        DragInteractor.Callback,
        TrendBiasInteractor.Callback,
        NewsBiasInteractor.Callback,
        GapBiasInteractor.Callback,
        StrongestPullBiasInteractor.Callback {

    private GapDetails gapDetails;
    private int currentPrice;
    private String ticker;
    private List<LiquidityZone> liquidityZoneList;

    public PredictionPresenterImpl(String ticker, List<LiquidityZone> liquidityZoneList, GapDetails gapDetails, int currentPrice) {
        this.gapDetails = gapDetails;
        this.currentPrice = currentPrice;
        this.ticker = ticker;
        this.liquidityZoneList = liquidityZoneList;
    }

    @Override
    public void start() {
        new NodeInteractorImpl(this, liquidityZoneList, currentPrice).execute();
    }

    @Override
    public void onNodesCreated(List<Node> data) {
        new PointInteractorImpl(this, data).execute();
    }


    @Override
    public void onNewsBiasCreated(List<Node> data) {
        new GapUpBiasInteractor(this, data, gapDetails).execute();
    }

    @Override
    public void onPredictionComplete(List<Node> data) {
        printNodes(data);
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

    private void printNodes(List<Node> data) {
        System.out.println("Prediction : " + ticker);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-12s\n", "Price", "Change", "Prediction");
        System.out.println("-----------------------------------------------------------------------------");
        for (Node e : data) {
            System.out.printf("%-16s%-12.2f%-12.2f%-12s\n", e.level, e.change, e.prediction, e.note);
        }
        System.out.println("-----------------------------------------------------------------------------");

    }
}
