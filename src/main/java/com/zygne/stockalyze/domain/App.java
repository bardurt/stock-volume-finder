package com.zygne.stockalyze.domain;

import com.zygne.stockalyze.domain.interactor.implementation.ScriptInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.TradingViewScriptInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.NewsBiasInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.PredictionInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.TrendBiasInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.ProbabilityInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.stats.StatisticsInteractor;
import com.zygne.stockalyze.domain.model.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class App implements
        VolumePriceLevelCreatorInteractor.Callback,
        VolumePriceGroupCreatorInteractor.Callback,
        SupplyZoneCreatorInteractor.Callback,
        SupplyZoneFilterInteractor.Callback,
        InstitutionalPriceFormatterInteractor.Callback,
        StatisticsInteractor.Callback,
        NodeCreatorInteractor.Callback,
        PredictionInteractor.Callback,
        ProbabilityInteractor.Callback,
        TrendBiasInteractor.Callback,
        NewsBiasInteractor.Callback,
        SourceCheckerInteractor.Callback,
        DataFetchInteractor.Callback,
        HistogramCreatorInteractor.Callback,
        ScriptInteractor.Callback {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");
    private String ticker = "";
    private int timeSpan = 0;
    private double meanVolume = 0.0D;
    private double standardDeviation = 0.0D;
    private final PredictionData predictionData = new PredictionData();
    private TradingViewScriptInteractor tradingViewScriptInteractor;

    private Simulator simulator = new Simulator();

    public void start(String[] args) {
        if (args.length == 0) {
            System.out.println("No argument provided");
            return;
        }

        String fileName = args[0];

        if (fileName.equals("-h")) {
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Help");
            System.out.println("arg1 = Source (Path to csv file OR ticker name)");
            System.out.println("arg2 = current price in cents");
            System.out.println("trend = -1 down, 0 consolidation, 1 up");
            System.out.println("News = -1 negative, 0 non or generic, 1 significant positive news");
            System.out.println("-----------------------------------------------------------------------------");

            return;
        }

        try {
            predictionData.currentPrice = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
            predictionData.currentPrice = 0;
        }

        try {
            predictionData.trend = Integer.parseInt(args[2]);
        } catch (Exception ignored) {
            predictionData.trend = 0;
        }

        try {
            predictionData.news = Integer.parseInt(args[3]);
        } catch (Exception ignored) {
            predictionData.news = 0;
        }

        simulator.currentPrice = predictionData.currentPrice;
        simulator.news = predictionData.news;
        simulator.trend = predictionData.trend;
        new SourceCheckerInteractor(this, fileName).execute();
    }

    @Override
    public void readCsvFile(String path) {
        new CsvReaderInteractor(this, path).execute();
    }

    @Override
    public void downloadData(String ticker) {
        new YahooFinanceInteractor(this, ticker).execute();
    }

    @Override
    public void onDataFetched(List<String> entries, String ticker) {
        this.ticker = ticker;
        new HistogramCreatorInteractor(this, entries).execute();
    }

    @Override
    public void onHistogramCreated(List<Histogram> data, int months) {
        this.timeSpan = months;
        new StatisticsInteractor(this, data).execute();
    }

    @Override
    public void onDataFetchError(String message) {
        System.out.println(message);
    }

    @Override
    public void onInstitutionalPriceFormatted(List<VolumePriceLevel> data) {
        new VolumePriceGroupCreatorInteractor(this, data).execute();
    }

    @Override
    public void onVolumePriceGroupCreated(List<VolumePriceGroup> data) {
        new SupplyZoneCreatorInteractor(this, data).execute();
    }

    @Override
    public void onSupplyZoneCreated(List<SupplyZone> data) {
        new SupplyZoneFilterInteractor(this, data, meanVolume, standardDeviation).execute();
    }

    @Override
    public void onSupplyZoneFiltered(List<SupplyZone> data) {
        simulator.supplyZones = data;
        printList(data);
        tradingViewScriptInteractor = new TradingViewScriptInteractor(this, ticker, data);
        if (predictionData.currentPrice > 0) {
            new NodeCreatorInteractor(this, data, predictionData.currentPrice).execute();
        }
    }

    private void printList(List<SupplyZone> data) {

        System.out.println("\n\n");
        System.out.println(dateFormat.format(Calendar.getInstance().getTime()));
        System.out.println();
        System.out.println("Ticker : " + ticker + "\t Time Span : " + timeSpan + " Months" + "\t Mean Vol : " + (int) meanVolume);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-7s%-10s%-12s\n", "Price (Cents)", "Volume", "Count", "Rel Vol", "Note");
        System.out.println("-----------------------------------------------------------------------------");

        for (SupplyZone e : data) {
            System.out.printf("%-16s%-12s%-7s%-8.2f%-12s\n", e.price, e.totalSize, e.orderCount, e.relativeVolume, " " + e.note);
        }

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("\n\n");
    }

    @Override
    public void onStatisticsCalculated(List<Histogram> data, double mean, double standardDeviation) {
        meanVolume = mean;
        this.standardDeviation = standardDeviation;
        new VolumePriceLevelCreatorInteractor(this, data).execute();
    }

    @Override
    public void onVolumePriceCreated(List<VolumePriceLevel> data) {
        new VolumePriceGroupCreatorInteractor(this, data).execute();
    }

    @Override
    public void onPredictionComplete(List<Node> data) {
        printNodes(data);
        tradingViewScriptInteractor.execute();
        //simulator.start();
    }

    @Override
    public void onNodesCreated(List<Node> data) {
        new ProbabilityInteractor(this, data).execute();
    }

    @Override
    public void onProbabilityCreated(List<Node> data) {
        new TrendBiasInteractor(this, data, predictionData.trend).execute();
    }

    @Override
    public void onTrendBiasCreated(List<Node> data) {
        new NewsBiasInteractor(this, data, predictionData.news).execute();
    }

    @Override
    public void onNewsBiasCreated(List<Node> data) {
        new PredictionInteractor(this, data).execute();
    }

    private void printNodes(List<Node> data) {
        System.out.println("Prediction : " + ticker);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-12s\n", "Price", "Change", "Prediction");
        System.out.println("-----------------------------------------------------------------------------");
        for (Node e : data) {
            System.out.printf("%-16s%-12.2f%-12.2f%-12s\n", e.level, e.change, e.prediction, e.note);
        }
        System.out.println("-----------------------------------------------------------------------------");

    }

    @Override
    public void onScriptCreated(String script) {
        System.out.println(script);
    }
}
