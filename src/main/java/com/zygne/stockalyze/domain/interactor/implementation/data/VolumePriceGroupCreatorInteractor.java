package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.VolumePriceLevel;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;

import java.util.*;

public class VolumePriceGroupCreatorInteractor implements Interactor {

    private Callback callback;
    private List<VolumePriceLevel> data;

    public VolumePriceGroupCreatorInteractor(Callback callback, List<VolumePriceLevel> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        Map<String, VolumePriceGroup> map = new HashMap<>();


        for (VolumePriceLevel e : data) {

            String tag = "p" + e.price;

            if (map.get(tag) != null) {
                map.get(tag).orderCount++;
                map.get(tag).totalSize += e.size;
            } else {
                map.put(tag, new VolumePriceGroup(e.price, e.size));
            }
        }

        List<VolumePriceGroup> groups = new ArrayList<>(map.values());

        Collections.sort(groups);
        Collections.reverse(groups);
        callback.onVolumePriceGroupCreated(groups);

    }

    public interface Callback{
        void onVolumePriceGroupCreated(List<VolumePriceGroup> data);
    }
}
