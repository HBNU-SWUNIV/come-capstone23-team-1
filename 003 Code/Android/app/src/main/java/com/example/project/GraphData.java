package com.example.project;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class GraphData {
    private long startTime;
    private long timestamp; // 수면 데이터의 타임스탬프
    private float accelerometerX; // 가속도 센서 X 축 값
    private float accelerometerY; // 가속도 센서 Y 축 값
    private float accelerometerZ; // 가속도 센서 Z 축 값
    private List<Entry> entries;

    public GraphData(){
        entries = new ArrayList<>();
    }
    // 가속도 센서 데이터 저장 메서드
    public void setAccelerometerData(float x, float y, float z) {
        this.accelerometerX = x;
        this.accelerometerY = y;
        this.accelerometerZ = z;
    }

    public void clear(){
        this.accelerometerX = 0.0f;
        this.accelerometerY = 0.0f;
        this.accelerometerZ = 0.0f;
    }

    // 타임스탬프 설정 메서드
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public void addDataEntry(float x){
        entries.add(new Entry(entries.size(), x));
    }

    public void RemoveDataEntry()
    {
        entries.clear();
    }
    public List<Entry> getEntries(){
        return entries;
    }

    public float getAccelerometerX(){
        return accelerometerX;
    }

    public float getAccelerometerY(){
        return accelerometerY;
    }

    public float getAccelerometerZ(){
        return accelerometerZ;
    }

    public long getTimestamp(){
        return timestamp;
    }


}
