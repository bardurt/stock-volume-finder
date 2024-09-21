package com.zygne.stockalyze.domain.printing.command;

import com.zygne.stockalyze.domain.printing.TextStyle;

public class TextStyleCommand extends PrinterCommand {

    private final TextStyle textStyle;

    public TextStyleCommand(TextStyle textStyle) {
        this.textStyle = textStyle;
    }

    public final TextStyle getTextStyle() {
        return textStyle;
    }
}
