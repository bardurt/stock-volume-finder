package com.zygne.stockalyze;

public class StringUtils {


    public static String center(String source, int width){

        int padding = (width - source.length()) / 2;


        return " ".repeat(Math.max(0, padding)) +
                source +
                " ".repeat(Math.max(0, padding));
    }

    public static String repeat(String source, int amount){

        StringBuilder builder = new StringBuilder();

        builder.append(String.valueOf(source).repeat(Math.max(0, amount)));

        return builder.toString();
    }
}
