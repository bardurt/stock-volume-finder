package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileCreatorInteractor implements Interactor {

    private static final String FOLDER_NAME = "scripts";

    private final Callback callback;
    private final String content;
    private final String folder;
    private final String name;

    public FileCreatorInteractor(Callback callback, String content, String folder, String name) {
        this.callback = callback;
        this.content = content;
        this.folder = folder;
        this.name = name;
    }

    @Override
    public void execute() {


        String fileName = folder + "/" + name;

        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException ignored) {

        }

        callback.onFileCreated(fileName);

    }

    public interface Callback{
        void onFileCreated(String message);
    }
}
