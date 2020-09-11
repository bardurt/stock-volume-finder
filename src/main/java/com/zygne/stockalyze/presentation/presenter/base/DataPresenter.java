package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.DataReport;

public interface DataPresenter {

    void start();

    interface View {
        void onDataPresenterCompleted(DataReport dataReport);
    }
}
