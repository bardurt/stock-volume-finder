package com.zygne.stockalyze.domain.model;

import java.util.Comparator;

public class Node implements Comparable{

    public int level;
    public int pull;
    public boolean origin;
    public double probability;
    public double change;
    public double prediction;
    public double strength;
    public String note = "";
    public int side = 0;

    @Override
    public int compareTo(Object o) {
        int levelB = ((Node)o).level;

        if(level < levelB){
            return 1;
        } else if(level == levelB){
            return 0;
        }

        return -1;
    }

    public static final class StrengthComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return Double.compare(o1.strength, o2.strength);
        }
    }

}
