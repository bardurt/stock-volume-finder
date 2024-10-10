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

    private final Printer printer = new ConsolePrinter();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

    private final int MAX_LENGTH = 1000000;

    private final long[] priceArray = new long[MAX_LENGTH];

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

        long totalVolume = 0;
        try {
            URL content = new java.net.URI(url).toURL();
            InputStream stream = content.openStream();

            Scanner inputStream = new Scanner(stream);
            int count = 0;
            while (inputStream.hasNext()) {

                String data = inputStream.next();

                // first item is header data
                if (count > 1) {
                    String[] tempArr = data.split(",");
                    try {
                        long volume = Long.parseLong(tempArr[5]);
                        totalVolume += volume;
                        // Multiply by 100 to get the cent value
                        int open = (int) (Double.parseDouble(tempArr[1]) * 100);
                        int high = (int) (Double.parseDouble(tempArr[2]) * 100);
                        int low = (int) (Double.parseDouble(tempArr[3]) * 100);
                        int close = (int) (Double.parseDouble(tempArr[4]) * 100);

                        priceArray[open] += (long) (volume * 0.3);
                        priceArray[high] += (long) (volume * 0.2);
                        priceArray[low] += (long) (volume * 0.2);
                        priceArray[close] += (long) (volume * 0.3);

                    } catch (Exception e) {
                        System.out.println("Error parsing data: " + e.getMessage());
                    }
                }
                count++;
            }
            inputStream.close();
        } catch (Exception e) {
            System.out.println("Could not fetch data from : " + url);
            return;
        }


        // filters for getting prices that represent a % of total volume
        long filter = (long) (totalVolume * 0.001);
        long topVolume = (long) (totalVolume * 0.002);

        long end = System.currentTimeMillis() - startTime;
        System.out.println("Time to run " + end + "ms");

        printer.addCommand(new TextColorCommand(Color.DEFAULT));
        printer.addCommand(new TextCommand("Symbol : " + symbol + " - " + dateFormat.format(Calendar.getInstance().getTime())));
        printer.addCommand(new NewLineCommand());
        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new TextCommand("Price"));
        printer.addCommand(new ColumnCommand(6));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new TextCommand("Volume"));
        printer.addCommand(new ColumnCommand(16));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        printer.addCommand(new RepeatCommand("-", 22));
        printer.print();

        printer.addCommand(new NewLineCommand());
        printer.print();

        for(int i = MAX_LENGTH-1; i > 0; i--){
            var backgroundColorCommand = new BackgroundColorCommand(Color.DEFAULT);
            TextColorCommand textColorCommand = new TextColorCommand(Color.DEFAULT);
            var textStyleCommand = new TextStyleCommand(TextStyle.DEFAULT);

            if(priceArray[i] > filter) {
                if(priceArray[i] >= topVolume){
                    backgroundColorCommand = new BackgroundColorCommand(Color.GREEN);
                    textColorCommand = new TextColorCommand(Color.BLACK);
                }

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand("" + i));
                printer.addCommand(new ColumnCommand(6));
                printer.addCommand(new AlignmentCommand(Alignment.LEFT));
                printer.print();

                printer.addCommand(backgroundColorCommand);
                printer.addCommand(textColorCommand);
                printer.addCommand(textStyleCommand);
                printer.addCommand(new TextCommand(String.format("%,d", priceArray[i])));
                printer.addCommand(new ColumnCommand(16));
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
