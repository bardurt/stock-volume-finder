package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.GapDetails;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.enums.MarketTime;

import java.util.List;

public interface DataPresenter {

    void start();

    interface View {

        void onDataPresenterCompleted(String ticker, List<LiquidityZone> data, GapDetails gapDetails, MarketTime marketTime);
    }
}
