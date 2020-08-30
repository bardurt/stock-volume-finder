package com.zygne.stockalyze;

import com.zygne.stockalyze.domain.model.GapDetails;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Statistics;
import com.zygne.stockalyze.presentation.presenter.base.DataPresenter;
import com.zygne.stockalyze.presentation.presenter.base.PredictionPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.DataPresenterImpl;
import com.zygne.stockalyze.presentation.presenter.implementation.PredictionPresenterImpl;

import java.util.List;

public class App implements DataPresenter.View {


    private DataPresenter dataPresenter;
    private PredictionPresenter predictionPresenter;

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
            System.out.println("trend = -1 down, 0 consolidation, 1 up");
            System.out.println("News = -1 negative, 0 non or generic, 1 significant positive news");
            System.out.println("Gap = 1 Yes, 0 No");
            System.out.println("-----------------------------------------------------------------------------");

            return;
        }

        try {
            currentPrice = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
        }

        dataPresenter = new DataPresenterImpl(this, fileName, currentPrice);
        dataPresenter.start();
    }

    @Override
    public void onDataPresenterCompleted(String ticker, List<LiquidityZone> data, Statistics statistics, GapDetails gapDetails) {
        predictionPresenter = new PredictionPresenterImpl(ticker, data, gapDetails, currentPrice);
        predictionPresenter.start();
    }

}
