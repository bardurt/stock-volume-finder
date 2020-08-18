package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.VolumePriceLevel;

import java.util.List;

public class InstitutionalPriceFormatterInteractor implements Interactor {

    private Callback callback;
    private List<VolumePriceLevel> data;

    public InstitutionalPriceFormatterInteractor(Callback callback, List<VolumePriceLevel> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

//        for (VolumePriceLevel p: data) {
//            p.price = InstitutionalNumberFormatter.formatInstitutional(p.price);
//        }

        callback.onInstitutionalPriceFormatted(data);
    }

    public interface Callback{
        void onInstitutionalPriceFormatted(List<VolumePriceLevel> data);
    }
}
