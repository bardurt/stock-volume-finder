package com.zygne.stockalyze.domain.interactor.implementation.prediction;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.List;

public interface GapBiasInteractor extends Interactor {

    public interface Callback{
        void onGapBiasCreated(List<Node> data);
    }
}
