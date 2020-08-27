package com.zygne.stockalyze.domain.interactor.implementation.scripting;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

public interface ScriptInteractor extends Interactor {

    interface Callback{
        void onScriptCreated(String script, String name);
    }
}
