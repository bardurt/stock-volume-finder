package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.model.enums.MarketTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class NyseMarketTimeInteractor implements MarketTimeInteractor {

    private final Callback callback;

    public NyseMarketTimeInteractor(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void execute() {
        SimpleDateFormat etDf = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mma 'ET'");
        TimeZone etTimeZone = TimeZone.getTimeZone("America/New_York");

        //Set ET timezone
        etDf.setTimeZone(etTimeZone);

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeZone(etTimeZone);

        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        MarketTime marketTime = getMarketTime(hour, minute);

        System.out.println();

        callback.onMarketTimeValidated(marketTime);

    }

    private MarketTime getMarketTime(int hour, int minute){

        if (hour == 9) {

            if (minute >= 20 && minute <= 40) {
                return MarketTime.MARKET_OPEN;
            }

        }

        if (hour >= 6 && hour < 10) {

            if(hour > 6 && hour < 9){
                return MarketTime.PRE_MARKET;
            }

            if(hour == 6 && minute >= 30){
                return MarketTime.PRE_MARKET;
            }

            if(hour == 9 && minute <= 20){
                return MarketTime.PRE_MARKET;
            }
        }

        if (hour == 15) {

            if(minute >= 45) {
                return MarketTime.CLOSING_SESSION;
            }
        }

        if (hour >= 15 && hour <= 16) {
            return MarketTime.POWER_HOUR;
        }

        if (hour >= 9 && hour < 16) {
            return MarketTime.REGULAR_HOURS;
        }

        if (hour >= 16 && hour < 20) {
            return MarketTime.AFTER_HOURS;
        }

        return MarketTime.CLOSED;
    }


    public static void main(String[] args) {

        new NyseMarketTimeInteractor(marketTime -> System.out.println("Market Time : " + marketTime.label)).execute();
    }
}
