package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.utils.StringHelper;
import com.zygne.stockalyze.domain.utils.TimeHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvHistogramInteractor implements Interactor {
    private static final String delimiter = ",";

    private final Callback callback;
    private String filePath;

    public CsvHistogramInteractor(Callback callback, String filePath) {
        this.callback = callback;
        this.filePath = filePath;
    }

    @Override
    public void execute() {

        String tickerName = StringHelper.getTickerNameFromCsv(filePath);
        List<Histogram> data = new ArrayList<>();
        String year1 = "";
        String year2 = "";

        File file;
        FileReader fileReader = null;

        try {
            file = new File(filePath);
            fileReader = new FileReader(file);
        } catch (Exception e) {
            System.out.println("Error - Could not load the file : " + filePath);
        }


        if (fileReader != null) {
            try {
                BufferedReader br = new BufferedReader(fileReader);
                String line;
                String[] tempArr;
                int count = -1;
                while ((line = br.readLine()) != null) {
                    count++;

                    if (count == 0) {
                        continue;
                    }
                    tempArr = line.split(delimiter);

                    if (count == 1) {
                        year1 = tempArr[0];
                    }

                    int volume = Integer.parseInt(tempArr[6]);
                    double open = Double.parseDouble(tempArr[1]);
                    double high = Double.parseDouble(tempArr[2]);
                    double low = Double.parseDouble(tempArr[3]);
                    double close = Double.parseDouble(tempArr[3]);

                    Histogram histogram = new Histogram();
                    histogram.open = (int) (open * 100);
                    histogram.high = (int) (high * 100);
                    histogram.low = (int) (low * 100);
                    histogram.close = (int) (close * 100);
                    histogram.volume = volume;

                    data.add(histogram);
                    year2 = tempArr[0];
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int months = TimeHelper.getMonthsDifference(year1, year2);

        callback.onHistogramLoaded(data, tickerName, months);

    }

    public interface Callback {
        void onHistogramLoaded(List<Histogram> data, String tickerName, int timeSpan);
    }
}
