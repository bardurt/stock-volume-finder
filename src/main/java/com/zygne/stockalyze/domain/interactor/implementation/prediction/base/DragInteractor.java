package com.zygne.stockalyze.domain.interactor.implementation.prediction.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Node;

import java.util.List;

public interface DragInteractor extends Interactor {

    interface Callback{
        void onDragCreated(List<Node> data);
    }
}