package com.zygne.stockalyze.domain.model;

public class VolumePriceLevel {

    public final int price;
    public final long size;

    public VolumePriceLevel(int price, long size) {
        this.size = size;
        this.price = price;
    }
}
