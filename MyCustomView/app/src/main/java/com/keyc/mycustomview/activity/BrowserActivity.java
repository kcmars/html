package com.keyc.mycustomview.activity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import com.keyc.mycustomview.R;
import com.keyc.mycustomview.utils.AiYunJavaScriptInterface;
import com.keyc.mycustomview.utils.PaxWebChromeClient;
import com.keyc.mycustomview.view.AiYunWebView;

/**
 * A demo browser activity
 * In this demo there are three modes,
 * sonic mode: sonic mode means webview loads html by sonic,
 * offline mode: offline mode means webview loads html from local offline packages,
 * default mode: default mode means webview loads html in the normal way.
 */
public class BrowserActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String PARAM_URL = "param_url";
    private AiYunWebView mWebView;
    private ImageView mImgClose;
    private TextView mTvTitle;
    private TextView mTvClose;

    private PaxWebChromeClient mChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_browser);
        mWebView = (AiYunWebView) findViewById(R.id.webview);
        mImgClose = (ImageView) findViewById(R.id.img_close);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvClose = (TextView) findViewById(R.id.tv_close);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mImgClose.setOnClickListener(this);
        mTvClose.setOnClickListener(this);
        //需要设置setWebViewClient，否则会打开浏览器跳转二级网页
        mWebView.setWebViewClient(new AiYunWebView.AiYunWebViewClient());
        mChromeClient = new PaxWebChromeClient(this, mWebView.getProgressBar(), mTvTitle);
//        mWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setTextZoom(100); //禁止缩放字体大小
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebContentsDebuggingEnabled(true);
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
        Bundle args = null;
        if (getIntent().getBundleExtra("params")!=null){
            args = getIntent().getBundleExtra("params");
        }else {
            args = new Bundle();
        }
        args.putString("url", "http://118.190.203.67:8800/web/tree");

        mWebView.addJavascriptInterface(new AiYunJavaScriptInterface(this, args, null), "AiYunInterface");
        mWebView.loadUrl("http://118.190.203.67:8800/web/tree");
    }

    //选取回执
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mChromeClient.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            //清空所有Cookie
            CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
            CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
            cookieManager.removeAllCookie();// Removes all cookies.
            CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearCache(true);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_close:
                finish();
                break;
        }
    }
}
