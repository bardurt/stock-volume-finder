package com.zygne.volfinder.domain.printing;

import com.zygne.volfinder.domain.printing.command.PrinterCommand;

public interface Printer {

    void addCommand(PrinterCommand printerCommand);
    void print();
}
