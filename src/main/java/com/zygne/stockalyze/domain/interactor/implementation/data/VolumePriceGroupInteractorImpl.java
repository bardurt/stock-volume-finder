package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.VolumePriceGroupInteractor;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;
import com.zygne.stockalyze.domain.model.VolumePriceLevel;

import java.util.*;

public class VolumePriceGroupInteractorImpl implements VolumePriceGroupInteractor {

    private final Callback callback;
    private final List<VolumePriceLevel> data;

    public VolumePriceGroupInteractorImpl(Callback callback, List<VolumePriceLevel> data) {
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
}
