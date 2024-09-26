package com.zygne.volfinder.domain.printing.command;

import com.zygne.volfinder.domain.printing.Color;

public class ColorCommand extends PrinterCommand {

    protected final Color color;

    public ColorCommand(Color color) {
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
