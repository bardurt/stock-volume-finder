package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

import static com.zygne.stockalyze.domain.utils.Constants.MAX_PROD;

public class NewsBiasInteractor implements Interactor {

    private static final double MAX_BIAS = 2;
    private static final double MAX_SKEW = 0.02;


    private Callback callback;
    private List<Node> data;
    private int bias = 0;

    public NewsBiasInteractor(Callback callback, List<Node> data, int bias) {
        this.callback = callback;
        this.data = data;
        this.bias = bias;
    }

    @Override
    public void execute() {

        if (bias == 0) {
            callback.onNewsBiasCreated(data);
            return;
        }

        double upperBias = 1.00;

        if (bias > 0) {
            upperBias += MAX_SKEW;
        } else {
            upperBias -= MAX_SKEW;
        }

        double lowerBias = MAX_BIAS - upperBias;

        int originIndex = 0;

        for (int i = 0; i < data.size(); i++) {

            if (data.get(i).origin) {
                originIndex = i;
                data.get(i).probability = 100;
            }
        }

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

        callback.onNewsBiasCreated(data);

    }

    private void createBias(List<Node> nodes, double bias) {

        for (Node n : nodes) {
            n.probability *= bias;
            if (n.probability >= MAX_PROD) {
                n.probability = MAX_PROD;
            } else if(n.probability < 1){
                n.probability = 1;
            }
        }
    }

    public interface Callback {
        void onNewsBiasCreated(List<Node> data);
    }
}
