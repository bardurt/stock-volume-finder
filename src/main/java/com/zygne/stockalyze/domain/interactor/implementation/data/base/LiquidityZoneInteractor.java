package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.List;

public interface LiquidityZoneInteractor extends Interactor {

    interface Callback{
        void onLiquidityZonesCreated(List<LiquidityZone> data);
    }
}
