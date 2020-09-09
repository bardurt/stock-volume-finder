package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.PointInteractor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

public class PointInteractorImpl implements PointInteractor {

    private final Callback callback;
    private final List<Node> data;

    public PointInteractorImpl(Callback callback, List<Node> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        for(Node e : data){
            e.probability = (99 - (e.change * 0.5));
        }

        List<Node> filteredList = new ArrayList<>();

        for(Node e : data){
            if(e.probability > 0){
                filteredList.add(e);
            }
        }

        callback.onPointsCreated(filteredList);
    }
}
