package com.zygne.volfinder;

import com.zygne.volfinder.domain.printing.Alignment;
import com.zygne.volfinder.domain.printing.Color;
import com.zygne.volfinder.domain.printing.ConsolePrinter;
import com.zygne.volfinder.domain.printing.Printer;
import com.zygne.volfinder.domain.printing.TextStyle;
import com.zygne.volfinder.domain.printing.command.AlignmentCommand;
import com.zygne.volfinder.domain.printing.command.BackgroundColorCommand;
import com.zygne.volfinder.domain.printing.command.ColumnCommand;
import com.zygne.volfinder.domain.printing.command.NewLineCommand;
import com.zygne.volfinder.domain.printing.command.RepeatCommand;
import com.zygne.volfinder.domain.printing.command.TextColorCommand;
import com.zygne.volfinder.domain.printing.command.TextCommand;
import com.zygne.volfinder.domain.printing.command.TextStyleCommand;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Application {

    private static final int MAX_LENGTH = 1000000;
    private static final double OPEN_WEIGHT = 0.3;
    private static final double HIGH_WEIGHT = 0.2;
    private static final double LOW_WEIGHT = 0.2;
    private static final double CLOSE_WEIGHT = 0.3;
    private static final double FILTER_THRESHOLD_RATIO = 0.001;
    private static final double TOP_VOLUME_THRESHOLD_RATIO = 0.002;
    private static final int PRICE_COLUMN_WIDTH = 6;
    private static final int VOLUME_COLUMN_WIDTH = 16;
    private static final int HEADER_SEPARATOR_LENGTH = 22;

    private final Printer printer = new ConsolePrinter();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
    private final long[] priceArray = new long[MAX_LENGTH];

    public void start(String[] args) {
        long startTime = System.currentTimeMillis();
        if (args.length == 1) {
            if (args[0].equals("-h")) {
                printHelp();
                return;
            }
        }

        if (args.length != 2) {
            System.err.println("Error: requires 2 arguments\n");
            printHelp();
            return;
        }

        String symbol = args[0];
        String apiKey = args[1];

        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + apiKey + "&outputsize=full&datatype=csv";

        long totalVolume = 0;
        try {
            URL content = new java.net.URI(url).toURL();
            try (InputStream stream = content.openStream(); Scanner inputStream = new Scanner(stream)) {
                int count = 0;
                while (inputStream.hasNext()) {

                    String data = inputStream.next();

                    if (count > 1) { // Skip headers
                        String[] tempArr = data.split(",");
                        try {
                            long volume = Long.parseLong(tempArr[5]);
                            totalVolume += volume;
                            int open = (int) (Double.parseDouble(tempArr[1]) * 100);
                            int high = (int) (Double.parseDouble(tempArr[2]) * 100);
                            int low = (int) (Double.parseDouble(tempArr[3]) * 100);
                            int close = (int) (Double.parseDouble(tempArr[4]) * 100);

                            priceArray[open] += (long) (volume * OPEN_WEIGHT);
                            priceArray[high] += (long) (volume * HIGH_WEIGHT);
                            priceArray[low] += (long) (volume * LOW_WEIGHT);
                            priceArray[close] += (long) (volume * CLOSE_WEIGHT);

                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing numeric data: " + e.getMessage());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.err.println("Error processing data row: Insufficient columns in data");
                        }
                    }
                    count++;
                }
            }
        } catch (java.net.URISyntaxException e) {
            System.err.println("Error: Invalid URL syntax - " + e.getMessage());
            return;
        } catch (java.io.IOException e) {
            System.err.println("Error: Unable to fetch data from the URL - " + e.getMessage());
            return;
        }

        long filter = (long) (totalVolume * FILTER_THRESHOLD_RATIO);
        long topVolume = (long) (totalVolume * TOP_VOLUME_THRESHOLD_RATIO);

        long end = System.currentTimeMillis() - startTime;
        System.out.println("Time to run " + end + "ms\n");

        printHeader(symbol);
        processAndPrintResults(filter, topVolume);
    }

    private void printHelp() {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Help");
        System.out.println("arg1 = Asset name ex MSFT");
        System.out.println("arg2 = API KEY from - www.alphavantage.co");
        System.out.println("java -jar main.jar MSFT [API KEY]");
        System.out.println("-----------------------------------------------------------------------------\n");
    }

    private void printHeader(String symbol) {
        printer.addCommand(new TextColorCommand(Color.DEFAULT));
        printer.addCommand(new TextCommand("Symbol : " + symbol + " - " + dateFormat.format(Calendar.getInstance().getTime())));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("Price"));
        printer.addCommand(new ColumnCommand(PRICE_COLUMN_WIDTH));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new TextCommand("Volume"));
        printer.addCommand(new ColumnCommand(VOLUME_COLUMN_WIDTH));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new RepeatCommand("-", HEADER_SEPARATOR_LENGTH));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();
    }

    private void processAndPrintResults(long filter, long topVolume) {
        for (int i = MAX_LENGTH - 1; i > 0; i--) {
            var backgroundColorCommand = new BackgroundColorCommand(Color.DEFAULT);
            TextColorCommand textColorCommand = new TextColorCommand(Color.DEFAULT);
            var textStyleCommand = new TextStyleCommand(TextStyle.DEFAULT);

            if (priceArray[i] > filter) {
                if (priceArray[i] >= topVolume) {
                    backgroundColorCommand = new BackgroundColorCommand(Color.GREEN);
                    textColorCommand = new TextColorCommand(Color.BLACK);
                }

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand("" + i));
                printer.addCommand(new ColumnCommand(PRICE_COLUMN_WIDTH));
                printer.addCommand(new AlignmentCommand(Alignment.LEFT));
                printer.print();

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand(String.format("%,d", priceArray[i])));
                printer.addCommand(new ColumnCommand(VOLUME_COLUMN_WIDTH));
                printer.addCommand(new AlignmentCommand(Alignment.LEFT));
                printer.print();

                printer.addCommand(new NewLineCommand());
                printer.print();
            }
        }
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.start(args);
    }
}