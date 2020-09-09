package com.zygne.stockalyze.domain.model.enums;

public enum  MarketTime {
    PRE_MARKET("Pre Market"),
    MARKET_OPEN("Market Open"),
    REGULAR_HOURS("Regular Hours"),
    POWER_HOUR("Power Hour"),
    CLOSING_SESSION("Closing Session"),
    AFTER_HOURS("After Hours"),
    CLOSED("Closed");

    public final String label;

    MarketTime(String label) {
        this.label = label;
    }
}
