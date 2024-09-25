package com.zygne.stockalyze.domain.model;

public class VolumePriceGroup implements Comparable{

    public final int price;
    public long totalSize;

    public VolumePriceGroup(int price, long totalSize){
        this.price = price;
        this.totalSize = totalSize;
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
