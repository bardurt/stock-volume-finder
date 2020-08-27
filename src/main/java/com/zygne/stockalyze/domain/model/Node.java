package com.zygne.stockalyze.domain.model;

public class Node implements Comparable{

    public int level;
    public int pull;
    public boolean origin;
    public double probability;
    public double change;
    public double prediction;
    public double strength;
    public String note = "";

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

}
