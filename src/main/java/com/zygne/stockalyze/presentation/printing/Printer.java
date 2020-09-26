package com.zygne.stockalyze.presentation.printing;

public interface Printer {

    void setBackgroundColor(Color color);
    void setTextColor(Color color);
    void print(String value);
}
