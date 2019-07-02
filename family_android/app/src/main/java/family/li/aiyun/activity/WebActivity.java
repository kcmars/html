package family.li.aiyun.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import family.li.aiyun.LiApplication;
import family.li.aiyun.R;
import family.li.aiyun.util.JavaScriptInterface;

public class WebActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private WebView mWebView;
    private String mUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        if (getIntent().getStringExtra("url") != null) {
            mUrl = getIntent().getStringExtra("url");
        }

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mWebView = (WebView) findViewById(R.id.web_view);

        mIvBack.setOnClickListener(this);

        initWebView();
    }

    /**
     * 初始化webView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false); // 支持缩放
        webSettings.setUseWideViewPort(true);      //设置缩放后不会变形
        webSettings.setBuiltInZoomControls(false);     //设置可以缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);

        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");

        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                mTvTitle.setText(title);
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("url", mUrl);
        mWebView.addJavascriptInterface(new JavaScriptInterface(LiApplication.getContext(), bundle), "LeeInterface");

        //步骤2. 选择加载方式(加载apk包中的html页面)
//        mWebView.loadUrl("file:///android_asset/www/error.html");
        mWebView.loadUrl(mUrl);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
}
