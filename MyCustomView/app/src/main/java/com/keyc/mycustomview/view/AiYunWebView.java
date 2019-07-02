package com.keyc.mycustomview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.keyc.mycustomview.R;
import com.keyc.mycustomview.utils.Utils;

/**
 * Created by bobo on 2017/10/28.
 */

public class AiYunWebView extends WebView {
    private ProgressBar mProgressBar;

    private Context mContext;

    public AiYunWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;

        initProgress();

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);

        // init webview settings
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    private void initProgress() {
        mProgressBar = new ProgressBar(mContext, null,
                android.R.attr.progressBarStyleHorizontal);
        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 10, 0, 0);
        mProgressBar.setLayoutParams(layoutParams);
        Drawable drawable = mContext.getResources().getDrawable(R.color.color_0);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        mProgressBar.setProgress(0);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }


    public static class AiYunWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            webView.loadUrl("file:///android_asset/no_network.html");
            super.onReceivedError(webView, webResourceRequest, webResourceError);
        }

        @TargetApi(21)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return shouldInterceptRequest(view, request.getUrl().toString());
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

            return null;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            return super.shouldOverrideUrlLoading(webView, s);
        }
    }


}
