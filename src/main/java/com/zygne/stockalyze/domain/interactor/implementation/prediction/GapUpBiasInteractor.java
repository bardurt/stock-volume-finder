package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.GapDetails;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

import static com.zygne.stockalyze.domain.utils.Constants.MAX_PROD;

public class GapUpBiasInteractor implements GapBiasInteractor {

    private static final double MAX_BIAS = 2;
    private static final double MAX_SKEW = 0.10;

    private Callback callback;
    private List<Node> data;
    private GapDetails gapDetails;

    public GapUpBiasInteractor(Callback callback, List<Node> data, GapDetails gapDetails) {
        this.callback = callback;
        this.data = data;
        this.gapDetails = gapDetails;
    }

    @Override
    public void execute() {

        if(gapDetails.isGapper()){
            callback.onGapBiasCreated(data);
            return;
        }

        if(gapDetails.gap10 < 0.5){
            callback.onGapBiasCreated(data);
            return;
        }


        double slpwDpwn = 0.02;
        double upperBias = 1.00;

        if (gapDetails.gap10 >= 0.5) {
            upperBias += 0.6;
        }

        if (gapDetails.gap20 >= 0.5) {
            upperBias += 0.02;
        }

        if (gapDetails.gapBull >= 0.5) {
            upperBias += 0.03;
        }

        if(upperBias > 1 + MAX_SKEW){
            upperBias = 1 + MAX_SKEW;
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

        createBias(upperPull, upperBias,slpwDpwn, 1);
        createBias(lowerPull, lowerBias, slpwDpwn, 0.9);

        List<Node> filteredList = new ArrayList<>();

        filteredList.addAll(data);

        callback.onGapBiasCreated(data);

    }

    private void createBias(List<Node> nodes, double bias, double slowDown, double minBias) {

        for (Node n : nodes) {
            n.probability *= bias;
            bias *= (1-slowDown);

            if(bias < minBias){
                bias = minBias;
            }

            if (n.probability >= MAX_PROD) {
                n.probability = MAX_PROD;
            } else if(n.probability < 1){
                n.probability = 1;
            }
        }
    }
}
