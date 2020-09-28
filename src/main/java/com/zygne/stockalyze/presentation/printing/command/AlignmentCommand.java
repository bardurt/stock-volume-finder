package com.zygne.stockalyze.presentation.printing.command;

import com.zygne.stockalyze.presentation.printing.Alignment;

public class AlignmentCommand extends PrinterCommand {

    private final Alignment alignment;

    public AlignmentCommand(Alignment alignment) {
        this.alignment = alignment;
    }

    public final Alignment getAlignment(){
        return alignment;
    }
}
