package com.zygne.volfinder.domain.model;

import java.util.Comparator;

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

    public static final class VolumeComparator implements Comparator<VolumePriceGroup> {

        private final boolean ascending;

        public VolumeComparator(boolean ascending){
            this.ascending = ascending;
        }

        @Override
        public int compare(VolumePriceGroup o1, VolumePriceGroup o2) {
            if(ascending) {
                return Long.compare(o2.totalSize, o1.totalSize);
            } else {
                return Long.compare(o1.totalSize, o2.totalSize);
            }
        }
    }
}
