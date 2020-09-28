package com.zygne.stockalyze.presentation.printing.command;

import com.zygne.stockalyze.presentation.printing.Color;

public class ColorCommand extends PrinterCommand {

    protected final Color color;

    public ColorCommand(Color color) {
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
