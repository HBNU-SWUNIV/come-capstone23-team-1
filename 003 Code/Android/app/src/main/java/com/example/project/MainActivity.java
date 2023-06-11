package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.hardware.*;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button Graph, Map, Calendar;
    private Chronometer chronometer;

    private long startTime;
    private long endTime;
    private long sleepTime;

    private long pauseOffset;
    private boolean running;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");
    String formatDate = sdfNow.format(date);

    TextView dateNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateNow = (TextView) findViewById(R.id.dateNow);
        dateNow.setText(formatDate);

        chronometer = findViewById(R.id.chronometer);


        Graph = (Button)findViewById(R.id.GraphButton);
        Map = (Button)findViewById(R.id.MapButton);
        Calendar = (Button)findViewById(R.id.CalendarButton);


        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h = (int)(time / 36000000);
                int m = (int)(time - h*36000000)/60000;
                int s = (int)(time - h*36000000 - m*60000) / 1000;
                String t = (h < 10 ? "0"+h: h)+":" + (m < 10 ? "0"+m : m) + ":" + (s < 10 ? "0"+s: s);

                chronometer.setText(t);
            }
        });

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setText("00:00:00");

        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        Graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SleepActivity.class);
                intent.putExtra("시작", startTime);
                intent.putExtra("종료", endTime);
                intent.putExtra("수면", sleepTime);
                startActivity(intent);
            }
        });

        Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

    }



    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            startTime = System.currentTimeMillis();
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            endTime = System.currentTimeMillis();
            sleepTime = (endTime - startTime) / 1000;
        }
    }

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        chronometer.setText("00:00:00");
    }

}