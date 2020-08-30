package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.interactor.implementation.FileCreatorInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.*;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.ScriptInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.TradingViewScriptInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.StatisticsInteractorImpl;
import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.presentation.presenter.base.DataPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        TopLiquidityZonesInteractorImpl.Callback{


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");

    private View view;

    private String ticker = "";
    private int timeSpan = 0;
    private Statistics statistics;
    private int stockFloat;
    private GapDetails gapDetails = null;
    private final int openPrice;
    private List<Histogram> histogramList;
    private List<LiquidityZone> liquidityZoneList;

    public DataPresenterImpl(View view, String ticker, int openPrice) {
        this.view = view;
        this.openPrice = openPrice;
        this.ticker = ticker;
    }

    @Override
    public void start() {
        new DataSourceInteractorImpl(this, ticker).execute();
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
        this.stockFloat = stockFloat;
    }

    @Override
    public void onDataFetched(List<String> entries, String ticker) {
        this.ticker = ticker;
        new HistogramInteractorImpl(this, entries).execute();
    }

    @Override
    public void onHistogramCreated(List<Histogram> data, int months) {
        this.timeSpan = months;
        this.histogramList = data;
        new StatisticsInteractorImpl(this, data).execute();
    }

    @Override
    public void onDataFetchError(String message) {
        System.out.println(message);
    }

    @Override
    public void onVolumePriceGroupCreated(List<VolumePriceGroup> data) {
        new LiquidityZoneInteractorImpl(this, data, statistics).execute();
    }

    @Override
    public void onLiquidityZonesCreated(List<LiquidityZone> data) {
        new LiquidityZoneFilterInteractorImpl(this, data).execute();
    }

    @Override
    public void onLiquidityZonesFiltered(List<LiquidityZone> data) {
        printList(data);
        this.liquidityZoneList = data;
        new TopLiquidityZonesInteractorImpl(this, liquidityZoneList, 10).execute();
    }

    private void printList(List<LiquidityZone> data) {

        System.out.println("\n\n");
        System.out.println("Report : " + ticker + " - " + dateFormat.format(Calendar.getInstance().getTime()));
        System.out.println();
        System.out.println("Ticker : " + ticker + "\t Time Span : " + timeSpan + " Months" + "\t Mean Vol : " + ((int) statistics.mean));
        System.out.printf("%-9s%,d\n", "Float", stockFloat);
        System.out.println();

        System.out.println("Gap Details");
        System.out.printf("%-16s%.2f\n", "Current Gap", gapDetails.currentGap);

        if (gapDetails.isGapper()) {
            System.out.println();
            System.out.printf("%s%.2f%s\n", "Gap History for ", GapDetails.minGap, "+ gappers ");
            System.out.printf("%-16s%s\n", "Gap Count", gapDetails.gapCount);
            System.out.printf("%-16s%.2f\n", "High 10%", gapDetails.gap10);
            System.out.printf("%-16s%.2f\n", "High 20%", gapDetails.gap20);
            System.out.printf("%-16s%.2f\n", "Bullish close", gapDetails.gap20);
            System.out.println();
        }

        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-7s%-10s%-12s\n", "Price (Cents)", "Volume", "Count", "Rel Vol", "Note");
        System.out.println("-----------------------------------------------------------------------------");

        for (LiquidityZone e : data) {
            System.out.printf("%-16s%-12s%-7s%-8.2f%-12s\n", e.price, e.totalSize, e.orderCount, e.relativeVolume, " " + e.note);
        }

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
    }

    @Override
    public void onStatisticsCalculated(Statistics statistics) {
        this.statistics = statistics;
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
        System.out.println("SCRIPT CREATED : " + message);
        System.out.println();
        view.onDataPresenterCompleted(ticker, liquidityZoneList, statistics, gapDetails);
    }

    @Override
    public void onTrendCalculated(Trend trend, List<Histogram> data) {
        new GapRateInteractorImpl(this, data, openPrice).execute();
    }

    @Override
    public void onGapRateCalculated(GapDetails gapDetails, List<Histogram> data) {
        this.gapDetails = gapDetails;
        new VolumePriceInteractorImpl(this, data).execute();
    }

    @Override
    public void onTopLiquidityZonesFound(List<LiquidityZone> data) {
        new TradingViewScriptInteractor(this, ticker, data).execute();
    }
}
