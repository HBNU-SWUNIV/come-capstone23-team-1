package com.example.project;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener{
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private ImageView Memo, Search, information;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        try{
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("키 해시 == ", Base64.encodeToString(md.digest(), Base64.DEFAULT))
;            }
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        // 권한ID를 가져옵니다
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);

        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int permission3 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        // 권한이 열려있는지 확인
        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED || permission3 == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions(
                        new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1000);
            }
            return;
        }


        //맵 뷰 띄우는 코드
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        Memo = (ImageView)findViewById(R.id.imageMemo);
        Search = (ImageView)findViewById(R.id.imageSearch);
        information = (ImageView) findViewById(R.id.imageInfo);

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, MapSearchActivity.class);
                startActivity(intent);
            }
        });

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        Memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, MemoActivity.class);
                startActivity(intent);
            }
        });
    }


    // 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == false) {
                finish();
            }
        }


    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}

class TestData{
    String name;
    Double latitude;
    Double longitude;

    public void setName(String name){
        this.name = name;
    }

    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    public String getName(){
        return name;
    }

    public Double getLatitude(){
        return latitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    @Override
    public String toString() {
        return "TestData{" + "name='" + name + '\'' + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }

}

class TestApiData{
    String apiUrl = "https://api.odcloud.kr/api/15081492/v1/uddi:e781ca0c-1ec9-4a31-b769-a0c441137726";
    String apiKey = "kJYrjKPu%2F%2FvmHy2bjb86lsYBMqShzicBq7Bn%2FO3EwmwNeqg%2BF648td1mkEnZPPIzJqqXyzOHc%2FLBARvOOruhaw%3D%3D";

    public ArrayList<TestData> getData(){
        ArrayList<TestData> DataArray = new ArrayList<TestData>();

        Thread t = new Thread(){
            @Override
            public void run() {
                try{
                    String fullurl = apiUrl + "?serviceKey=" + apiKey + "&returnType=XML";
                    URL url = new URL(fullurl);
                    InputStream is = url.openStream();

                    XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = xmlFactory.newPullParser();
                    parser.setInput(is, "utf-8");

                    boolean IsName = false;
                    boolean IsLat = false;
                    boolean IsLong = false;
                    String name = "", latitude = "", longitude = "";

                    while(parser.getEventType() != XmlPullParser.END_DOCUMENT){
                        int type = parser.getEventType();
                        TestData data = new TestData();

                        if(type == XmlPullParser.START_TAG){
                            if (parser.getName().equals("col")){
                                if(parser.getAttributeValue(0).equals("공원명"))
                                    IsName = true;
                                else if(parser.getAttributeValue(0).equals("위도"))
                                    IsLat = true;
                                else if(parser.getAttributeValue(0).equals("경도"))
                                    IsLong = true;
                            }
                        }else if(type == XmlPullParser.TEXT){
                            if(IsName){
                                name = parser.getText();
                                IsName = false;
                            }else if(IsLat){
                                latitude = parser.getText();
                                IsLat = false;
                            }else if(IsLong){
                                longitude = parser.getText();
                                IsLong = false;
                            }
                        }else if(type == XmlPullParser.END_TAG && parser.getName().equals("item")){
                            data.setName(name);
                            data.setLatitude(Double.valueOf(latitude));
                            data.setLongitude(Double.valueOf(longitude));

                            DataArray.add(data);
                        }
                        type = parser.next();
                    }
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }catch(XmlPullParserException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };

        try{
            t.start();
            t.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        return DataArray;

    }

}
