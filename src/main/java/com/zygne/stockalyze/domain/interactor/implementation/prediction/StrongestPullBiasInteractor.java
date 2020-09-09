package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

import static com.zygne.stockalyze.domain.utils.Constants.MAX_PROBABILITY;

public class StrongestPullBiasInteractor implements Interactor {

    private static final double MAX_BIAS = 2;
    private static final double MAX_SKEW = 0.02;

    private final Callback callback;
    private final List<Node> data;

    public StrongestPullBiasInteractor(Callback callback, List<Node> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        int strongestPullLevel = 0;
        double strongestPull = 0;
        Node origin = null;
        int originIndex = 0;

        for (int i = 0; i < data.size(); i++) {

            if (data.get(i).pull > strongestPull) {
                strongestPull = data.get(i).pull;
                strongestPullLevel = data.get(i).level;
            }

            if(data.get(i).origin){
                origin = data.get(i);
                originIndex = i;
            }
        }


        if(origin == null){
            callback.onStrongestPullBiasCreated(data);
            return;
        }

        double upperBias = 1.00;

        if (strongestPullLevel > origin.level) {
            upperBias += MAX_SKEW;
        } else if (strongestPullLevel < origin.level) {
            upperBias -= MAX_SKEW;
        } else {
            callback.onStrongestPullBiasCreated(data);
            return;
        }

        double lowerBias = MAX_BIAS - upperBias;

        List<Node> upperPull = new ArrayList<>();

        for (int i = originIndex - 1; i > -1; i--) {
            upperPull.add(data.get(i));
        }

        List<Node> lowerPull = new ArrayList<>();

        for (int i = originIndex + 1; i < data.size(); i++) {
            lowerPull.add(data.get(i));
        }

        createBias(upperPull, upperBias);
        createBias(lowerPull, lowerBias);

        List<Node> filteredList = new ArrayList<>();

        filteredList.addAll(data);

        callback.onStrongestPullBiasCreated(data);

    }

    private void createBias(List<Node> nodes, double bias) {

        for (Node n : nodes) {
            n.probability *= bias;
            if (n.probability >= MAX_PROBABILITY) {
                n.probability = MAX_PROBABILITY;
            } else if(n.probability < 1){
                n.probability = 1;
            }
        }
    }

    public interface Callback {
        void onStrongestPullBiasCreated(List<Node> data);
    }
}
