package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.utils.Timer;
import com.zygne.stockalyze.presentation.printing.*;
import com.zygne.stockalyze.presentation.presenter.base.DataPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.DataPresenterImpl;
import com.zygne.stockalyze.presentation.printing.command.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class App implements DataPresenter.View {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");

    private final Printer printer = new ConsolePrinter();
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


        timer.start();
        DataPresenter dataPresenter = new DataPresenterImpl(this, fileName, 0);
        dataPresenter.start();
    }

    private void printInfoSection(DataReport dataReport) {

        printer.addCommand(new TextColorCommand(Color.DEFAULT));
        printer.addCommand(new TextCommand("DataReport : " + dataReport.ticker + " - " + dateFormat.format(Calendar.getInstance().getTime())));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("Ticker : " + dataReport.ticker + "\t Time Span : " + dataReport.timeSpan + " Days" + "\t Mean Vol : " + ((int) dataReport.statistics.mean)));
        printer.addCommand(new NewLineCommand());
        printer.print();
    }

    private void printTableHeader() {
        for (int i = 0; i < 2; i++) {

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

            BackgroundColorCommand backgroundColorCommand = new BackgroundColorCommand(Color.DEFAULT);
            TextColorCommand textColorCommand = new TextColorCommand(Color.DEFAULT);
            TextStyleCommand textStyleCommand = new TextStyleCommand(TextStyle.DEFAULT);

            if (liquidityZone.percentile <= 10) {
                backgroundColorCommand = new BackgroundColorCommand(Color.GREEN);
                printer.addCommand(backgroundColorCommand);
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

        } else {
            printer.addCommand(new ColumnCommand(50));
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

        printer.addCommand(new ColumnCommand(105));
        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.DEFAULT));
        printer.addCommand(new TextCommand("Supply Zones"));
        printer.addCommand(new ColumnCommand(50));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.DEFAULT));
        printer.addCommand(new TextCommand("|"));
        printer.addCommand(new ColumnCommand(3));
        printer.addCommand(new AlignmentCommand(Alignment.CENTER));
        printer.print();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.DEFAULT));
        printer.addCommand(new TextCommand("Top Zones " + String.format("%.2f", topCount) + "% Volume"));
        printer.addCommand(new ColumnCommand(52));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();


        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new ColumnCommand(105));
        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printTableHeader();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand(StringUtils.repeat("-", 105)));
        printer.addCommand(new NewLineCommand());
        printer.print();

        for (int i = 0; i < dataReport.filteredZones.size(); i++) {

            LiquidityZone liquidityZone = dataReport.filteredZones.get(i);
            LiquidityZone topZone = null;

            if (i < dataReport.topZones.size() - 1) {
                topZone = dataReport.topZones.get(i);
            }

            printTableEntry(liquidityZone);
            printTableEntry(topZone);

            printer.print();
            printer.addCommand(new NewLineCommand());
            printer.print();

        }

        printer.addCommand(new TextCommand(StringUtils.repeat("-", 105)));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();
    }

    @Override
    public void onDataPresenterCompleted(DataReport dataReport) {
        printInfoSection(dataReport);
        printTableSection(dataReport);
    }
}
