package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.DecayInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.utils.TimeHelper;

import java.util.List;

public class DecayInteractorImpl implements DecayInteractor {

    private static final double MAX_DECAY = 0.25;

    private final Callback callback;
    private final List<Histogram> data;
    private final int timeFrame;

    public DecayInteractorImpl(Callback callback, List<Histogram> data, int timeFrame) {
        this.callback = callback;
        this.data = data;
        this.timeFrame = timeFrame;
    }

    @Override
    public void execute() {

        long now = System.currentTimeMillis();

        double decay = 1;
        for (Histogram e : data){

            double diff = TimeHelper.getDaysDifference(now, e.timeStamp) / (double) timeFrame;
            decay = 1 - (MAX_DECAY * diff);

            System.out.println("Decay " + decay);
            e.decay = decay;
        }

        callback.onDecayCalculated(data);
    }
}
