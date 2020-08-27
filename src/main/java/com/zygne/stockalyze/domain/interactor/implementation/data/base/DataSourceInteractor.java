package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

public interface DataSourceInteractor extends Interactor {

    interface Callback{
        void readCsvFile(String path);
        void downloadData(String ticker);
    }
}
