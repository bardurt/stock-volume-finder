package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.PowerRatioInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.PowerZone;

import java.util.List;

public class PowerRatioInteractorImpl implements PowerRatioInteractor {

    private Callback callback;
    private List<PowerZone> powerZones;
    private List<LiquidityZone> liquidityZones;

    public PowerRatioInteractorImpl(Callback callback, List<PowerZone> powerZones, List<LiquidityZone> liquidityZones) {
        this.callback = callback;
        this.powerZones = powerZones;
        this.liquidityZones = liquidityZones;
    }

    @Override
    public void execute() {

        int total = powerZones.size();

        if (total == 0) {
            callback.onPowerRatioCreated(liquidityZones);
            return;
        }

        for (LiquidityZone e : liquidityZones) {
            int ratio = 0;
            int count = 0;
            for (PowerZone p : powerZones) {
                if (p.inZone(e.price)) {
                    count++;
                    if (p.type == PowerZone.RECJECT) {
                        ratio--;
                    } else {
                        ratio++;
                    }
                }
            }
            if (ratio != 0) {
                e.powerRatio = ratio / (double) count;
            } else {
                e.powerRatio = 0;
            }

        }

        callback.onPowerRatioCreated(liquidityZones);

    }
}
