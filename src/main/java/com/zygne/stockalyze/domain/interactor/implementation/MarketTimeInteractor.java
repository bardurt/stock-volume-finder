package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.enums.MarketTime;

public interface MarketTimeInteractor extends Interactor {

    interface Callback {
        void onMarketTimeValidated(MarketTime marketTime);
    }
}
