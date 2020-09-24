package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.domain.model.PowerZone;

import java.util.List;

public class SideInteractorImpl implements Interactor {


    private Callback callback;
    private List<Node> nodes;
    private List<PowerZone> zones;

    public SideInteractorImpl(Callback callback, List<Node> nodes, List<PowerZone> zones) {
        this.callback = callback;
        this.nodes = nodes;
        this.zones = zones;
    }

    @Override
    public void execute() {

        for(Node n : nodes){

            for(PowerZone z : zones){


                if(z.inZone(n.level)){
                    if(z.type == PowerZone.RECJECT){
                        n.side--;
                    } else {
                        n.side++;
                    }
                }
            }

        }

        callback.onSideCreated(nodes);

    }

    public interface Callback{
        void onSideCreated(List<Node> data);
    }
}
