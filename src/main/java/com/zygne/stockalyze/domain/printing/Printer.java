package com.zygne.stockalyze.domain.printing;

import com.zygne.stockalyze.domain.printing.command.PrinterCommand;

public interface Printer {

    void addCommand(PrinterCommand printerCommand);
    void print();
}
