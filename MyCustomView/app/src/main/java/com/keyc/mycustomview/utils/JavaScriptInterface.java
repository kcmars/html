package com.keyc.mycustomview.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.keyc.mycustomview.activity.Main16Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2019/6/20.
 */

public class JavaScriptInterface {

    private Bundle mBundle;
    private Object mTarget;

    public JavaScriptInterface(Object mTarget, Bundle bundle) {
        this.mTarget = mTarget;
        this.mBundle = bundle;
    }

    @JavascriptInterface
    public String getParams() {
        JSONObject jsonObject = new JSONObject();
        if (mBundle != null) {
            Set<String> allKeys = mBundle.keySet();
            Iterator i = allKeys.iterator();//先迭代出来
            while (i.hasNext()) {
                String key = (String) i.next();
                String value = (String) mBundle.get(key);
                try {
                    jsonObject.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject.toString();
    }

    /**
     * 接收web参数
     */
    @JavascriptInterface
    public void paramTo(String data) {
        Log.i("TAG", "paramTo: " + data);
        ((Main16Activity) mTarget).nodeClick(data);
    }
}
