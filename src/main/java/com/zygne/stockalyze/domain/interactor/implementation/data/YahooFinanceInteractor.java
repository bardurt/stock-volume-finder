package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataFetchInteractor;

import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class YahooFinanceInteractor implements DataFetchInteractor {

    private static final String delimiter = ",";

    private Callback callback;
    private String ticker;

    public YahooFinanceInteractor(Callback callback, String ticker){
        this.callback = callback;
        this.ticker = ticker;
    }

    @Override
    public void execute() {

        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        Instant fiveYears = now.minus(1826, ChronoUnit.DAYS);

        String timeTo = "" + yesterday.getEpochSecond();
        String timeFrom = "" + fiveYears.getEpochSecond();


        ticker = ticker.toUpperCase();
        String url = "https://query1.finance.yahoo.com/v7/finance/download/" + ticker + "?period1="+ timeFrom +"&period2=" + timeTo + "&interval=1d&events=history";

        System.out.println("Fetching data from " + url);

        List<String> lines = new ArrayList<>();
        try {
            URL content = new URL(url);
            InputStream stream = content.openStream();

            Scanner inputStream = new Scanner(stream);
            int count = 0;
            while (inputStream.hasNext()) {

                String data = inputStream.next();

                if(count > 1){
                    lines.add(data);
                }

                count++;
            }
            inputStream.close();
        } catch (Exception e){
            callback.onDataFetchError("Could not fetch data from : " + url);
            return;
        }

        if(lines.isEmpty()){
            callback.onDataFetchError("Could not fetch data from : " + url);
            return;
        }

        callback.onDataFetched(lines, ticker);

    }


}
