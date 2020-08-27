package com.zygne.stockalyze.domain.model;

public class LiquidityZone implements Comparable {

    public int price;
    public long totalSize;
    public int orderCount;
    public double relativeVolume;
    public String note = "";

    public LiquidityZone(int price, long totalSize, int orderCount) {
        this.price = price;
        this.totalSize = totalSize;
        this.orderCount = orderCount;
    }

    @Override
    public int compareTo(Object o) {
        int priceB = ((LiquidityZone)o).price;

        if(price < priceB){
            return 1;
        } else if(price == priceB){
            return 0;
        }

        return -1;
    }
}
