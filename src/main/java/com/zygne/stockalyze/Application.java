package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;
import com.zygne.stockalyze.domain.printing.Alignment;
import com.zygne.stockalyze.domain.printing.Color;
import com.zygne.stockalyze.domain.printing.ConsolePrinter;
import com.zygne.stockalyze.domain.printing.Printer;
import com.zygne.stockalyze.domain.printing.TextStyle;
import com.zygne.stockalyze.domain.printing.command.AlignmentCommand;
import com.zygne.stockalyze.domain.printing.command.BackgroundColorCommand;
import com.zygne.stockalyze.domain.printing.command.ColumnCommand;
import com.zygne.stockalyze.domain.printing.command.NewLineCommand;
import com.zygne.stockalyze.domain.printing.command.TextColorCommand;
import com.zygne.stockalyze.domain.printing.command.TextCommand;
import com.zygne.stockalyze.domain.printing.command.TextStyleCommand;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Application {

    private static final float FILTER_PERCENTILE = 15.0f;
    private static final float FILTER_TOP_ZONES = 5.0f;

    private final Printer printer = new ConsolePrinter();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

    public void start(String[] args) {
        long startTime = System.currentTimeMillis();
        if (args.length == 1) {
            if (args[0].equals("-h")) {
                System.out.println("-----------------------------------------------------------------------------");
                System.out.println("Help");
                System.out.println("arg1 = Asset name ex MSFT");
                System.out.println("arg2 = API KEY from - www.alphavantage.co");
                System.out.println("java -jar main.jar MSFT [API KEY]");
                System.out.println("-----------------------------------------------------------------------------");

                return;
            }
        }

        if (args.length != 2) {
            System.out.println("Error, requires 2 arguments");
            return;
        }

        String symbol = args[0];
        String apiKey = args[1];

        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + apiKey + "&outputsize=full&datatype=csv";

        List<String> lines = new ArrayList<>();
        try {
            URL content = new URL(url);
            InputStream stream = content.openStream();

            Scanner inputStream = new Scanner(stream);
            int count = 0;
            while (inputStream.hasNext()) {

                String data = inputStream.next();

                if (count > 1) {
                    lines.add(data);
                }

                count++;
            }
            inputStream.close();
        } catch (Exception e) {
            System.out.println("Could not fetch data from : " + url);
            return;
        }

        if (lines.isEmpty()) {
            System.out.println("Could not fetch data from : " + url);
            return;
        }

        if (lines.size() < 50) {
            for (String s : lines) {
                System.out.print(s + " ");
            }
            System.out.println();
            return;
        }

        String[] tempArr;

        long totalVolume = 0;
        int count = 0;
        Map<String, VolumePriceGroup> map = new HashMap<>();
        for (String line : lines) {

            tempArr = line.split(",");

            try {

                long volume = Long.parseLong(tempArr[5]);
                // Multiply by 100 to get the cent value
                int open = (int) (Double.parseDouble(tempArr[1]) * 100);
                int high = (int) (Double.parseDouble(tempArr[2]) * 100);
                int low = (int) (Double.parseDouble(tempArr[3]) * 100);
                int close = (int) (Double.parseDouble(tempArr[4]) * 100);

                totalVolume += volume;

                String tag1 = "p" + open;

                if (map.get(tag1) != null) {
                    map.get(tag1).totalSize += volume * 0.3;
                } else {
                    map.put(tag1, new VolumePriceGroup(open, (long) (volume * 0.3)));
                }

                String tag2 = "p" + high;

                if (map.get(tag2) != null) {
                    map.get(tag2).totalSize += volume * 0.2;
                } else {
                    map.put(tag2, new VolumePriceGroup(high, (long) (volume * 0.2)));
                }

                String tag3 = "p" + high;

                if (map.get(tag3) != null) {
                    map.get(tag3).totalSize += volume * 0.2;
                } else {
                    map.put(tag3, new VolumePriceGroup(low, (long) (volume * 0.2)));
                }

                String tag4 = "p" + close;

                if (map.get(tag4) != null) {
                    map.get(tag4).totalSize += volume * 0.3;
                } else {
                    map.put(tag4, new VolumePriceGroup(close, (long) (volume * 0.3)));
                }

            } catch (Exception e) {
                System.out.println("Error at line " + count);
            }

            count++;
        }

        List<VolumePriceGroup> groups = new ArrayList<>(map.values());

        List<LiquidityZone> liquidityZones = new ArrayList<>();

        for (VolumePriceGroup e : groups) {
            LiquidityZone s = new LiquidityZone(e.price, e.totalSize);
            s.volumePercentage = (e.totalSize / (double) totalVolume) * 100;
            liquidityZones.add(s);
        }

        liquidityZones.sort(new LiquidityZone.VolumeComparator());
        Collections.reverse(liquidityZones);

        int size = liquidityZones.size();
        double currentPercent = 0;
        for (int i = 0; i < liquidityZones.size(); i++) {
            liquidityZones.get(i).rank = i + 1;
            liquidityZones.get(i).percentile = ((i + 1) / (double) size) * 100;
            if (liquidityZones.get(i).percentile < FILTER_PERCENTILE) {
                liquidityZones.get(i).visible = true;
            }
            currentPercent += liquidityZones.get(i).volumePercentage;
            if (currentPercent < FILTER_TOP_ZONES) {
                liquidityZones.get(i).top = true;
            }
        }

        print(symbol, liquidityZones);

        long end = System.currentTimeMillis() - startTime;
        System.out.println("Time to run " + end + "ms");
    }

    private void print(String symbol, List<LiquidityZone> items) {
        printInfoSection(symbol);

        List<LiquidityZone> zones = filterList(items, false);
        List<LiquidityZone> top = filterList(items, true);
        printTableSection(top, zones);
    }

    private List<LiquidityZone> filterList(List<LiquidityZone> raw, boolean top) {
        List<LiquidityZone> items = new ArrayList<>();

        if (!top) {
            raw.sort(new LiquidityZone.PriceComparator());
            Collections.reverse(raw);

            for (LiquidityZone l : raw) {
                if (l.visible) {
                    items.add(l);
                }
            }

        } else {
            raw.sort(new LiquidityZone.VolumeComparator());
            Collections.reverse(raw);

            for (LiquidityZone l : raw) {
                if (l.top) {
                    items.add(l);
                }
            }
        }

        return items;
    }

    private void printInfoSection(String symbol) {

        printer.addCommand(new TextColorCommand(Color.DEFAULT));
        printer.addCommand(new TextCommand("Symbol : " + symbol + " - " + dateFormat.format(Calendar.getInstance().getTime())));
        printer.addCommand(new NewLineCommand());
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
            printer.addCommand(new AlignmentCommand(Alignment.LEFT));
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

            if (liquidityZone.percentile <= 3) {
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
            printer.addCommand(new AlignmentCommand(Alignment.LEFT));
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
            printer.addCommand(new ColumnCommand(42));
            printer.print();
        }

        printer.addCommand(new TextCommand("|"));
        printer.addCommand(new ColumnCommand(3));
        printer.addCommand(new AlignmentCommand(Alignment.CENTER));
        printer.print();
    }

    private void printTableSection(List<LiquidityZone> topZones, List<LiquidityZone> filteredZones) {

        printer.addCommand(new ColumnCommand(89));
        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.DEFAULT));
        printer.addCommand(new TextCommand("Supply Zones, top " + String.format("%.2f", FILTER_PERCENTILE) + "% Volume"));
        printer.addCommand(new ColumnCommand(42));
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
        printer.addCommand(new TextCommand("Top Zones " + String.format("%.2f", FILTER_TOP_ZONES) + "% of total Volume"));
        printer.addCommand(new ColumnCommand(44));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();


        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new ColumnCommand(89));
        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printTableHeader();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new com.zygne.stockalyze.domain.printing.command.RepeatCommand("-", 89));
        printer.addCommand(new NewLineCommand());
        printer.print();

        for (int i = 0; i < filteredZones.size(); i++) {

            LiquidityZone liquidityZone = filteredZones.get(i);
            LiquidityZone topZone = null;

            if (i < topZones.size() - 1) {
                topZone = topZones.get(i);
            }

            printTableEntry(liquidityZone);
            printTableEntry(topZone);

            printer.print();
            printer.addCommand(new NewLineCommand());
            printer.print();

        }

        printer.addCommand(new com.zygne.stockalyze.domain.printing.command.RepeatCommand("-", 89));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.start(args);
    }
}
