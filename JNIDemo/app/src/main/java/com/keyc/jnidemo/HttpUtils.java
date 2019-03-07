package com.keyc.jnidemo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by keyC on 2019/2/20.
 * 网络请求工具类
 */
public class HttpUtils {

    private OKHttpGetListener onOKHttpGetListener;
    private MyHandler myHandler = new MyHandler();
    private static final String TAG = "OKHttpUitls";

    /**
     * 上传参数
     * @param params 额外参数
     * @return
     */
    public RequestBody getRequestBody(HashMap<String, String> params) {
        params.put("device_no", System.currentTimeMillis() + "");
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("system_type","2");
        params.put("system_version","2");
        params.put("system_name", android.os.Build.BRAND+" "+android.os.Build.MODEL);
        params.put("app_version", "3.0");
        //序列化参数
        String serializationStr = JniUtils.serializationParams(params);
        //计算token
        String token = JniUtils.MD5(serializationStr);
        params.put("token", token);
        //加密
        JSONObject json = new JSONObject(params);
//        Map<String, Object> jsonParams = new HashMap<>();
//        jsonParams.put("query", Arithmetic.encryptBASE64(json1.toString(), Arithmetic.APP_KEY));
        String json2 = "";
        try {
            json2 = JniUtils.encrypt(json.toString(), JniUtils.APP_KEY);
            Log.i("TAG","onCreate: json2: " + json2);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        JSONObject json2 = new JSONObject(jsonParams);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/text; charset=utf-8"), json2);
        return body;
    }

    /**
     * post请求
     * @param url 请求地址
     * @param params 请求参数
     */
    public void post(final String url, final HashMap<String, String> params){
        OkHttpClient client = new OkHttpClient();
        //创建请求对象
        Request request = new Request.Builder().url(url).post(getRequestBody(params)).build();
        //创建Call请求队列
        //请求都是放到一个队列里面的
        Call call = client.newCall(request);

        //开始请求
        call.enqueue(new Callback() {
            // 失败，成功的方法都是在子线程里面，不能直接更新UI
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = myHandler.obtainMessage();
                message.obj = "请求失败";
                message.what = 0;
                myHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = myHandler.obtainMessage();
                String json = response.body().string();
                Log.i("TAG","onCreate: json: " + json);
                Log.i("TAG","onCreate: url: " + url + "  == params: " + params);
                try {
                    Log.i(TAG, "onCreate1: ");
                    message.obj = JniUtils.decrypt(json, "12345678");
                } catch (Exception e) {
                    Log.i(TAG, "onCreate2: " + e.toString());
                    e.printStackTrace();
                }
//                message.obj = json;
                message.what = 1;
                myHandler.sendMessage(message);
            }
        });
    }

    /**
     * 使用接口回到，将数据返回
     */
    public interface OKHttpGetListener{
        void error(String error);
        void success(String json);
    }

    /**
     * 给外部调用的方法
     * @param onOKHttpGetListener
     */
    public void setOnOKHttpGetListener(OKHttpGetListener onOKHttpGetListener){
        this.onOKHttpGetListener = onOKHttpGetListener;
    }

    /**
     * 使用Handler，将数据在主线程返回
     */
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int w = msg.what;
            if (w ==0){
                String error = (String) msg.obj;
                onOKHttpGetListener.error(error);
            }
            if (w==1){
                String json = (String) msg.obj;
                onOKHttpGetListener.success(json);
            }
        }
    }
}
