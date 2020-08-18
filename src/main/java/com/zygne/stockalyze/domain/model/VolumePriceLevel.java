package com.zygne.stockalyze.domain.model;

public class VolumePriceLevel {

    public int price;
    public int size;

    public VolumePriceLevel(int price, int size) {
        this.size = size;
        this.price = price;
    }
}
