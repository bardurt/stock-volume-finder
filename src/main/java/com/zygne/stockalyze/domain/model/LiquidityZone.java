package com.zygne.stockalyze.domain.model;

import java.util.Comparator;

public class LiquidityZone implements Comparable {

    public final int price;
    public final long volume;
    public final int orderCount;
    public double relativeVolume;
    public double volumePercentage;
    public double powerRatio;
    public boolean origin = false;
    public int rank = 0;
    public double percentile = 0.0d;

    public final String note = "";

    public LiquidityZone(int price, long volume, int orderCount) {
        this.price = price;
        this.volume = volume;
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
            return Long.compare(o1.volume, o2.volume);
        }
    }

    public static final class PriceComparator implements Comparator<LiquidityZone>{

        @Override
        public int compare(LiquidityZone o1, LiquidityZone o2) {
            return Integer.compare(o1.price, o2.price);
        }
    }

    public static final class RankComparator implements Comparator<LiquidityZone>{

        @Override
        public int compare(LiquidityZone o1, LiquidityZone o2) {
            return Integer.compare(o1.rank, o2.rank);
        }
    }
}
