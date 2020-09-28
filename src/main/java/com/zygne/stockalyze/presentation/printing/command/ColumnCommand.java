package com.zygne.stockalyze.presentation.printing.command;

public class ColumnCommand extends PrinterCommand {

    private final int width;

    public ColumnCommand(int width) {
        this.width = width;
    }

    public final int getWidth(){
        return width;
    }
}
