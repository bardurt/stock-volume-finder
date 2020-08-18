package com.zygne.stockalyze.domain.model;


public class Score {

    private static final int MAX_SCORE = 6;

    private int score = 0;

    public boolean initialized() {
        return true;
    }

    public void addPoint() {
        this.score++;
    }

    public void compute() {

        if(score > MAX_SCORE){
            score = MAX_SCORE;
        }
    }

    public double getFraction(){
        return score / (double) MAX_SCORE;
    }

    @Override
    public String toString() {

        if (score == 0) {
            return "";
        } else {
            return "Score : " + score + " / " + MAX_SCORE;
        }
    }
}
