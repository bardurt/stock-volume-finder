package com.zygne.stockalyze.domain.model.graphics;

public class ChartLine extends ChartObject {

    public double level;
    public int size;
    public int color;


    public static class Color{
        public static final int BLUE = 0;
        public static final int RED = 1;
        public static final int ORANGE = 2;
        public static final int YELLOW = 3;
    }
}
