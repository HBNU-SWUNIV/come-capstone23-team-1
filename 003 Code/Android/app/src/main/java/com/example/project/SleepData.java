package com.example.project;

public class SleepData {
    private String date;
    private int sleepTime;

    public SleepData(String date, int sleepTime) {
        this.date = date;
        this.sleepTime = sleepTime;
    }

    public String getDate() {
        return date;
    }

    public int getSleepTime() {
        return sleepTime;
    }
}