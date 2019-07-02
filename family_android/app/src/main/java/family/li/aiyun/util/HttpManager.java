package family.li.aiyun.util;

import android.content.Intent;
import android.net.ParseException;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.LiApplication;
import family.li.aiyun.activity.LoginActivity;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.HttpRequest;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

import static java.lang.String.valueOf;

/**
 * Created by keyC on 2019/6/12.
 */
public class HttpManager {

    static final MediaType MEDIATYPE = MediaType.parse("application/json; charset=utf-8");

    //本地地址
//    public static final String PUBLIC_HOST = "http://sit.zuapi.a56999.com/";
//    public static final String IP = "sit.zuapi.a56999.com";

    //线上地址
    public static final String PUBLIC_HOST = "http://zu.a56999.com/";
    public static final String IP = "zu.a56999.com";//

    static final String ApiHost = PUBLIC_HOST;
    private static final String TAG = "AiYunHttpUtils";

    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private CompositeDisposable disposables ;
    private Gson mGson;
    private static HttpManager instance;

    private HttpManager() {
        //okhhtp的单例模式
        mOkHttpClient = new OkHttpClient();
        disposables= new CompositeDisposable();
        mGson = new Gson();
        //构造一个handler，不管是从哪个哪个线程中发出消息，
        // 消息都会发送到主线程的messagequeue中
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                instance = new HttpManager();
            }
        }
        return instance;
    }

    /**
     * post网络请求
     * retrofit + RxJava
     * @param url 请求地址
     * @param params 请求参数
     * @param callback 回调函数
     */
    public void post (String url, final HashMap<String, Object> params, final HttpRequestCallback callback) {
        RetrofitHttpManager.getInstence().getCommonsService().
                getMessage(url, RetrofitHttpManager.getInstence().getRequestBody(params)).
                compose(ApiService.schedulersTransformer)
                .doOnSubscribe(disposable -> {
                    callback.requestStart();
                })
                .doFinally(() -> {
                    callback.requestComplete();
                })
                .subscribe(new io.reactivex.Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        RxApiManager.get().add(url, d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String ss = responseBody.string();
//                            Log.i("返回===",ss);
//                            String reset = Arithmetic.decryptBASE64(ss, Arithmetic.APP_KEY);
//                            String reset = DesUtils.deCode(ss, DesUtils.APP_KEY);
                            String reset = ss;
                            Log.i("返回",params + "---" + url + "-----" + reset);
                            HttpRequest mRequest = new Gson().fromJson(reset, new TypeToken<HttpRequest>(){}.getType());
                            if (mRequest.getCode() == 1) {
                                callback.requestSuccess(mRequest.getData(), mRequest.getMsg());
                            } else {
                                if (mRequest.getErr_code() == 1101) { //被下线后访问返回
                                    openLogin();
                                } else {
                                    callback.requestFail(mRequest.getMsg());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.requestError("解析失败");
                            Log.i("返回","解析失败" + "--" + url + "--" + e.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.requestError(handleResponseError(e));
                        Log.i("返回错误", params + "---" + RetrofitHttpManager.getInstence().getmRetrofit().baseUrl() + "---" + url + "--"  + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        RxApiManager.get().remove(url);
                    }
                });
    }

    /**
     * 上传图片
     */
    public void uploadImgPost(final String url, final HashMap<String, Object> map, File file, final HttpRequestCallback callback) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(file != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("image", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (HashMap.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        RetrofitHttpManager.getInstence().getCommonsService().
                getMessage(url, requestBody.build()).
                compose(ApiService.schedulersTransformer)
                .doOnSubscribe(disposable -> {
                    callback.requestStart();
                })
                .doFinally(() -> {
                    callback.requestComplete();
                })
                .subscribe(new io.reactivex.Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        RxApiManager.get().add(url, d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String ss = responseBody.string();
//                            Log.i("返回===",ss);
//                            String reset = Arithmetic.decryptBASE64(ss, Arithmetic.APP_KEY);
//                            String reset = DesUtils.deCode(ss, DesUtils.APP_KEY);
                            String reset = ss;
                            Log.i("返回",requestBody.toString() + "---" + url + "-----" + reset);
                            HttpRequest mRequest = new Gson().fromJson(reset, new TypeToken<HttpRequest>(){}.getType());
                            if (mRequest.getCode() == 1) {
                                callback.requestSuccess(mRequest.getData(), mRequest.getMsg());
                            } else {
                                if (mRequest.getCode() == -400) { //被下线后访问返回
//                                    openLogin();
                                } else {
                                    callback.requestFail(mRequest.getMsg());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.requestError("解析失败");
                            Log.i("返回","解析失败" + "--" + url + "--" + e.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.requestError(handleResponseError(e));
                        Log.i("返回错误", requestBody.toString() + "---" + RetrofitHttpManager.getInstence().getmRetrofit().baseUrl() + "---" + url + "--"  + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        RxApiManager.get().remove(url);
                    }
                });
    }

    /**
     * 回到当前身份主页并打开登录窗口
     */
    public void openLogin() {
        if (Utils.TOKEN == null) {
            return;
        }
        Utils.TOKEN = null;
        Utils.USER_ID = "-1"; //用户USER_ID
        Utils.USER_NAME = ""; //用户姓名
        Utils.USER_HEAD_IMG = ""; //用户头像
        Utils.ADMIN = 0; //是否属于管理员（0 否， 1 是）
        Utils.HIDE_INFO = 0; //是否隐藏用户更多信息（0 隐藏， 1 显示）
        Utils.clearPreference(LiApplication.getContext());
        Intent intent = new Intent(LiApplication.getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LiApplication.getContext().startActivity(intent);
    }

    /**
     * post请求
     * @param url
     * @param params
     * @param callback
     */
//    public void post(final String url, final Map<String, String> params, final AiYunBeanCommonHttpCallBack callback) {
//        //添加统一的参数
////        Log.e("TAG", "serializationStr1: "+params);
//        params.put("appversion", Utils.APP_VERSION_CODE + "");
//        params.put("appkey", PushServiceFactory.getCloudPushService().getUTDeviceId());
//        params.put("systype", "android");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        //序列化参数
//        String serializationStr = Arithmetic.serializationParams(params);
//        //计算token
//        String token = Arithmetic.MD5(serializationStr);
//        params.put("token", token);
//        //加密
//
//        JSONObject json1 = new JSONObject(params);
//        Map<String, Object> jsonParams = new HashMap<>();
//        jsonParams.put("query", Arithmetic.encryptBASE64(json1.toString(), Arithmetic.APP_KEY));
//        JSONObject json2 = new JSONObject(jsonParams);
//        //构建请求参数
//        RequestBody requestBody = RequestBody.create(MEDIATYPE, json2.toString());
//        //创建一个Request
//        final Request request = new Request.Builder()
//                .url(ApiHost + url)
//                .post(requestBody)
//                .tag(url)
//                .build();
//        //开始之前
//        if (callback != null) {
//            callback.onBeforeRequest(request);
//        }
//        //异步结果
//        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(final Call call, final IOException e) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callback.onFailure(call, e);
//                        callback.onFinish(request);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(final Call call, final Response response) throws IOException {
//                Log.e(TAG, "onSuccess333onResponse: " + response.toString() );
//                if (response.isSuccessful()) {
//                    //返回数据成功的话就解析json串
//                    try {
//                        String json = response.body().string();
//
//                        final AiYunBeanCommonHttpResult o = mGson.fromJson(json, callback.mType);//将json解析成对应的bean
//                        if (o.getData() != null) {
//                            String data = Arithmetic.decryptBASE64(o.getData(), Arithmetic.APP_KEY);
//                            o.setData(data);
//                        }
//                        //handler 除了可以发送Message 也可以发送 runnable对象
//                        //这个runnable对象的run方法就执行在 handler对应的线程中 当前的案例中 会执行在主线程中
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //将response返回给主线程
//                                callback.onSuccess(response, o, mGson);
//                                callback.onFinish(request);
//                            }
//                        });
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JsonSyntaxException e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            callback.onError(response, "服务器返回错误");
//                            callback.onFinish(request);
//                        }
//                    });
//                }
//            }
//        });
//
//    }

    /**
     * 取消指定tag的任务
     *
     * @param tag
     */
    public void cancel(String tag) {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        synchronized (dispatcher) {
            for (Call call : dispatcher.queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                    Log.e(TAG, "cancel: queuedCalls" );
                }
            }
            for (Call call : dispatcher.runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                    Log.e(TAG, "cancel:runningCalls " );
                }
            }
        }
    }

    /**
     * @param image_url
     * @param callback
     */
    public void getNetworkImage(String image_url, @NonNull final OnGetNetworkImage callback) {
        //创建OkHttpClient针对某个url的数据请求
        Request request = new Request.Builder().url(image_url).build();
        Call call = mOkHttpClient.newCall(request);

        //请求加入队列
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //此处处理请求失败的业务逻辑
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //我写的这个例子是请求一个图片
                //response的body是图片的byte字节
                byte[] bytes = response.body().bytes();
                //response.body().close();

                //把byte字节组装成图片
//                final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                callback.onGetImage(bytes);

            }
        });
    }

    /**
     * 获取到网络图片时回调
     */
    public interface OnGetNetworkImage {
        void onGetImage(byte[] bytes);
    }

    /**
     * @param url      下载连接
     * @param saveDir  储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 下载失败
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        Log.e(TAG, "isExistDir: " + savePath);
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(File file);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }


    private String handleResponseError(Throwable t) {

        String msg = "未知错误";
        if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }
        return  msg;
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}

