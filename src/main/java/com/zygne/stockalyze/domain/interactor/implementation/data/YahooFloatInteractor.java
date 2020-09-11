package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.StockFloatInteractor;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class YahooFloatInteractor implements StockFloatInteractor {

    private static final String FLOAT_TAG = "floatShares";
    private final Callback callback;
    private String ticker;

    public YahooFloatInteractor(Callback callback, String ticker){
        this.callback = callback;
        this.ticker = ticker;
    }

    @Override
    public void execute() {
        ticker = ticker.toUpperCase();

        String url = "https://finance.yahoo.com/quote/"+ticker +"/key-statistics?p="+ticker;


        String floatString = null;
        try {
            URL content = new URL(url);
            InputStream stream = content.openStream();

            Scanner inputStream = new Scanner(stream);
            while (inputStream.hasNext()) {

                String data = inputStream.next();

                if(data.contains(FLOAT_TAG)){
                    floatString = data;
                    break;
                }
            }
            inputStream.close();
        } catch (Exception e){
            return;
        }

        if(floatString != null){
            int stockFloat = getFloatFromRaw(floatString);
            callback.onStockFloatFetched(stockFloat);
        }
    }

    private int getFloatFromRaw(String raw){
        String[] parts = raw.split(",", -1);

        String floatString = "";

        for(String s : parts){
            if(s.contains(FLOAT_TAG)){
                floatString = s;
                break;
            }
        }

        int stockFloat = -1;

        try {
            stockFloat = Integer.parseInt(floatString.split(":")[2]);
        } catch (Exception e){
            System.out.println("Error getting float for stock : " + ticker);
        }

        return stockFloat;
    }

    public static void main(String[] args) {

        new YahooFloatInteractor(stockFloat -> System.out.println("Stock float : " + stockFloat), "vive").execute();
    }
}
