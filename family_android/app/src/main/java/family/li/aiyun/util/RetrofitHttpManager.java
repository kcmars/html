package family.li.aiyun.util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by keyC on 2019/6/12.
 */
public class RetrofitHttpManager {

    private static RetrofitHttpManager mRetrofitFactory;
    private Retrofit mRetrofit;
    private ApiService  apiService;
    public static int HTTP_TIME=6; // 超时的设置
    private RetrofitHttpManager(){

        OkHttpClient mOkHttpClient=new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIME, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIME, TimeUnit.SECONDS)
                .writeTimeout(HTTP_TIME, TimeUnit.SECONDS)
      //             .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
//                .addNetworkInterceptor(new Interceptor() {    // 缓存配置
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//                        return chain.proceed(request);
//                    }
             //   })
                .build();
        mRetrofit=new Retrofit.Builder()
                .baseUrl(HttpManager.ApiHost)
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .client(mOkHttpClient)
                .build();
        apiService=mRetrofit.create(ApiService.class);

    }
    public static RetrofitHttpManager getInstence(){
        if (mRetrofitFactory==null){
            synchronized (RetrofitHttpManager.class) {
                if (mRetrofitFactory == null)
                    mRetrofitFactory = new RetrofitHttpManager();
            }
        }
        return mRetrofitFactory;
    }
    public  Retrofit getmRetrofit(){
        return mRetrofit;
    }

    public ApiService getCommonsService(){
        return apiService;
    }

    /**
     * Retrofit 上传JSON
     * */
    public RequestBody getRequestBody(HashMap<String, Object> params) {
        params.put("device_no", SystemUtils.getDeviceId());
//        params.put("device_no", PushServiceFactory.getCloudPushService().getDeviceId());
//        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("system_type","2");
        params.put("system_version", Utils.APP_VERSION_NAME);
        params.put("system_name", android.os.Build.BRAND+" "+android.os.Build.MODEL);
        params.put("app_version", Utils.APP_VERSION_CODE+"");
        //序列化参数
//        String serializationStr = Arithmetic.serializationParams(params);
//        String serializationStr = DesUtils.serializationParams(params);
        //计算token
//        String token = Arithmetic.MD5(serializationStr);
//        String token = DesUtils.MD5(serializationStr);
//        params.put("token", token);
        //加密
//        JSONObject json1 = new JSONObject(params);
//        Map<String, Object> jsonParams = new HashMap<>();
//        jsonParams.put("query", Arithmetic.encryptBASE64(json1.toString(), Arithmetic.APP_KEY));
        //新加密方式
//        String json2 = "";
//        try {s
//            json2 = DesUtils.enCode(json1.toString(), DesUtils.APP_KEY);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        JSONObject json2 = new JSONObject(params);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json2.toString());
        return body;
    }

}
