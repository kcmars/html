package family.li.aiyun.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;

import family.li.aiyun.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by keyC on 2019/6/20.
 * web交互
 */

public class JavaScriptInterface {

    private Bundle mBundle;
    private Context mContext;

    public JavaScriptInterface(Context mContext, Bundle bundle) {
        this.mContext = mContext;
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
        try {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) mContext).nodeClick(data);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
