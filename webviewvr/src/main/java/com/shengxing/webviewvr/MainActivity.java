package com.shengxing.webviewvr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

  private WebView mWebView;
  private String url = "file:///android_asset/admin.html";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mWebView = findViewById(R.id.web);
    mWebView.loadUrl(url);
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return true;
      }
    });
  }
}
