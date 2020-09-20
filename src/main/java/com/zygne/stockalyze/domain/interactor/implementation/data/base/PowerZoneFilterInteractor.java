package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.PowerZone;

import java.util.List;

public interface PowerZoneFilterInteractor extends Interactor {

    interface Callback{
        void onPowerZoneFiltered(List<PowerZone> data);
    }
}
