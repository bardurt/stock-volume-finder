package com.zygne.stockalyze;

import com.zygne.stockalyze.presentation.presenter.base.MainPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.MainPresenterImpl;

public class App {


    private MainPresenter mainPresenter;

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

        int currentPrice = 0;

        try {
            currentPrice = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
        }

        mainPresenter = new MainPresenterImpl();
        mainPresenter.start(fileName, currentPrice);
    }

}
