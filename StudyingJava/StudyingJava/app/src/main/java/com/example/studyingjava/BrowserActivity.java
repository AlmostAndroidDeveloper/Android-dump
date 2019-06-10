package com.example.studyingjava;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        Uri uri = getIntent().getData();
        webView.loadUrl(uri.toString());
    }
}
