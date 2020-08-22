package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PredictionInteractor implements Interactor {

    private Callback callback;
    private List<Node> data;

    public PredictionInteractor(Callback callback, List<Node> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        int originIndex = 0;
        double highestPull = 0.0d;

        for(int i = 0; i < data.size(); i++){

            if(data.get(i).pull > highestPull){
                highestPull = data.get(i).pull;
            }

            if(data.get(i).origin){
                originIndex = i;
                data.get(i).prediction = 100;
            }
        }

        List<Node> upperPull = new ArrayList<>();

        for(int i = originIndex-1; i > -1; i--){
            upperPull.add(data.get(i));
        }

        List<Node> lowerPull = new ArrayList<>();

        for(int i = originIndex+1; i < data.size(); i++){
            lowerPull.add(data.get(i));
        }

        calculateProbability(upperPull);
        calculateProbability(lowerPull);

        List<Node> filteredList = new ArrayList<>();

        for(Node e : data){
            if(e.probability > 0){
                filteredList.add(e);
            }
        }

        Collections.sort(filteredList);

        callback.onPredictionComplete(filteredList);
    }

    private void calculateProbability(List<Node> data){

        data.get(0).prediction = data.get(0).probability;
        for(int i = 1; i < data.size(); i++){
            data.get(i).prediction = ((data.get(i).probability * data.get(i-1).probability) / 100);
        }
    }

    public interface Callback{
        void onPredictionComplete(List<Node> data);
    }
}
