package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileCreatorInteractor implements Interactor {

    private static final String FOLDER_NAME = "scripts";

    private final Callback callback;
    private final String content;
    private final String name;

    public FileCreatorInteractor(Callback callback, String content, String name) {
        this.callback = callback;
        this.content = content;
        this.name = name;
    }

    @Override
    public void execute() {

        File file = new File(FOLDER_NAME);

        file.mkdirs();

        String fileName = file.getAbsolutePath() + "/" + name;

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
