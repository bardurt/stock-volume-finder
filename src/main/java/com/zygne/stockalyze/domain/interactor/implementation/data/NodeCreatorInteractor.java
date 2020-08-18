package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.domain.model.SupplyZone;
import com.zygne.stockalyze.domain.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeCreatorInteractor implements Interactor {

    private Callback callback;
    private List<SupplyZone> zones;
    private int currentLevel;

    public NodeCreatorInteractor(Callback callback, List<SupplyZone> zones, int currentLevel){
        this.callback = callback;
        this.zones = zones;
        this.currentLevel = currentLevel;
    }

    @Override
    public void execute() {

        List<Node> nodes = new ArrayList<>();

        boolean originFound = false;
        for(SupplyZone e : zones){

            Node n = new Node();
            n.level = e.price;
            n.pull = (int) (e.relativeVolume * e.orderCount);
            if(e.price == currentLevel){
                n.origin = true;
                n.note = Constants.TAG_CURRENT_PRICE;
                originFound = true;
            }

            nodes.add(n);
        }

        if(!originFound){
            Node n = new Node();
            n.level = currentLevel;
            n.pull = 0;
            n.origin = true;
            n.note = Constants.TAG_CURRENT_PRICE;
            nodes.add(n);
        }

        Collections.sort(nodes);

        callback.onNodesCreated(nodes);
    }

    public interface Callback{
        void onNodesCreated(List<Node> data);
    }
}
