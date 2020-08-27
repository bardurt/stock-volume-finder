package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.VolumePriceLevel;

import java.util.List;

public interface VolumePriceInteractor extends Interactor {

    interface Callback {
        void onVolumePriceCreated(List<VolumePriceLevel> data);
    }
}
