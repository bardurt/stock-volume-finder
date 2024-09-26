package com.zygne.volfinder.domain.model;

import java.util.Comparator;

public class LiquidityZone implements Comparable {

    public final int price;
    public final long volume;
    public double volumePercentage = 0.0;
    public int rank = 0;
    public double percentile = 0.0d;
    public boolean visible = false;
    public boolean top = false;

    public LiquidityZone(int price, long volume) {
        this.price = price;
        this.volume = volume;
    }

    @Override
    public int compareTo(Object o) {
        int priceB = ((LiquidityZone) o).price;

        if (price < priceB) {
            return 1;
        } else if (price == priceB) {
            return 0;
        }

        return -1;
    }

    public static final class VolumeComparator implements Comparator<LiquidityZone> {

        private final boolean ascending;

        public VolumeComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(LiquidityZone o1, LiquidityZone o2) {
            if (ascending) {
                return Long.compare(o2.volume, o1.volume);
            } else {
                return Long.compare(o1.volume, o2.volume);
            }
        }
    }

    public static final class PriceComparator implements Comparator<LiquidityZone> {

        private boolean ascending;

        public PriceComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(LiquidityZone o1, LiquidityZone o2) {
            if (ascending) {
                return Integer.compare(o2.price, o1.price);
            } else {
                return Integer.compare(o1.price, o2.price);
            }
        }
    }

}
