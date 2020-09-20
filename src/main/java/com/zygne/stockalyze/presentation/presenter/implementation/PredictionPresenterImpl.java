package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.interactor.implementation.ChartLineInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.FileCreatorInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.NodeChartLineInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.NodeInteractorImpl;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.*;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.*;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.PineScriptLineInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.PineScriptZoneInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.ScriptInteractor;
import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.model.enums.MarketTime;
import com.zygne.stockalyze.domain.model.graphics.ChartLine;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;

import java.util.List;

public class PredictionPresenterImpl implements PredictionPresenter,
        NodeInteractor.Callback,
        NodeFilterInteractor.Callback,
        PredictionInteractor.Callback,
        ProbabilityInteractor.Callback,
        ChangeInteractor.Callback,
        PointInteractor.Callback,
        DragInteractor.Callback,
        TrendBiasInteractor.Callback,
        NewsBiasInteractor.Callback,
        GapBiasInteractor.Callback,
        StrongestPullBiasInteractor.Callback,
        ChartLineInteractor.Callback,
        ScriptInteractor.Callback,
        FileCreatorInteractor.Callback{

    private final PredictionPresenter.View view;

    private List<Node> nodes;

    private final MarketTime marketTime;
    private final GapDetails gapDetails;
    private final int currentPrice;
    private final String ticker;
    private final List<LiquidityZone> liquidityZoneList;
    private final List<PowerZone> powerZones;
    private final String folder;

    public PredictionPresenterImpl(View view, DataReport dataReport){
        this.view = view;
        this.marketTime = dataReport.marketTime;
        this.gapDetails = dataReport.gapDetails;
        this.currentPrice = dataReport.openPrice;
        this.ticker = dataReport.ticker;
        this.liquidityZoneList = dataReport.zones;
        this.powerZones = dataReport.powerZones;
        this.folder = dataReport.folder;
    }

    @Override
    public void start() {
        new NodeInteractorImpl(this, liquidityZoneList, currentPrice).execute();
    }

    @Override
    public void onNodesCreated(List<Node> data) {
        new NodeFilterInteractorImpl(this, data).execute();
    }

    @Override
    public void onNodesFiltered(List<Node> data) {
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
        this.nodes = data;
        new NodeChartLineInteractor(this, data).execute();
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

    @Override
    public void onChartLineCreated(List<ChartLine> lines) {
        new PineScriptLineInteractor(this, "prediction", ticker, lines).execute();
    }

    @Override
    public void onScriptCreated(String script, String name) {
        new FileCreatorInteractor(this, script, folder, name).execute();
    }

    @Override
    public void onFileCreated(String message) {
        view.onPredictionCompleted(nodes);
    }
}
