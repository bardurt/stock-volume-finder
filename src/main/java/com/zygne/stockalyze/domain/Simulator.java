package com.zygne.stockalyze.domain;

import com.zygne.stockalyze.domain.interactor.implementation.data.NodeCreatorInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.NearestHighNodeInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.NewsBiasInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.ProbabilityInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.TrendBiasInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.domain.model.SupplyZone;

import java.util.List;

public class Simulator implements
        NodeCreatorInteractor.Callback,
        ProbabilityInteractor.Callback,
        NewsBiasInteractor.Callback,
        TrendBiasInteractor.Callback,
        NearestHighNodeInteractor.Callback {


    private static final int MAX_ROUNDS = 96;
    private int rounds = 0;
    public List<SupplyZone> supplyZones;
    public int currentPrice;
    public int trend;
    public int news;

    public void start() {
        if (supplyZones != null) {
            new NodeCreatorInteractor(this, supplyZones, currentPrice).execute();
        }

    }


    @Override
    public void onNodesCreated(List<Node> data) {
        new ProbabilityInteractor(this, data).execute();
    }

    @Override
    public void onNewsBiasCreated(List<Node> data) {
        new TrendBiasInteractor(this, data, trend).execute();
    }

    @Override
    public void onProbabilityCreated(List<Node> data) {
        new NewsBiasInteractor(this, data, news).execute();
    }

    @Override
    public void onTrendBiasCreated(List<Node> data) {
        new NearestHighNodeInteractor(this, data).execute();
    }

    @Override
    public void onNearestHighNodeSelected(Node node) {
        rounds++;

        if(rounds > 12){
            news = 0;
        }

        if(rounds > 6){
            trend = 0;
        }

        System.out.println("Round " + rounds + " " + node.level);

        if (rounds < MAX_ROUNDS) {
            currentPrice = node.level;
            start();
        }

    }
}
