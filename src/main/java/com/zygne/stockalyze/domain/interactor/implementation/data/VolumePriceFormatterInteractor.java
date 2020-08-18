package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.VolumePriceLevel;

import java.util.List;

public class VolumePriceFormatterInteractor implements Interactor {

    private Callback callback;
    private int scalar;
    private List<VolumePriceLevel> data;

    public VolumePriceFormatterInteractor(Callback callback, int scalar, List<VolumePriceLevel> data) {
        this.callback = callback;
        this.scalar = scalar;
        this.data = data;
    }

    @Override
    public void execute() {
        double doubleScalar = Math.pow(10, scalar);




        callback.onVolumePriceFormatted(data);
    }

    public interface Callback {
        void onVolumePriceFormatted(List<VolumePriceLevel> data);
    }
}
