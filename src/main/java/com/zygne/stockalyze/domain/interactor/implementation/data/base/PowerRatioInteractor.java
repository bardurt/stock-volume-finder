package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.List;

public interface PowerRatioInteractor extends Interactor {

    interface Callback{
        void onPowerRatioCreated(List<LiquidityZone> data);
    }
}
