package com.zygne.stockalyze.domain;

import com.zygne.stockalyze.domain.interactor.implementation.FileCreatorInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.*;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.ScriptInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.scripting.TradingViewScriptInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.*;
import com.zygne.stockalyze.domain.interactor.implementation.stats.StatisticsInteractor;
import com.zygne.stockalyze.domain.model.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class App implements
        VolumePriceInteractor.Callback,
        VolumePriceGroupInteractor.Callback,
        LiquidityZoneInteractor.Callback,
        LiquidityZoneFilterInteractor.Callback,
        StatisticsInteractor.Callback,
        NodeInteractor.Callback,
        PredictionInteractor.Callback,
        ProbabilityInteractor.Callback,
        TrendBiasInteractor.Callback,
        NewsBiasInteractor.Callback,
        DataSourceInteractor.Callback,
        DataFetchInteractor.Callback,
        HistogramInteractor.Callback,
        ScriptInteractor.Callback,
        FileCreatorInteractor.Callback,
        StockFloatInteractor.Callback,
        TrendInteractor.Callback,
        GapRateInteractor.Callback,
        GapCheckInteractor.Callback,
        GapBiasInteractor.Callback{

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");
    private String ticker = "";
    private int timeSpan = 0;
    private double meanVolume = 0.0D;
    private double standardDeviation = 0.0D;
    private int stockFloat;
    private GapDetails gapDetails = null;
    private double gapRate = 0.0d;
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
            System.out.println("Gap = 1 Yes, 0 No");
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

        try {
            predictionData.gapper = Integer.parseInt(args[4]);
        } catch (Exception ignored){
            predictionData.gapper = 0;
        }

        simulator.currentPrice = predictionData.currentPrice;
        simulator.news = predictionData.news;
        simulator.trend = predictionData.trend;
        new DataSourceInteractorImpl(this, fileName).execute();
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
        new StatisticsInteractor(this, data).execute();
    }

    @Override
    public void onDataFetchError(String message) {
        System.out.println(message);
    }

    @Override
    public void onVolumePriceGroupCreated(List<VolumePriceGroup> data) {
        new LiquidityZoneInteractorImpl(this, data, meanVolume).execute();
    }

    @Override
    public void onLiquidityZonesCreated(List<LiquidityZone> data) {
        new LiquidityZoneFilterInteractorImpl(this, data, meanVolume, standardDeviation).execute();
    }

    @Override
    public void onLiquidityZonesFiltered(List<LiquidityZone> data) {
        simulator.liquidityZones = data;
        printList(data);
        if (predictionData.currentPrice > 0) {
            new NodeInteractorImpl(this, data, predictionData.currentPrice).execute();
        }
    }

    private void printList(List<LiquidityZone> data) {

        System.out.println("\n\n");
        System.out.println("Report : " + ticker + " - " + dateFormat.format(Calendar.getInstance().getTime()));
        System.out.println();
        System.out.println("Ticker : " + ticker + "\t Time Span : " + timeSpan + " Months" + "\t Mean Vol : " + ((int) meanVolume));
        System.out.printf("%-9s%,d\n", "Float", stockFloat);
        System.out.println();

        System.out.println("Gap Details");
        System.out.printf("%-16s%.2f\n", "Current Gap", gapDetails.currentGap);

        System.out.println();

        System.out.printf("%s%.2f%s%.2f\n", "Gap History for ", gapDetails.minGap, " to ", gapDetails.currentGap);
        System.out.printf("%-16s%.2f\n", "High 10%", gapDetails.gap10);
        System.out.printf("%-16s%.2f\n", "High 20%", gapDetails.gap20);
        System.out.printf("%-16s%.2f\n", "Bullish close", gapDetails.gap20);
        System.out.println();

        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-7s%-10s%-12s\n", "Price (Cents)", "Volume", "Count", "Rel Vol", "Note");
        System.out.println("-----------------------------------------------------------------------------");

        for (LiquidityZone e : data) {
            System.out.printf("%-16s%-12s%-7s%-8.2f%-12s\n", e.price, e.totalSize, e.orderCount, e.relativeVolume, " " + e.note);
        }

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("\n\n");
    }

    @Override
    public void onStatisticsCalculated(List<Histogram> data, double mean, double standardDeviation) {
        meanVolume = mean;
        this.standardDeviation = standardDeviation;
        new TrendInteractorImpl(this, data).execute();
    }

    @Override
    public void onVolumePriceCreated(List<VolumePriceLevel> data) {
        new VolumePriceGroupInteractorImpl(this, data).execute();
    }

    @Override
    public void onPredictionComplete(List<Node> data) {
        printNodes(data);
        tradingViewScriptInteractor = new TradingViewScriptInteractor(this, ticker, data);
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
        new GapBiasInteractor(this, data, gapDetails).execute();
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
    public void onScriptCreated(String script, String name) {
        new FileCreatorInteractor(this, script, name).execute();
    }

    @Override
    public void onFileCreated(String message) {
        System.out.println("SCRIPT CREATED : " + message);
    }

    @Override
    public void onTrendCalculated(Trend trend, List<Histogram> data) {
        new GapRateInteractorImpl(this, data, predictionData.currentPrice).execute();
    }


    @Override
    public void onGapRateCalculated(GapDetails gapDetails, List<Histogram> data) {
        this.gapDetails = gapDetails;
        this.predictionData.gapRate = gapDetails.gapBull;
        this.gapRate = gapDetails.gapBull;
        new VolumePriceInteractorImpl(this, data).execute();
    }

    @Override
    public void onGapBiasCreated(List<Node> data) {
        new PredictionInteractor(this, data).execute();
    }

    @Override
    public void onGapValidated(boolean gapUp) {

    }
}
