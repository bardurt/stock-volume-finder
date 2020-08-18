package com.zygne.stockalyze.domain.model;

public interface StockState extends StockInfo {

    int runner();

    Trend trend();

    News news();

    int gap();

    void normalize();

    enum News{
        Generic,
        Medium,
        Significant;

        static News fromInt(int code){
            if(code == 1){
                return Medium;
            }

            if(code == 2){
                return Significant;
            }

            return Generic;
        }

    }

    enum Trend{
        Consolidation,
        Up,
        Down;

        static Trend fromInt(int code){
            if(code == -1){
                return Down;
            }

            if(code == 0){
                return Consolidation;
            }

            return Up;
        }
    }
}
