package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

public interface ScriptInteractor extends Interactor {

    public interface Callback{
        void onScriptCreated(String script);
    }
}
