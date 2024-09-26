package com.zygne.volfinder.domain.printing.command;

import com.zygne.volfinder.domain.printing.Alignment;

public class AlignmentCommand extends PrinterCommand {

    private final Alignment alignment;

    public AlignmentCommand(Alignment alignment) {
        this.alignment = alignment;
    }

    public final Alignment getAlignment(){
        return alignment;
    }
}
