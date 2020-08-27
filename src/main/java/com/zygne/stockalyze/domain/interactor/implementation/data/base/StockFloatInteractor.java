package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

public interface StockFloatInteractor extends Interactor {

    interface Callback{
        void onStockFloatFetched(int stockFloat);
    }

}
