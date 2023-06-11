package com.example.project;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import java.util.ArrayList;

public class MapSearchActivity extends AppCompatActivity {
    EditText editTextSearch;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        //EditText와 Button 객체 찾기
        editTextSearch = findViewById(R.id.editTextSearch);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        TestApiData apiData = new TestApiData();
        ArrayList<TestData> dataArr = apiData.getData();

        ArrayList<MapPOIItem> markerArr = new ArrayList<MapPOIItem>();
        for(TestData data : dataArr){
            MapPOIItem marker = new MapPOIItem();
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(data.getLatitude(), data.getLongitude()));
            marker.setItemName(data.getName());
            markerArr.add(marker);
        }
       // mapView.addPOIItems(markerArr.toArray(new MapPOIItem[markerArr.size()]));
    }

    private void performSearch(){
        String searchQuery = editTextSearch.getText().toString().trim();
        searchLocation(searchQuery);
    }

    private void searchLocation(String keyword){
        // 검색어를 이용하여 위치 검색 로직 작성
        // 예시로 마커를 추가
        TestApiData apidata = new TestApiData();
        ArrayList<TestData> dataArr = apidata.getData();

        ArrayList<MapPOIItem> markerArr = new ArrayList<MapPOIItem>();
        for (TestData data : dataArr){
            if(data.getName().contains(keyword)){
                MapPOIItem marker = new MapPOIItem();
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(data.getLatitude(), data.getLongitude()));
                marker.setItemName(data.getName());
                markerArr.add(marker);
            }
        }
        //mapView.addPOIItems(markerArr.toArray(new MapPOIItem[markerArr.size()]));
    }
}
