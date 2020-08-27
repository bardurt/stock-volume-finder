package com.zygne.stockalyze.domain.interactor.implementation.data.base;


import com.zygne.stockalyze.domain.interactor.base.Interactor;

import java.util.List;

public interface DataFetchInteractor extends Interactor {

    interface Callback{
        void onDataFetched(List<String> entries, String ticker);
        void onDataFetchError(String message);
    }
}
