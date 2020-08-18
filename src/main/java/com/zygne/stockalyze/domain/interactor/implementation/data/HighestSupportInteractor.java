package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.SupplyZone;

import java.util.ArrayList;
import java.util.List;

public class HighestSupportInteractor implements Interactor {

    private Callback callback;
    private List<SupplyZone> data;
    private int currentPrice;

    public HighestSupportInteractor(Callback callback, List<SupplyZone> data, int currentPrice) {
        this.callback = callback;
        this.data = data;
        this.currentPrice = currentPrice;
    }

    @Override
    public void execute() {

        int maxSupportZones = 5;

        int currentSupportZones = 0;
        List<SupplyZone> filteredList = new ArrayList<>();

        int highestSupportIndex = 0;
        int highestSupport = 0;


        int index = 0;
        for (SupplyZone e : data) {
            index++;
            if (e.price < currentPrice) {
                if (currentPrice / e.price < 2) {
                    if (e.totalSize > highestSupport) {
                        highestSupport = e.totalSize;
                        highestSupportIndex = index;
                    }
                }
            }
        }

        for (int i = 0; i < data.size(); i++) {

            if (i == highestSupportIndex) {
                break;
            }

            filteredList.add(data.get(i));
        }


        callback.onHighestSupportFound(filteredList);

    }


    public interface Callback {
        void onHighestSupportFound(List<SupplyZone> data);
    }
}
