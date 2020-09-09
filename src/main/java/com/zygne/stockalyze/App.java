package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.model.GapDetails;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.enums.MarketTime;
import com.zygne.stockalyze.presentation.presenter.base.DataPresenter;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.DataPresenterImpl;
import com.zygne.stockalyze.presentation.presenter.implementation.PredictionPresenterImpl;

import java.util.List;

public class App implements DataPresenter.View {


    private int currentPrice = 0;
    private String ticker;

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
    public void onDataPresenterCompleted(String ticker, List<LiquidityZone> data, GapDetails gapDetails, MarketTime marketTime) {
        if(currentPrice > 0) {
            PredictionPresenter predictionPresenter = new PredictionPresenterImpl(ticker, data, gapDetails, currentPrice, marketTime);
            predictionPresenter.start();
        }
    }

}
