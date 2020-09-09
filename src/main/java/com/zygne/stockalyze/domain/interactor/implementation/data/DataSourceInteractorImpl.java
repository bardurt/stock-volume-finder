package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataSourceInteractor;

public class DataSourceInteractorImpl implements DataSourceInteractor {

    private final Callback callback;
    private final String source;

    public DataSourceInteractorImpl(Callback callback, String source){
        this.callback = callback;
        this.source = source;
    }

    @Override
    public void execute() {

        if(source.contains(".csv")){
            callback.readCsvFile(source);
        } else {
            callback.downloadData(source);
        }

    }
}
