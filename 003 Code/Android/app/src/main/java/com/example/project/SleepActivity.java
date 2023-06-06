package com.example.project;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class SleepActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private LineChart lineChart;
    private SleepData sleepData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleeping_layout);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

       lineChart = findViewById(R.id.sleepChart);
       sleepData = new SleepData();

       Description description = new Description();
       description.setText("Accelerometer Data");
       lineChart.setDescription(description);
       lineChart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            sleepData.setAccelerometerData(x, y, z);
            sleepData.addDataEntry(sleepData.getEntries().size(), x);

            List<ILineDataSet> dataSets = lineChart.getData().getDataSets();
            if (dataSets != null && dataSets.size() > 0){
                LineDataSet set = (LineDataSet) dataSets.get(0);
                set.setValues(sleepData.getEntries());
                lineChart.getData().notifyDataChanged();
                lineChart.notifyDataSetChanged();
            }else{
                LineDataSet set = new LineDataSet(sleepData.getEntries(), "Accelerometer Data");
                set.setColors(ColorTemplate.MATERIAL_COLORS);
                set.setValueTextColor(Color.BLACK);
                List<ILineDataSet> dataSetList = new ArrayList<>();
                dataSetList.add(set);
                LineData lineData = new LineData(dataSetList);
                lineChart.setData(lineData);
            }
            lineChart.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 가속도 센서 정확도 변경 시 처리할 로직을 구현 (필요한 경우)
    }

}