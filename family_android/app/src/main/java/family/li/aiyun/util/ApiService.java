package family.li.aiyun.util;


import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by keyC on 2019/6/12
 */

public interface ApiService {

    /**
     * get 网络请求
     */

    @GET("{url}")
    Observable<ResponseBody> executeGet(@Path("url") String url,
                                        @QueryMap Map<String, String> maps);
    @FormUrlEncoded
    @POST("{url}")
    Call<ResponseBody> netWork(@Path("url") String url,
                               @FieldMap Map<String, String> maps);

    /**
     * post 网络 *
     */
    @FormUrlEncoded
    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            @FieldMap Map<String, String> maps);

    /*上传图片*/
    @Multipart
    @POST("{url}")
    Observable<ResponseBody> uploadImg(@Part MultipartBody.Part imageFile);

    /**
     * 没有参数的 post 请求
     */
    @POST("{url}")
    Observable<ResponseBody> post(@Path("url") String url);
    /**
     * RXJAVA schedulersTransformer
     * AndroidSchedulers.mainThread()
     */
    final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    /**
     * Json 提交
     * */
  //  @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST
    Observable<ResponseBody>getMessage(@Url String url, @Body RequestBody info);   // 请求体味RequestBody 类型
}
