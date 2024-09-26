package com.zygne.volfinder.domain.printing.command;

public class RepeatCommand extends PrinterCommand {

    private final String content;

    public RepeatCommand(String value, int count) {
        this.content = String.valueOf(value).repeat(Math.max(0, count));
    }

    public final String getContent() {
        return content;
    }
}