package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

public class SourceCheckerInteractor implements Interactor {

    private Callback callback;
    private String source;

    public SourceCheckerInteractor(Callback callback, String source){
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

    public interface Callback{
        void readCsvFile(String path);
        void downloadData(String ticker);
    }
}
