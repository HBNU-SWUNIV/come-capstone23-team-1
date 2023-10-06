package com.example.project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private boolean isGraphDrawingEnabled = true;



    private List<Long> sleepTimes;
    private Long startTime, endTime;
    private Long SleepTime;
    private LineChart lineChart;
    private SleepData sleepData;
    Button stopButton, clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleeping_layout);

        Intent intent = getIntent();
        startTime = intent.getLongExtra("시작", 0) / 1000;
        endTime = intent.getLongExtra("종료", 0) / 1000;
        SleepTime = intent.getLongExtra("수면", 0);

        String a = String.valueOf(startTime);
        String b = String.valueOf(SleepTime);
        String c = String.valueOf(endTime);
        Log.d("startTime : "+ a, "sleepTime = " + b);
        Log.d( "endTime = ", c);


        sleepTimes = new ArrayList<>();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

       lineChart = findViewById(R.id.sleepChart);
       sleepData = new SleepData();
       stopButton = (Button)findViewById(R.id.stopButton);
       clearButton = (Button)findViewById(R.id.clearButton);

       if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
       }

       stopButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               isGraphDrawingEnabled = !isGraphDrawingEnabled; // 그래프 그리는 동작 활성화/비활성화 전환

           }
       });

       clearButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               // 그래프 데이터 초기화
               List<Entry> emptyEntries = new ArrayList<>();
               LineDataSet emptyDataSet = new LineDataSet(emptyEntries, "Accelerometer Data");
               emptyDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
               emptyDataSet.setValueTextColor(Color.BLACK);
               LineData emptyLineData = new LineData(emptyDataSet);
               lineChart.setData(emptyLineData);

               sleepData.RemoveDataEntry();

               lineChart.setDescription(null); // 초기 설명(Description) 제거
               lineChart.invalidate();
           }
       });

       Description description = new Description();
       description.setText("Accelerometer Data");
       lineChart.setDescription(description);

       List<Entry> initialEntries = new ArrayList<>();
       LineDataSet initialDataSet = new LineDataSet(initialEntries, "Accelerometer Data");
       initialDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
       initialDataSet.setValueTextColor(Color.BLACK);
       LineData initialLineData = new LineData(initialDataSet);
       lineChart.setData(initialLineData);

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

        if (isGraphDrawingEnabled && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                sleepData.setAccelerometerData(x, y, z);
                sleepData.addDataEntry(x);

                LineDataSet dataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
                dataSet.setValues(sleepData.getEntries());
                lineChart.getData().notifyDataChanged();
                lineChart.notifyDataSetChanged();

                lineChart.invalidate();

            // 여기에서 가속도 데이터를 분석하고 수면 패턴을 확인할 수 있습니다.
            // 예를 들어, 특정 조건을 충족하면 수면 상태를 변경하거나 저장할 수 있습니다.
            // 이 부분에 심층적인 수면 분석 알고리즘을 구현해야 합니다.

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //권한이 승인될 때 실행할 코드 추가
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 가속도 센서 정확도 변경 시 처리할 로직을 구현 (필요한 경우)
    }

}