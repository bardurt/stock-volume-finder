package com.zygne.stockalyze.domain.model;

import java.util.Comparator;

public class LiquidityZone implements Comparable {

    public final int price;
    public final long totalSize;
    public final int orderCount;
    public double relativeVolume;
    public final String note = "";

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

    public static final class VolumeComparator implements Comparator<LiquidityZone>{

        @Override
        public int compare(LiquidityZone o1, LiquidityZone o2) {
            return Long.compare(o1.totalSize, o2.totalSize);
        }
    }
}
