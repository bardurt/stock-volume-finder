package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.Node;

import java.util.List;

public interface PredictionPresenter {

    void start();

    interface View {
        void onPredictionCompleted(List<Node> nodes);
    }
}
