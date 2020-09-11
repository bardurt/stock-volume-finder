package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.interactor.implementation.FileCreatorInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.MarketTimeInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.NyseMarketTimeInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.*;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.PineScriptInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.ScriptInteractor;
import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.model.enums.MarketTime;
import com.zygne.stockalyze.presentation.presenter.base.DataPresenter;

import java.text.SimpleDateFormat;
import java.util.List;

public class DataPresenterImpl implements DataPresenter,
        VolumePriceInteractor.Callback,
        VolumePriceGroupInteractor.Callback,
        LiquidityZoneInteractor.Callback,
        LiquidityZoneFilterInteractor.Callback,
        StatisticsInteractor.Callback,
        DataSourceInteractor.Callback,
        DataFetchInteractor.Callback,
        HistogramInteractor.Callback,
        ScriptInteractor.Callback,
        FileCreatorInteractor.Callback,
        StockFloatInteractor.Callback,
        TrendInteractor.Callback,
        GapRateInteractor.Callback,
        TopLiquidityZonesInteractorImpl.Callback,
        MarketTimeInteractor.Callback,
        DecayInteractor.Callback {


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");

    private final View view;

    private List<Histogram> histogramList;

    private DataReport dataReport = new DataReport();

    public DataPresenterImpl(View view, String ticker, int openPrice) {
        this.view = view;
        dataReport.openPrice = openPrice;
        dataReport.ticker = ticker;
    }

    @Override
    public void start() {
        new NyseMarketTimeInteractor(this).execute();
    }

    @Override
    public void readCsvFile(String path) {
        new CsvReaderInteractor(this, path).execute();
    }

    @Override
    public void downloadData(String ticker) {
        new YahooFloatInteractor(this, ticker).execute();
        new YahooFinanceInteractor(this, ticker).execute();
    }

    @Override
    public void onStockFloatFetched(int stockFloat) {
        dataReport.stockFloat = stockFloat;
    }

    @Override
    public void onDataFetched(List<String> entries, String ticker) {
        dataReport.ticker = ticker;
        new HistogramInteractorImpl(this, entries).execute();
    }

    @Override
    public void onHistogramCreated(List<Histogram> data, int months) {
        dataReport.timeSpan = months;
        this.histogramList = data;
        new DecayInteractorImpl(this, data, months).execute();

    }

    @Override
    public void onDataFetchError(String message) {
        System.out.println(message);
    }

    @Override
    public void onVolumePriceGroupCreated(List<VolumePriceGroup> data) {
        new LiquidityZoneInteractorImpl(this, data, dataReport.statistics).execute();
    }

    @Override
    public void onLiquidityZonesCreated(List<LiquidityZone> data) {
        new LiquidityZoneFilterInteractorImpl(this, data).execute();
    }

    @Override
    public void onLiquidityZonesFiltered(List<LiquidityZone> data) {
        dataReport.zones = data;
        new TopLiquidityZonesInteractorImpl(this, data, 20).execute();
    }

    @Override
    public void onStatisticsCalculated(Statistics statistics) {
        dataReport.statistics = statistics;
        new TrendInteractorImpl(this, histogramList).execute();
    }

    @Override
    public void onVolumePriceCreated(List<VolumePriceLevel> data) {
        new VolumePriceGroupInteractorImpl(this, data).execute();
    }

    @Override
    public void onScriptCreated(String script, String name) {
        new FileCreatorInteractor(this, script, name).execute();
    }

    @Override
    public void onFileCreated(String message) {
        view.onDataPresenterCompleted(dataReport);
    }

    @Override
    public void onTrendCalculated(List<Histogram> data) {
        new GapRateInteractorImpl(this, data, dataReport.openPrice).execute();
    }

    @Override
    public void onGapRateCalculated(GapDetails gapDetails, List<Histogram> data) {
        dataReport.gapDetails = gapDetails;
        new VolumePriceInteractorImpl(this, data).execute();
    }

    @Override
    public void onTopLiquidityZonesFound(List<LiquidityZone> data, int count) {
        dataReport.topZones = data;

        new PineScriptInteractor(this, dataReport.ticker, data).execute();
    }

    @Override
    public void onMarketTimeValidated(MarketTime marketTime) {
        dataReport.marketTime = marketTime;
        new DataSourceInteractorImpl(this, dataReport.ticker).execute();
    }

    @Override
    public void onDecayCalculated(List<Histogram> data) {
        new StatisticsInteractorImpl(this, data).execute();
    }
}
