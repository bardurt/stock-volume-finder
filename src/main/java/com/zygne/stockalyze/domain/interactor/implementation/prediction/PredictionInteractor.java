package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

public class PredictionInteractor implements Interactor {


    private Callback callback;
    private List<Node> data;
    private double force;

    public PredictionInteractor(Callback callback, List<Node> data, double force) {
        this.callback = callback;
        this.data = data;
        this.force = force;
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

        for(int i = originIndex-1; i > -1; i--){
            upperPull.add(data.get(i));
        }

        List<Node> lowerPull = new ArrayList<>();

        for(int i = originIndex+1; i < data.size(); i++){
            lowerPull.add(data.get(i));
        }

        calculateProbability(upperPull, originLevel, highestPull, force,1, 1);
        calculateProbability(lowerPull, originLevel, highestPull, force, -1, 1);

        List<Node> filteredList = new ArrayList<>();

        for(Node e : data){
            if(e.probability > 0){
                filteredList.add(e);
            }
        }

        callback.onPredictionComplete(filteredList);
    }

    private void calculateProbability(List<Node> data, int originalLevel, double pull, double force, int direction, int adjustment){
        double prop = 90;
        prop += ((9 * force) * direction);
        int movement = 0;
        double change = 0;
        double drag = 0.0d;

        for (Node datum : data) {
            movement++;
            int diff = Math.abs(datum.level - originalLevel);
            drag += ((double) originalLevel / diff);
            change += (((datum.pull / pull)) * movement) * drag;

            prop -= change;
            datum.probability = prop + (direction * adjustment);

            if (prop < 0) {
                prop = 0;
            }
        }

    }

    public interface Callback{
        void onPredictionComplete(List<Node> data);
    }
}
