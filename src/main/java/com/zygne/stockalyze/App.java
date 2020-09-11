package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.model.enums.MarketTime;
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

        if (dataReport.marketTime == MarketTime.MARKET_OPEN || dataReport.marketTime == MarketTime.PRE_MARKET) {
            System.out.println("Gap Details");
            System.out.printf("%-16s%.2f\n", "Current Gap", dataReport.gapDetails.currentGap);

            if (dataReport.gapDetails.isGapper()) {
                System.out.println();
                System.out.printf("%s%.2f%s\n", "Gap History for ", GapDetails.minGap, "+ gappers ");
                System.out.printf("%-16s%s\n", "Gap Count", dataReport.gapDetails.gapCount);
                System.out.printf("%-16s%.2f\n", "High 10%", dataReport.gapDetails.gap10);
                System.out.printf("%-16s%.2f\n", "High 20%", dataReport.gapDetails.gap20);
                System.out.printf("%-16s%.2f\n", "Bullish close", dataReport.gapDetails.gap20);
                System.out.println();
            }
        }

        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-7s%-10s%-12s\n", "Price (Cents)", "Volume", "Count", "Rel Vol", "Note");
        System.out.println("-----------------------------------------------------------------------------");

        for (LiquidityZone e : dataReport.zones) {
            System.out.printf("%-16s%-12s%-7s%-8.2f%-12s\n", e.price, e.totalSize, e.orderCount, e.relativeVolume, " " + e.note);
        }

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();

        System.out.println("TOP " + dataReport.topZones.size() + " zones : " + dataReport.ticker);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-7s%-10s%-12s\n", "Price (Cents)", "Volume", "Count", "Rel Vol", "Note");
        System.out.println("-----------------------------------------------------------------------------");

        for (LiquidityZone e : dataReport.topZones) {
            System.out.printf("%-16s%-12s%-7s%-8.2f%-12s\n", e.price, e.totalSize, e.orderCount, e.relativeVolume, " " + e.note);
        }

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();

        if (currentPrice > 0) {
            PredictionPresenter predictionPresenter = new PredictionPresenterImpl(this, dataReport);
            predictionPresenter.start();
        }
    }

    @Override
    public void onPredictionCompleted(List<Node> nodes) {
        System.out.println("Prediction : " + dataReport.ticker);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-16s%-12s%-12s\n", "Price", "Change", "Score (0 / 100)");
        System.out.println("-----------------------------------------------------------------------------");

        for (Node e : nodes) {
            System.out.printf("%-16s%-12.2f%-12.2f%-12s\n", e.level, e.change, e.prediction, e.note);
        }

        System.out.println("-----------------------------------------------------------------------------");
    }
}
