package com.zygne.stockalyze.domain.model;

public class SupplyZone implements Comparable {

    public int price;
    public int totalSize;
    public int orderCount;
    public double relativeVolume;
    public String note = "";

    public SupplyZone(int price, int totalSize, int orderCount) {
        this.price = price;
        this.totalSize = totalSize;
        this.orderCount = orderCount;
    }

    @Override
    public int compareTo(Object o) {
        int priceB = ((SupplyZone)o).price;

        if(price < priceB){
            return 1;
        } else if(price == priceB){
            return 0;
        }

        return -1;
    }
}
