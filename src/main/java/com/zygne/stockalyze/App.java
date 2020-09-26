package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.utils.Timer;
import com.zygne.stockalyze.presentation.printing.Color;
import com.zygne.stockalyze.presentation.printing.ConsolePrinter;
import com.zygne.stockalyze.presentation.printing.Printer;
import com.zygne.stockalyze.presentation.presenter.base.DataPresenter;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.DataPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class App implements DataPresenter.View, PredictionPresenter.View {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");

    private final Printer printer = new ConsolePrinter();
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

        System.out.printf("%-60s%-9s%-6.2f%-45s%-6s\n", "Supply Zones", "Top Zones ", topCount, "% Volume", "Range");
        printer.print( StringUtils.repeat("-", 182));
        System.out.println();
        for(int i = 0; i< 3; i++){
            System.out.printf("%-8s%-12s%-7s%-10s%-9s%6s%6s%-3s", "Price", "Volume", "Count", "Rel Vol", "Vol Pct", "P Rat", "Rank", " | ");

        }
        System.out.println();

        printer.print( StringUtils.repeat("-", 182));
        System.out.println();

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

            String zone = String.format("%-8s%-12s%-7s%-10.2f%-9.2f%6.2f%6d", lz.price, lz.volume, lz.orderCount, lz.relativeVolume, lz.volumePercentage, lz.powerRatio, lz.rank);

            if(lz.rank < 10){
                printer.setBackgroundColor(Color.CYAN);
            }

            if(lz.powerRatio < -0.49){
                printer.setTextColor(Color.RED);
            } else if(lz.powerRatio > 0.49){
                printer.setTextColor(Color.BLUE);
            }

            printer.print(zone);
            printer.print(" | ");

            String topZone = "";

            if(tz != null){
                if(tz.powerRatio < -0.49){
                    printer.setTextColor(Color.RED);
                } else if(tz.powerRatio > 0.49){
                    printer.setTextColor(Color.BLUE);
                }
                topZone = String.format("%-8s%-12s%-7s%-10.2f%-9.2f%6.2f%6d", tz.price, tz.volume, tz.orderCount, tz.relativeVolume, tz.volumePercentage, tz.powerRatio, tz.rank);
            } else {
                topZone = String.format("%-8s%-12s%-7s%-10s%-9s%6s%6s", "", "", "", "", "", "", "");
            }

            printer.print(topZone);
            printer.print(" | ");

            String rangeText = "";

            if(range != null){
                if(range.origin) {
                    rangeText = StringUtils.center("<---------------->", 58);

                } else {

                    rangeText = String.format("%-8s%-12s%-7s%-10.2f%-9.2f%6.2f%6d", range.price, range.volume, range.orderCount, range.relativeVolume, range.volumePercentage, range.powerRatio, range.rank);

                    if(range.rank < 10){
                        printer.setBackgroundColor(Color.CYAN);
                    }

                    if(range.powerRatio < -0.49){
                        printer.setTextColor(Color.RED);
                    } else if(range.powerRatio > 0.49){
                        printer.setTextColor(Color.BLUE);
                    }

                }
            } else {
                rangeText = String.format("%-8s%-12s%-7s%-10s%-9s%6s%6s", "", "", "", "", "", "", "");
            }

            printer.print(rangeText);

            System.out.println();

        }

        printer.print( StringUtils.repeat("-", 182));
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
