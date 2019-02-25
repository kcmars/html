package com.keyc.jnidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private byte[] a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        tv.setText(JniUtils.getKey().length + "");
        a = JniUtils.getKey();
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

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
