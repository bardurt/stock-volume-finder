package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.utils.Timer;
import com.zygne.stockalyze.presentation.presenter.base.DataPresenter;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.DataPresenterImpl;
import com.zygne.stockalyze.presentation.presenter.implementation.PredictionPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class App implements DataPresenter.View, PredictionPresenter.View {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");

    private int currentPrice = 0;
    private DataReport dataReport;
    private Timer timer = new Timer();

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
            System.out.println("-----------------------------------------------------------------------------");

            return;
        }

        try {
            currentPrice = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
        }

        timer.start();
        DataPresenter dataPresenter = new DataPresenterImpl(this, fileName, currentPrice);
        dataPresenter.start();
    }

    @Override
    public void onDataPresenterCompleted(DataReport dataReport) {
        this.dataReport = dataReport;

        System.out.println("\n\n");
        System.out.println("DataReport : " + dataReport.ticker + " - " + dateFormat.format(Calendar.getInstance().getTime()));
        System.out.println("Market Time : " + dataReport.marketTime.label);
        System.out.println();
        System.out.println("Ticker : " + dataReport.ticker + "\t Time Span : " + dataReport.timeSpan + " Days" + "\t Mean Vol : " + ((int) dataReport.statistics.mean));
        System.out.printf("%-9s%,d\n", "Float", dataReport.stockFloat);
        System.out.println();

        System.out.println("Gap Details");

        System.out.println();
        System.out.printf("%s%.2f%s\n", "Gap History for ", GapDetails.minGap, "+ gappers ");
        System.out.printf("%-16s%s\n", "Gap Count", dataReport.gapDetails.gapCount);
        System.out.printf("%-16s%.2f\n", "High 10%", dataReport.gapDetails.gap10);
        System.out.printf("%-16s%.2f\n", "High 20%", dataReport.gapDetails.gap20);
        System.out.printf("%-16s%.2f\n", "Bullish close", dataReport.gapDetails.gap20);
        System.out.println();

        System.out.println("Origin Price : " + dataReport.openPrice);
        System.out.println();

        double topCount = 0;

        for(LiquidityZone e : dataReport.topZones){
            topCount += e.volumePercentage;
        }

        System.out.printf("%-55s%-4s%-6.2f%-40s%-6s\n", "Supply Zones", "Top Zones ", topCount, "% Volume", "Range");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s%-12s%-7s%-10s%-12s%-5s%-2s", "Price", "Volume", "Count", "Rel Vol", "Vol Pct", "P Rat", "|");
        System.out.printf("%-8s%-12s%-7s%-10s%-12s%-5s%-2s", "Price", "Volume", "Count", "Rel Vol", "Vol Pct", "P Rat", "|");
        System.out.printf("%-8s%-12s%-7s%-10s%-12s%-5s%-2s", "Price", "Volume", "Count", "Rel Vol", "Vol Pct", "P Rat", "|");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");


        for (int i = 0; i < dataReport.filteredZones.size(); i++) {

            LiquidityZone lz = dataReport.filteredZones.get(i);
            LiquidityZone tz = null;
            LiquidityZone range = null;

            if (i < dataReport.topZones.size() - 1) {
                tz = dataReport.topZones.get(i);
            }

            if (i < dataReport.range.size() - 1) {
                range = dataReport.range.get(i);
            }

            System.out.printf("%-8s%-12s%-7s%-10.2f%-12.2f%5.2f%-2s", lz.price, lz.volume, lz.orderCount, lz.relativeVolume, lz.volumePercentage, lz.powerRatio, "|");

            if(tz != null){
                System.out.printf("%-8s%-12s%-7s%-10.2f%-12.2f%5.2f%-2s", tz.price, tz.volume, tz.orderCount, tz.relativeVolume, tz.volumePercentage, tz.powerRatio, "|");
            } else {
                System.out.printf("%-8s%-12s%-7s%-10s%-12s%-5s%-2s", "", "", "", "", "", "", "");
            }

            if(range != null){
                System.out.printf("%-8s%-12s%-7s%-10.2f%-12.2f%5.2f", range.price, range.volume, range.orderCount, range.relativeVolume, range.volumePercentage, range.powerRatio);
            }

            System.out.println();

        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();

//        if (currentPrice > 0) {
//            PredictionPresenter predictionPresenter = new PredictionPresenterImpl(this, dataReport);
//            predictionPresenter.start();
//        }
    }

    @Override
    public void onPredictionCompleted(List<Node> nodes) {
        System.out.println("Prediction : " + dataReport.ticker);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-18s%-12s\n", "Price", "Change", "Score (0 / 100)", "Buy / Sell");
        System.out.println("-----------------------------------------------------------------------------");

        for (Node e : nodes) {
            System.out.printf("%-16s%-12.2f%-18.2f%-12s%-12s\n", e.level, e.change, e.prediction, e.side, e.note);
        }

        System.out.println("-----------------------------------------------------------------------------");

        timer.stop();

        System.out.println("Computation time : " + timer.getDuration() + " ms");
    }
}
