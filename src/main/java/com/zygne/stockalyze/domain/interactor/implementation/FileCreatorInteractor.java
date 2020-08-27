package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

import java.io.FileWriter;
import java.io.IOException;

public class FileCreatorInteractor implements Interactor {

    private Callback callback;
    private String content;
    private String name;

    public FileCreatorInteractor(Callback callback, String content, String name) {
        this.callback = callback;
        this.content = content;
        this.name = name;
    }

    @Override
    public void execute() {

        String fileName = name;
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {

        }

        callback.onFileCreated(fileName);

    }

    public interface Callback{
        void onFileCreated(String message);
    }
}
