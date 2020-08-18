package com.zygne.stockalyze.domain.model;

public class VolumePriceGroup implements Comparable{

    public int price;
    public int totalSize;
    public int orderCount;

    public VolumePriceGroup(int price, int totalSize){
        this.price = price;
        this.totalSize = totalSize;
        this.orderCount = 1;
    }

    @Override
    public int compareTo(Object o) {
        int priceB = ((VolumePriceGroup)o).price;

        if(price > priceB){
            return 1;
        } else if(price == priceB){
            return 0;
        }

        return -1;
    }
}
