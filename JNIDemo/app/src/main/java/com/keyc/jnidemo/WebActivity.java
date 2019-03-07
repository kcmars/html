package com.keyc.jnidemo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebActivity extends BaseActivity {

    private static final String TAG = "mWebActivity";

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = findViewById(R.id.web_view);
        mProgressBar = findViewById(R.id.progress_bar);
        showLeft(R.mipmap.icon_black_left, true, "img");

        //添加js监听 这样html就能与原生交互，可以自定义Interface回调事件处理
        mWebView.addJavascriptInterface(this, "android");

        //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.i(TAG, "onReceivedTitle: " + title);
                setTitle(title);
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.i(TAG, "onProgressChanged: " + newProgress);
                mProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                Log.i(TAG, "onReceivedIcon: ");
            }

        });

        //WebViewClient主要帮助WebView处理各种通知、请求事件、拦截url等
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "onPageFinished: " + url);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i(TAG, "onPageStarted: " + url);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i(TAG, "shouldOverrideUrlLoading: " + request.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        WebSettings mWebSettings = mWebView.getSettings();
        //允许使用js
        mWebSettings.setJavaScriptEnabled(true);
        //不允许使用缓存
        mWebSettings.setAppCacheEnabled(false);
        //不使用缓存，只从网络获取数据.
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //不支持屏幕缩放
        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(false);
        //不显示webview缩放按钮
        mWebSettings.setDisplayZoomControls(false);

        //加载url
        mWebView.loadUrl("http://webapp.a56999.com/Bus/html/api.html");

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            mWebView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        mWebView.destroy();
        mWebView = null;
    }
}
