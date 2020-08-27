package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.TimeFrame;
import com.zygne.stockalyze.domain.utils.TimeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistogramInteractorImpl implements HistogramInteractor {
    private static final String delimiter = ",";

    private Callback callback;
    private List<String> entries;

    public HistogramInteractorImpl(Callback callback, List<String> entries){
        this.callback = callback;
        this.entries = entries;
    }

    @Override
    public void execute() {

        String[] tempArr;
        List<Histogram> data = new ArrayList<>();
        String year1 = "";
        String year2 = "";
        int count = 0;
        for(String line : entries){

            tempArr = line.split(delimiter);

            if (count == 0) {
                year1 = tempArr[0];
            }

            try {

                long volume = Long.parseLong(tempArr[6]);
                double open = Double.parseDouble(tempArr[1]);
                double high = Double.parseDouble(tempArr[2]);
                double low = Double.parseDouble(tempArr[3]);
                double close = Double.parseDouble(tempArr[4]);

                Histogram histogram = new Histogram();
                histogram.timeStamp = TimeHelper.getTimeStamp(tempArr[0]);
                histogram.open = (int) (open * 100);
                histogram.high = (int) (high * 100);
                histogram.low = (int) (low * 100);
                histogram.close = (int) (close * 100);
                histogram.volume = volume;
                histogram.timeFrame = TimeFrame.Day;
                data.add(histogram);
                year2 = tempArr[0];

            } catch (Exception e){
                System.out.println("Error at line " + count);
            }

            count++;
        }

        int months = TimeHelper.getMonthsDifference(year1, year2);

        Collections.sort(data);

        callback.onHistogramCreated(data, months);
    }

}
