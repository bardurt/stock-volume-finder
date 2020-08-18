package com.zygne.stockalyze.domain;

import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.prediction.PredictionInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.stats.StatisticsInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.stats.ScoreInteractor;
import com.zygne.stockalyze.domain.model.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class App implements
        VolumePriceLevelCreatorInteractor.Callback,
        VolumePriceFormatterInteractor.Callback,
        VolumePriceGroupCreatorInteractor.Callback,
        SupplyZoneCreatorInteractor.Callback,
        SupplyZoneFilterInteractor.Callback,
        InstitutionalPriceFormatterInteractor.Callback,
        HighestSupportInteractor.Callback,
        StatisticsInteractor.Callback,
        CsvHistogramInteractor.Callback,
        ScoreInteractor.Callback,
        NodeCreatorInteractor.Callback,
        PredictionInteractor.Callback {

    private final SimpleDateFormat dateFormat =  new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");
    private String ticker = "";
    private int timeSpan = 0;
    private double meanVolume = 0.0D;
    private double standardDeviation = 0.0D;
    private final PredictionData predictionData = new PredictionData();

    public void start(String[] args){
        if(args.length == 0){
            System.out.println("No CSV file provided");
            return;
        }

        String fileName = args[0];

        try {
            predictionData.currentPrice = Integer.parseInt(args[1]);
        } catch (Exception ignored){
            predictionData.currentPrice = 0;
        }

        try {
            predictionData.firstHourVolume = Integer.parseInt(args[2]);
        } catch (Exception ignored){
            predictionData.firstHourVolume = 0;
        }

        try {
            predictionData.newsCatalyst = Integer.parseInt(args[3]);
        } catch (Exception ignored){
            predictionData.newsCatalyst = 0;
        }

        try {
            predictionData.fiveDayTrend = Integer.parseInt(args[4]);
        } catch (Exception ignored){
            predictionData.fiveDayTrend = 0;
        }

        try {
            predictionData.formerRunner = Integer.parseInt(args[5]);
        } catch (Exception ignored){
            predictionData.formerRunner = 0;
        }

        try {
            predictionData.gapUp = Integer.parseInt(args[6]);
        } catch (Exception ignored){
            predictionData.gapUp = 0;
        }

        new CsvHistogramInteractor(this, fileName).execute();
    }

    @Override
    public void onInstitutionalPriceFormatted(List<VolumePriceLevel> data) {
        new VolumePriceGroupCreatorInteractor(this, data).execute();
    }

    @Override
    public void onVolumePriceFormatted(List<VolumePriceLevel> data) {
        for (VolumePriceLevel e : data) {
            System.out.println("Histogram p :" + e.price + ", size : " + e.size);
        }
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

        new ScoreInteractor(this, data, predictionData).execute();
    }


    @Override
    public void onHighestSupportFound(List<SupplyZone> data) {
        printList(data, null);
    }

    private void printList(List<SupplyZone> data, Score score) {

        System.out.println("\n\n");
        System.out.println(dateFormat.format(Calendar.getInstance().getTime()));
        System.out.println();
        System.out.println("Ticker : " + ticker + "\t Time Span : " + timeSpan + " Months" + "\t Mean Vol : " + (int) meanVolume);
        if (score != null) {
            System.out.println(score.toString());
        }
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
    public void onHistogramLoaded(List<Histogram> data, String tickerName, int timeSpan) {
        this.ticker = tickerName;
        this.timeSpan = timeSpan;
        new StatisticsInteractor(this, data).execute();
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
    public void onScoreCalculated(List<SupplyZone> data, Score score) {
        printList(data, score);

        if(predictionData.currentPrice > 0) {
            new NodeCreatorInteractor(this, data, predictionData.currentPrice).execute();
        }

    }

    @Override
    public void onPredictionComplete(List<Node> data) {

        System.out.println("Prediction : " + ticker);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-12s\n", "Level", "P(Level)", "Note");
        System.out.println("-----------------------------------------------------------------------------");
        for (Node e : data) {
            System.out.printf("%-16s%-12.2f%-12s\n", e.level, e.probability, e.note);
        }
        System.out.println("-----------------------------------------------------------------------------");
    }

    @Override
    public void onNodesCreated(List<Node> data) {
        new PredictionInteractor(this, data, 1).execute();
    }
}
