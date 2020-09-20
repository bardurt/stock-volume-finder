package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.NodeFilterInteractor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeFilterInteractorImpl implements NodeFilterInteractor {

    private Callback callback;
    private List<Node> data;

    public NodeFilterInteractorImpl(Callback callback, List<Node> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        double minStrength = Double.MAX_VALUE;

        for (Node e : data){
            if(e.strength < minStrength && !e.origin){
                minStrength = e.strength;
            }
        }

        double limit = minStrength*20;

        List<Node> filteredList = new ArrayList<>();

        for (Node e : data){
            if(e.strength > limit && !e.origin){
               filteredList.add(e);
            }
            if(e.origin){
                filteredList.add(e);
            }
        }

        callback.onNodesFiltered(filteredList);

    }
}
