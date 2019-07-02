package com.keyc.mycustomview.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.Kin;
import com.keyc.mycustomview.utils.JavaScriptInterface;
import com.keyc.mycustomview.utils.Utils;

import static com.keyc.mycustomview.activity.BrowserActivity.PARAM_URL;

public class Main16Activity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWebView;
    private TextView mTvText;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main16);

        //步骤1. 定义Webview组件
        mWebView = (WebView) findViewById(R.id.web_view);
        mTvText = (TextView) findViewById(R.id.tv_text);

        mTvText.setOnClickListener(this);
        /**
         * 读取json数据，并解析成对应的bean对象
         */
        String json = Utils.getJson("test.json", this);
        Bundle bundle = new Bundle();
        bundle.putString("data", json);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setTextZoom(100); //禁止缩放字体大小
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setUseWideViewPort(true);      //设置缩放后不会变形
        webSettings.setBuiltInZoomControls(true);     //设置可以缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);

        mWebView.setWebContentsDebuggingEnabled(true);

        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");

        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        bundle.putString("url", "http://118.190.203.67:8800/web/tree");
        mWebView.addJavascriptInterface(new JavaScriptInterface(this, bundle), "LeeInterface");

        //步骤2. 选择加载方式(加载apk包中的html页面)
//        mWebView.loadUrl("file:///android_asset/www/error.html");
        mWebView.loadUrl("http://118.190.203.67:8800/web/tree");


    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.tv_text:
                /**
                 * 读取json数据，并解析成对应的bean对象
                 */
                String json = Utils.getJson("test.json", this);

                Intent intent = new Intent(this, BrowserActivity.class);
                intent.putExtra(PARAM_URL, "http://118.190.203.67:8800/web/tree");
                bundle.putString("data", json);
                intent.putExtra("params", bundle);
                startActivity(intent);
                break;
        }
    }

    /**
     * 接受web点击事件
     */
    public void nodeClick(String string) {
        Log.i("TAG", "nodeClick: " + string);
//        String json = new Gson().toJson(string);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

        }
    }
}
