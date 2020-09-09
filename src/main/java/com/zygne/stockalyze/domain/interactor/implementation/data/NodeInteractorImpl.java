package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.NodeInteractor;
import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeInteractorImpl implements NodeInteractor {

    private final Callback callback;
    private final List<LiquidityZone> zones;
    private final int currentLevel;

    public NodeInteractorImpl(Callback callback, List<LiquidityZone> zones, int currentLevel){
        this.callback = callback;
        this.zones = zones;
        this.currentLevel = currentLevel;
    }

    @Override
    public void execute() {

        List<Node> nodes = new ArrayList<>();

        boolean originFound = false;
        for(LiquidityZone e : zones){

            Node n = new Node();
            n.level = e.price;
            n.pull = (int) (e.relativeVolume * e.orderCount);
            n.strength = e.relativeVolume;
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
}
