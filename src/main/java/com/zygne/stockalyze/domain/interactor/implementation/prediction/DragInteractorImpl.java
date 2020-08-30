package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.implementation.prediction.base.DragInteractor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.ArrayList;
import java.util.List;

public class DragInteractorImpl implements DragInteractor {

    private Callback callback;
    private List<Node> data;

    public DragInteractorImpl(Callback callback, List<Node> data) {
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

        calculateDrag(upperPull, totalPull);
        calculateDrag(lowerPull, totalPull);

        List<Node> filteredList = new ArrayList<>();

        for(Node e : data){
            if(e.probability > 0){
                filteredList.add(e);
            }
        }

        callback.onDragCreated(filteredList);
    }

    private void calculateDrag(List<Node> data, double pull){

        // how many levels have we moved through
        int movement = 0;

        // how much drag, counter force
        double drag = 0.1d;

        for (Node datum : data) {
            movement++;

            drag += (movement * 0.02);

            datum.probability -= drag;

            drag += (datum.pull / pull);

        }
    }
}
