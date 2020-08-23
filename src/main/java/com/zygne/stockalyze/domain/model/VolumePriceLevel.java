package com.zygne.stockalyze.domain.model;

public class VolumePriceLevel {

    public int price;
    public long size;

    public VolumePriceLevel(int price, long size) {
        this.size = size;
        this.price = price;
    }
}
