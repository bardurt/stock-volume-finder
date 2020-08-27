package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;

import java.util.List;

public interface VolumePriceGroupInteractor extends Interactor {

    interface Callback{
        void onVolumePriceGroupCreated(List<VolumePriceGroup> data);
    }
}
