package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.PointInteractor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

public class PointInteractorImpl implements PointInteractor {

    private Callback callback;
    private List<Node> data;

    public PointInteractorImpl(Callback callback, List<Node> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        int originIndex = 0;
        double totalPull = 0.0d;
        int originLevel = 0;
        double highestPull = 0.0d;

        for(int i = 0; i < data.size(); i++){

            if(data.get(i).pull > highestPull){
                highestPull = data.get(i).pull;
            }
            totalPull += data.get(i).pull;
            if(data.get(i).origin){
                originIndex = i;
                originLevel = data.get(i).level;
                data.get(i).probability = 100;
            }
        }

        List<Node> upperPull = new ArrayList<>();

        for(int i = originIndex; i > -1; i--){
            if(data.get(i).origin){
                continue;
            }

            data.get(i).change = (((double) data.get(i).level / originLevel) -1) * 100;
            upperPull.add(data.get(i));
        }

        List<Node> lowerPull = new ArrayList<>();

        for(int i = originIndex; i < data.size(); i++){
            if(data.get(i).origin){
                continue;
            }
            data.get(i).change = (1 - ((double) data.get(i).level / originLevel))*100;
            lowerPull.add(data.get(i));
        }

        calculatePoints(upperPull);
        calculatePoints(lowerPull);

        List<Node> filteredList = new ArrayList<>();

        for(Node e : data){
            if(e.probability > 0){
                filteredList.add(e);
            }
        }

        callback.onPointsCreated(filteredList);
    }

    private void calculatePoints(List<Node> data){

        for (Node datum : data) {

            datum.probability = (99 - (datum.change * 0.6));
        }
    }
}
