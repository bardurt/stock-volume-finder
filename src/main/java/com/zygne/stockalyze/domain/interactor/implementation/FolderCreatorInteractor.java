package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

import java.io.File;

public class FolderCreatorInteractor implements Interactor {

    private Callback callback;
    private String name;

    public FolderCreatorInteractor(Callback callback, String name) {
        this.callback = callback;
        this.name = name;
    }

    @Override
    public void execute() {

        File file = new File(name);

        file.mkdirs();

        String path = file.getAbsolutePath() + "/";


        callback.onFolderCreated(path);

    }

    public interface Callback{
        void onFolderCreated(String path);
    }
}
