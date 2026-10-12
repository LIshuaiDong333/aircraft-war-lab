package edu.hitsz.dao;

public class Rank {
    private String name;
    private int score;
    private long time;     //游戏结束时间戳

    public Rank(){}

    public Rank(String name, int score, long time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public String getName() { return name; }
    //public void setName(String name) { this.name = name; }
    public int getScore() { return score; }
    //public void setScore(int score) { this.score = score; }
    public long getTime() { return time; }
    //public void setTime(long time) { this.time = time; }
}