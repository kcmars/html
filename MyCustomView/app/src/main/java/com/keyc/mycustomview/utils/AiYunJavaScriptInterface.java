/*
 * Tencent is pleased to support the open source community by making VasSonic available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 *
 */

package com.keyc.mycustomview.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * Sonic javaScript Interface (Android API Level >= 17)
 */
public class AiYunJavaScriptInterface {

    private Bundle mBundle;
    private Object mTarget;
    //剪切板管理工具类
    private ClipboardManager mClipboardManager;
    public AiYunJavaScriptInterface(Object mTarget, Bundle bundle, ClipboardManager clipboardManager) {
        this.mTarget = mTarget;
        this.mBundle = bundle;
        this.mClipboardManager = clipboardManager;
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
        Intent intent = new Intent();
        intent.putExtra("param", data);
        ((Activity) mTarget).setResult(RESULT_OK, intent);
    }

}
