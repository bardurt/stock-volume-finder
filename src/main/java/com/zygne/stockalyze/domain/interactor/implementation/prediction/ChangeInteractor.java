package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.List;

public class ChangeInteractor implements Interactor {

    private final Callback callback;
    private final List<Node> data;

    public ChangeInteractor(Callback callback, List<Node> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {
        int originIndex = -1;
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
            }
        }

        if (originIndex == -1){
            callback.onChangeCalculated(data);
            return;
        }

        for(int i = originIndex; i > -1; i--){
            if(data.get(i).origin){
                continue;
            }

            data.get(i).change = (((double) data.get(i).level / originLevel) -1) * 100;
        }

        for(int i = originIndex; i < data.size(); i++){
            if(data.get(i).origin){
                continue;
            }
            data.get(i).change = (1 - ((double) data.get(i).level / originLevel))*100;
        }

        callback.onChangeCalculated(data);
    }

    public interface Callback{
        void onChangeCalculated(List<Node> data);
    }
}
