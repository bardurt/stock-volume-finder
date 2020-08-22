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
            data.get(i).change = (1 - ((double) data.get(i).level / originLevel))*100;
            lowerPull.add(data.get(i));
        }

        calculateProbability(upperPull, totalPull);
        calculateProbability(lowerPull, totalPull);

        List<Node> filteredList = new ArrayList<>();

        for(Node e : data){
            if(e.probability > 0){
                filteredList.add(e);
            }
        }

        callback.onProbabilityCreated(filteredList);

    }

    private void calculateProbability(List<Node> data, double pull){

        // starting probability for first item
        double probability = 100;

        // how many levels have we moved through
        int movement = 0;

        // how much drag, counter force
        double drag = 0.1d;

        for (Node datum : data) {
            movement++;

            drag += (datum.change / 10);

            drag += (datum.pull / pull);

            drag += movement * 0.025;

            probability -= drag;

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
