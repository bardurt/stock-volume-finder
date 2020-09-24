package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.interactor.implementation.charting.ChartLineInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.FileCreatorInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.charting.NodeChartLineInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.*;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.DragInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.NodeFilterInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.NodeInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.PointInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.PineScript2Interactor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.ScriptInteractor;
import com.zygne.stockalyze.domain.model.DataReport;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.domain.model.PowerZone;
import com.zygne.stockalyze.domain.model.graphics.ChartObject;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;

import java.util.List;

public class PredictionPresenterImpl implements PredictionPresenter,
        NodeInteractor.Callback,
        NodeFilterInteractor.Callback,
        PredictionInteractor.Callback,
        ChangeInteractor.Callback,
        PointInteractor.Callback,
        DragInteractor.Callback,
        StrongestPullBiasInteractor.Callback,
        ChartLineInteractor.Callback,
        ScriptInteractor.Callback,
        FileCreatorInteractor.Callback,
        SideInteractorImpl.Callback{

    private final PredictionPresenter.View view;

    private List<Node> nodes;

    private final int currentPrice;
    private final String ticker;
    private final List<LiquidityZone> liquidityZoneList;
    private final List<PowerZone> powerZones;
    private final String folder;

    public PredictionPresenterImpl(View view, DataReport dataReport){
        this.view = view;
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
        new SideInteractorImpl(this, data, powerZones).execute();
    }

    @Override
    public void onNodesFiltered(List<Node> data) {
        new ChangeInteractor(this, data).execute();
    }

    @Override
    public void onPredictionComplete(List<Node> data) {
        this.nodes = data;
        new NodeChartLineInteractor(this, data).execute();
    }

    @Override
    public void onStrongestPullBiasCreated(List<Node> data) {
        new PredictionInteractor(this, data).execute();
    }

    @Override
    public void onDragCreated(List<Node> data) {
        new StrongestPullBiasInteractor(this, data).execute();
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
    public void onChartLineCreated(List<ChartObject> lines) {
        new PineScript2Interactor(this, "prediction", ticker, lines).execute();
    }

    @Override
    public void onScriptCreated(String script, String name) {
        new FileCreatorInteractor(this, script, folder, name).execute();
    }

    @Override
    public void onFileCreated(String message) {
        view.onPredictionCompleted(nodes);
    }

    @Override
    public void onSideCreated(List<Node> data) {
        new NodeFilterInteractorImpl(this, data).execute();
    }
}
