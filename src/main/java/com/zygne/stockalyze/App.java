package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.utils.Timer;
import com.zygne.stockalyze.presentation.presenter.implementation.PredictionPresenterImpl;
import com.zygne.stockalyze.presentation.printing.*;
import com.zygne.stockalyze.presentation.presenter.base.DataPresenter;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.DataPresenterImpl;
import com.zygne.stockalyze.presentation.printing.command.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class App implements DataPresenter.View, PredictionPresenter.View {

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

    private void printInfoSection(DataReport dataReport) {

        printer.addCommand(new TextCommand("DataReport : " + dataReport.ticker + " - " + dateFormat.format(Calendar.getInstance().getTime())));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("Ticker : " + dataReport.ticker + "\t Time Span : " + dataReport.timeSpan + " Days" + "\t Mean Vol : " + ((int) dataReport.statistics.mean)));
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand(String.format("%-9s%,d", "Float", dataReport.stockFloat)));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand(String.format("%s%.2f%s", "Gap History for ", GapDetails.minGap, "+ gappers ")));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("Gap Count"));
        printer.addCommand(new ColumnCommand(18));
        printer.print();

        printer.addCommand(new TextCommand("" + dataReport.gapDetails.gapCount));
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("High 10%"));
        printer.addCommand(new ColumnCommand(18));
        printer.print();

        printer.addCommand(new TextCommand(String.format("%.2f", dataReport.gapDetails.gap10)));
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("High 20%"));
        printer.addCommand(new ColumnCommand(18));
        printer.print();

        printer.addCommand(new TextCommand(String.format("%.2f", dataReport.gapDetails.gap20)));
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("Bullish close"));
        printer.addCommand(new ColumnCommand(18));
        printer.print();

        printer.addCommand(new TextCommand(String.format("%.2f", dataReport.gapDetails.gapBull)));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("Origin Price"));
        printer.addCommand(new ColumnCommand(18));
        printer.print();

        printer.addCommand(new TextCommand(String.format("%d", dataReport.openPrice)));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();
    }

    private void printTableHeader(){
        for (int i = 0; i < 3; i++) {

            printer.addCommand(new TextCommand("Price"));
            printer.addCommand(new ColumnCommand(6));
            printer.addCommand(new AlignmentCommand(Alignment.LEFT));
            printer.print();

            printer.addCommand(new TextCommand("Vlm"));
            printer.addCommand(new ColumnCommand(16));
            printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
            printer.print();

            printer.addCommand(new TextCommand("RVol"));
            printer.addCommand(new ColumnCommand(8));
            printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
            printer.print();

            printer.addCommand(new TextCommand("VolPct"));
            printer.addCommand(new ColumnCommand(7));
            printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
            printer.print();

            printer.addCommand(new TextCommand("P Rat"));
            printer.addCommand(new ColumnCommand(6));
            printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
            printer.print();

            printer.addCommand(new TextCommand("Rank"));
            printer.addCommand(new ColumnCommand(7));
            printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
            printer.print();

            printer.addCommand(new TextCommand("Ptile"));
            printer.addCommand(new ColumnCommand(6));
            printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
            printer.print();

            printer.addCommand(new TextCommand("|"));
            printer.addCommand(new ColumnCommand(3));
            printer.addCommand(new AlignmentCommand(Alignment.CENTER));
            printer.print();

        }
    }

    private void printTableEntry(LiquidityZone liquidityZone) {

        if (liquidityZone != null) {
            if (liquidityZone.origin) {
                printer.addCommand(new ColumnCommand(56));
                printer.addCommand(new TextCommand("<---------------->"));
                printer.addCommand(new AlignmentCommand(Alignment.CENTER));
                printer.print();
            } else {

                BackgroundColorCommand backgroundColorCommand = new BackgroundColorCommand(Color.DEFAULT);
                TextColorCommand textColorCommand = new TextColorCommand(Color.DEFAULT);
                TextStyleCommand textStyleCommand = new TextStyleCommand(TextStyle.DEFAULT);

                if (liquidityZone.percentile <= 10) {
                    backgroundColorCommand = new BackgroundColorCommand(Color.CYAN);
                    printer.addCommand(new BackgroundColorCommand(Color.CYAN));
                }

                if (liquidityZone.percentile <= 0.25) {
                   // textStyleCommand = new TextStyleCommand(TextStyle.UNDERLINE);
                }

                if (liquidityZone.powerRatio < -0.49) {
                    textColorCommand = new TextColorCommand(Color.RED);
                } else if (liquidityZone.powerRatio > 0.49) {
                    textColorCommand = new TextColorCommand(Color.BLUE);
                }

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand("" + liquidityZone.price));
                printer.addCommand(new ColumnCommand(6));
                printer.addCommand(new AlignmentCommand(Alignment.LEFT));
                printer.print();

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand(String.format("%,d", liquidityZone.volume)));
                printer.addCommand(new ColumnCommand(16));
                printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
                printer.print();

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand(String.format("%.2f", liquidityZone.relativeVolume)));
                printer.addCommand(new ColumnCommand(8));
                printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
                printer.print();

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand(String.format("%.2f", liquidityZone.volumePercentage)));
                printer.addCommand(new ColumnCommand(7));
                printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
                printer.print();

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand(String.format("%.2f", liquidityZone.powerRatio)));
                printer.addCommand(new ColumnCommand(6));
                printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
                printer.print();

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand("" + liquidityZone.rank));
                printer.addCommand(new ColumnCommand(7));
                printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
                printer.print();

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand(String.format("%.2f", liquidityZone.percentile)));
                printer.addCommand(new ColumnCommand(6));
                printer.addCommand(new AlignmentCommand(Alignment.RIGHT));
                printer.print();

            }
        } else {
            printer.addCommand(new ColumnCommand(56));
            printer.print();
        }

        printer.addCommand(new TextCommand("|"));
        printer.addCommand(new ColumnCommand(3));
        printer.addCommand(new AlignmentCommand(Alignment.CENTER));
        printer.print();
    }

    private void printTableSection(DataReport dataReport) {
        double topCount = 0;

        for (LiquidityZone e : dataReport.topZones) {
            topCount += e.volumePercentage;
        }

        printer.addCommand(new ColumnCommand(176));
        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.WHITE));
        printer.addCommand(new TextCommand("Supply Zones"));
        printer.addCommand(new ColumnCommand(56));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.WHITE));
        printer.addCommand(new TextCommand("|"));
        printer.addCommand(new ColumnCommand(3));
        printer.addCommand(new AlignmentCommand(Alignment.CENTER));
        printer.print();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.WHITE));
        printer.addCommand(new TextCommand("Top Zones " + String.format("%.2f", topCount) + "% Volume"));
        printer.addCommand(new ColumnCommand(56));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.WHITE));
        printer.addCommand(new TextCommand("|"));
        printer.addCommand(new ColumnCommand(3));
        printer.addCommand(new AlignmentCommand(Alignment.CENTER));
        printer.print();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.WHITE));
        printer.addCommand(new TextCommand("Range"));
        printer.addCommand(new ColumnCommand(58));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new ColumnCommand(176));
        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printTableHeader();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand(StringUtils.repeat("-", 176)));
        printer.addCommand(new NewLineCommand());
        printer.print();

        for (int i = 0; i < dataReport.filteredZones.size(); i++) {

            LiquidityZone liquidityZone = dataReport.filteredZones.get(i);
            LiquidityZone topZone = null;
            LiquidityZone range = null;

            if (i < dataReport.topZones.size() - 1) {
                topZone = dataReport.topZones.get(i);
            }

            if (i < dataReport.range.size() - 1) {
                range = dataReport.range.get(i);
            }

            printTableEntry(liquidityZone);
            printTableEntry(topZone);
            printTableEntry(range);

            printer.print();
            printer.addCommand(new NewLineCommand());
            printer.print();

        }

        printer.addCommand(new TextCommand(StringUtils.repeat("-", 182)));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();
    }

    @Override
    public void onDataPresenterCompleted(DataReport dataReport) {
        this.dataReport = dataReport;

        printInfoSection(dataReport);
        printTableSection(dataReport);
    }

    @Override
    public void onPredictionCompleted(List<Node> nodes) {

        printer.addCommand(new TextCommand("Prediction : " + dataReport.ticker));
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("Price"));
        printer.addCommand(new ColumnCommand(12));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new TextCommand("Change"));
        printer.addCommand(new ColumnCommand(12));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new TextCommand("Score"));
        printer.addCommand(new ColumnCommand(12));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();


        printer.addCommand(new NewLineCommand());
        printer.print();

        for (Node e : nodes) {

            printer.addCommand(new TextCommand(""+e.level));
            printer.addCommand(new ColumnCommand(12));
            printer.addCommand(new AlignmentCommand(Alignment.LEFT));
            printer.print();

            printer.addCommand(new TextCommand(String.format("%.2f", e.change)));
            printer.addCommand(new ColumnCommand(12));
            printer.addCommand(new AlignmentCommand(Alignment.LEFT));
            printer.print();

            printer.addCommand(new TextCommand(String.format("%.2f", e.prediction)));
            printer.addCommand(new ColumnCommand(12));
            printer.addCommand(new AlignmentCommand(Alignment.LEFT));
            printer.print();

            printer.addCommand(new NewLineCommand());
            printer.print();

        }

    }
}
