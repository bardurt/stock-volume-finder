package com.zygne.stockalyze.domain.utils;

public class StringHelper {

    public static String getTickerNameFromCsv(String file){

        String[] parts = file.split("/", -1);

        String csv = "";

        for(String s : parts){
            if(s.toUpperCase().contains(".CSV")){
                csv = s;
                break;
            }
        }

        if(csv.isEmpty()){
            return "";
        }

        return csv.split("\\.")[0];
    }
}
