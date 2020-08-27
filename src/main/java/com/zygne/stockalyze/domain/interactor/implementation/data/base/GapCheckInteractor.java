package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

public interface GapCheckInteractor extends Interactor {

    interface Callback{
        void onGapValidated(boolean gapUp);
    }
}
