package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.interactor.implementation.*;
import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.*;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.PineScriptLineInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.PineScriptZoneInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.ScriptInteractor;
import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.model.enums.MarketTime;
import com.zygne.stockalyze.domain.model.graphics.ChartLine;
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
        DecayInteractor.Callback,
        ChartLineInteractor.Callback,
        PowerZoneInteractor.Callback,
        PowerZoneFilterInteractor.Callback,
        FolderCreatorInteractor.Callback{


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");

    private final View view;

    private List<Histogram> histogramList;

    private DataReport dataReport = new DataReport();
    private final int expectedScripts = 2;
    private int scriptCount = 0;
    private String folder = "";

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
        new FolderCreatorInteractor(this, dataReport.ticker).execute();
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
        new FileCreatorInteractor(this, script, folder, name).execute();
    }

    @Override
    public void onFileCreated(String message) {
        scriptCount++;
        if(scriptCount == 1){
            new PineScriptZoneInteractor(this, "power_zones", dataReport.ticker, dataReport.powerZones).execute();
        } else {
            view.onDataPresenterCompleted(dataReport);
        }
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
        new LiquidityZoneChartLineInteractor(this, data).execute();
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

    @Override
    public void onChartLineCreated(List<ChartLine> lines) {
        new PineScriptLineInteractor(this, "liqidity_zones", dataReport.ticker, lines).execute();
    }

    @Override
    public void onPowerZonesCreated(List<PowerZone> data) {
        new PowerZoneFilterInteractorImpl(this, data, dataReport.openPrice).execute();
    }

    @Override
    public void onPowerZoneFiltered(List<PowerZone> data) {
        dataReport.powerZones = data;
        new DecayInteractorImpl(this, histogramList, dataReport.timeSpan).execute();
    }

    @Override
    public void onFolderCreated(String path) {
        this.folder = path;
        this.dataReport.folder = folder;
        new PowerZoneInteractorImpl(this, histogramList).execute();
    }
}
