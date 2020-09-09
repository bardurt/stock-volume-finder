package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.List;

public class NearestHighNodeInteractor implements Interactor {


    private final Callback callback;
    private final List<Node> nodes;

    public NearestHighNodeInteractor(Callback callback, List<Node> nodes){
        this.callback = callback;
        this.nodes = nodes;
    }

    @Override
    public void execute() {

        int originIndex = 0;

        for(int i = 0; i < nodes.size(); i++){

            if(nodes.get(i).origin){
                originIndex = i;
            }
        }

        Node upper = null;
        Node lower = null;

        try {
            upper = nodes.get(originIndex-1);
        } catch (Exception ignored){

        }

        try {
            lower = nodes.get(originIndex+1);
        } catch (Exception ignored){

        }

        if(upper == null){
            callback.onNearestHighNodeSelected(lower);
            return;
        }

        if(lower == null){
            callback.onNearestHighNodeSelected(upper);
            return;
        }

        if(lower.probability > upper.probability){
            callback.onNearestHighNodeSelected(lower);
            return;
        }

        if(lower.probability < upper.probability){
            callback.onNearestHighNodeSelected(upper);
        }

    }

    public interface Callback{
        void onNearestHighNodeSelected(Node node);
    }
}
