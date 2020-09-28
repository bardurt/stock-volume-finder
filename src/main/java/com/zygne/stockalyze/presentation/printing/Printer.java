package com.zygne.stockalyze.presentation.printing;

import com.zygne.stockalyze.presentation.printing.command.PrinterCommand;

public interface Printer {

    void addCommand(PrinterCommand printerCommand);
    void print();
}
