package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockalyze.domain.utils.StringHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderInteractor implements DataFetchInteractor {
    private static final String delimiter = ",";

    private final Callback callback;
    private String filePath;

    public CsvReaderInteractor(Callback callback, String filePath) {
        this.callback = callback;
        this.filePath = filePath;
    }

    @Override
    public void execute() {

        String tickerName = StringHelper.getTickerNameFromCsv(filePath);
        List<String> data = new ArrayList<>();

        File file;
        FileReader fileReader = null;

        try {
            file = new File(filePath);
            fileReader = new FileReader(file);
        } catch (Exception e) {
            callback.onDataFetchError("Could not load data from file : " + filePath );
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
                    data.add(line);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
                callback.onDataFetchError("Could not load data from file : " + filePath );
            }
        }

        callback.onDataFetched(data, tickerName);

    }
}
