package com.zygne.volfinder.domain.printing.command;

public class TextCommand extends PrinterCommand {

    private final String content;

    public TextCommand(String content) {
        this.content = content;
    }

    public final String getContent(){
        return content;
    }
}
