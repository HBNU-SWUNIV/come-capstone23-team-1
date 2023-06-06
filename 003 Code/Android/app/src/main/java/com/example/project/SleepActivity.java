package com.example.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SleepActivity extends AppCompatActivity {
    private LineChart sleepChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleeping_layout);

        sleepChart = findViewById(R.id.sleepChart);
        drawSleepChart();
    }

    private void drawSleepChart() {
        List<SleepData> sleepDataList = getSleepDataList(); // 데이터를 가져오는 메서드 호출

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < sleepDataList.size(); i++) {
            SleepData sleepData = sleepDataList.get(i);
            entries.add(new Entry(i, sleepData.getSleepTime()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "수면 시간");
        LineData lineData = new LineData(dataSet);

        sleepChart.setData(lineData);
        sleepChart.invalidate();
    }

    private List<SleepData> getSleepDataList() {
        // 수면 데이터를 가져오는 로직을 구현합니다.
        // 예시로 임시 데이터를 반환하도록 하겠습니다.
        List<SleepData> sleepDataList = new ArrayList<>();
        for(int i=0;i<10;i++){
            sleepDataList.add(new SleepData("2023-06-01", i));
        }

        return sleepDataList;
    }
}