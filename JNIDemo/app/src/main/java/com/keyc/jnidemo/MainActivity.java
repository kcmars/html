package com.keyc.jnidemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.TimerTask;

import static junit.framework.Assert.assertEquals;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity-TAG";
    private long mBackPressedTime = -1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);
        showLeft(R.mipmap.icon_black_left, true, "img");

        TextView tv_bg = findViewById(R.id.tv_bg);
        tv_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        Button mBtnWebView = (Button) findViewById(R.id.btn_web_view);
        mBtnWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, WebActivity.class);
                startActivity(intent);
            }
        });
//        tv.setText(stringFromJNI());
        tv.setText(JniUtils.getKey().length + "");
        String ming = "{\"app_version\":\"11.21\",\"system_version\":\"12.100000\"}";
        try {
            String encrypmi = JniUtils.encrypt(ming, "12345678");
            String decrypmi = JniUtils.decrypt(encrypmi,"12345678");
            Log.i("TAG", "onCreate: " + "来自JniUtilsDes:" +  JniUtils.setStringJni() + "\n加密前：" + ming + "\n加密后：" + encrypmi + "\n解密后：" + decrypmi);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            String encrypmi = JniUtils.encode(ming);
//            String decrypmi = JniUtils.decode(encrypmi);
//            Log.i("TAG", "onCreate: " + "来自JniUtils:" +  JniUtils.setStringJni() + "\n加密前：" + ming + "\n加密后：" + encrypmi + "\n解密后：" + decrypmi);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        String ming = "asdasd";
//        try {
//            String encrypmi = DesUtils2.encrypt(ming);
//            String decrypmi = DesUtils2.decrypt(encrypmi);
//            Log.i("TAG", "onCreate: " + "来自DesUtils2:" +  JniUtils.setStringJni() + "\n加密前：" + ming + "\n加密后：" + encrypmi + "\n解密后：" + decrypmi);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            String key = DesUtils.initKey("12345678");
//            Log.i("TAG","onCreate: 原文:\t" + ming);
//            Log.i("TAG","onCreate: 密钥:\t" + key);
//
//            byte[] inputData = ming.getBytes();
//            inputData = DesUtils.encrypt(inputData, key);
//            Log.i("TAG","onCreate: 加密后:\t" + DesUtils.encryptBASE64(inputData));
//
//            byte[] outputData = DesUtils.decrypt(inputData, key);
//            String outputStr = new String(outputData);
//            Log.i("TAG","onCreate: 解密后:\t" + outputStr);
//            assertEquals(ming, outputStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        getData();
    }

    private void getData(){
        //创建OKHttpClient对象
        HttpUtils okHttpUitls = new HttpUtils();
        HashMap<String, String> params = new HashMap<>();
        params.put("city_code", "028");
        params.put("city", "成都市");
        okHttpUitls.post("http://118.190.203.67/api/System/Config/getVersionAndConfigInfo", params);
        okHttpUitls.setOnOKHttpGetListener(new HttpUtils.OKHttpGetListener() {
            @Override
            public void error(String error) {
                Log.i("TAG","onCreate: error:\t" + error);
            }

            @Override
            public void success(String json) {
                Log.i("TAG","onCreate: success:\t" + json);
            }
        });
    }

    public void onImgLeft() {
        Log.i(TAG, "onImgLeft: ");
//        super.onImgLeft();
        call("13111869201");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /**
     * 点击两次退出app
     */
    @Override
    public void onBackPressed() {
        if (mBackPressedTime == -1) {
            mBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new TimerTask() {
                @Override
                public void run() {
                    mBackPressedTime = -1;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
