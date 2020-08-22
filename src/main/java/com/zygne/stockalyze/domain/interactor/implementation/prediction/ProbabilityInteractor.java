package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

public class ProbabilityInteractor implements Interactor {

    private Callback callback;
    private List<Node> data;

    public ProbabilityInteractor(Callback callback, List<Node> data){
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

        for(int i = originIndex-1; i > -1; i--){
            data.get(i).change = (1 - originLevel / (double) data.get(i).level) * 100;
            upperPull.add(data.get(i));
        }

        List<Node> lowerPull = new ArrayList<>();

        for(int i = originIndex+1; i < data.size(); i++){
            data.get(i).change = (1 - ((double) data.get(i).level / originLevel))*-100;
            lowerPull.add(data.get(i));
        }

        calculateProbability(upperPull, originLevel, totalPull);
        calculateProbability(lowerPull, originLevel, totalPull);

        List<Node> filteredList = new ArrayList<>();

        for(Node e : data){
            if(e.probability > 0){
                filteredList.add(e);
            }
        }

        callback.onProbabilityCreated(filteredList);

    }

    private void calculateProbability(List<Node> data, int originalLevel, double pull){
        // starting probability for first item
        double probability = 95;

        // how many levels have we moved through
        int movement = 0;

        // changes for each step
        double change = 0;

        // how much drag, counter force
        double drag = 0.0d;

        for (Node datum : data) {
            movement++;

            // the absolute difference between the levels of origin and this level
            int levelDifference = Math.abs(datum.level - originalLevel);

            if(originalLevel > levelDifference){
                drag += ((double) levelDifference / originalLevel );
            } else {
                drag += ((double) originalLevel / levelDifference );
            }

            change += drag;

            change += (((datum.pull / pull)) * movement);

            probability -= change;

            datum.probability = probability;


            if (probability < 0) {
                probability = 0;
            }
        }

    }

    public interface Callback {
        void onProbabilityCreated(List<Node> data);
    }
}
