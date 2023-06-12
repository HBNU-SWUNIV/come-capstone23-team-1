package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    WebView webview;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.info_layout);
        Log.d("info", "information");

        webview = (WebView)findViewById(R.id.webView);
        webview.loadUrl("file:///android_asset/test.html");

    }
}
