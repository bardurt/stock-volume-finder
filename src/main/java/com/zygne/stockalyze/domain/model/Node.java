package com.zygne.stockalyze.domain.model;

public class Node implements Comparable{

    public int level;
    public int pull;
    public boolean origin;
    public double probability;
    public String note = "";


    @Override
    public int compareTo(Object o) {
        int levelb = ((Node)o).level;

        if(level < levelb){
            return 1;
        } else if(level == levelb){
            return 0;
        }

        return -1;
    }

}
