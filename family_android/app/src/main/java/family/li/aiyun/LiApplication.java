package family.li.aiyun;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import family.li.aiyun.util.Utils;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;


/**
 * Created by keyC on 2019/6/12.
 */

@SuppressLint("Registered")
public class LiApplication extends Application {

    public static LiApplication liApplication;
    /**
     * 全局上下文， 用于在工具类中使用 Context
     */
    public static Context mContext;

    public static LiApplication getInstance() {
        return liApplication;
    }

    public static IWXAPI iwxapi;

    public static Tencent mTencent;// 新建Tencent实例用于调用分享方法


    /**
     * 获取全局上下文*/
    public static Context getContext() {
        return mContext;
    }


    {
        //友盟快速集成分享sdk
//        PlatformConfig.setWeixin("wx9c168100887efcd2","a21c66063dd51a81125cbcc9f25e36b4");
//        PlatformConfig.setQQZone("1105836969","iPePmeE2LFi2I8CZ");
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(getApplicationContext());
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(getApplicationContext());
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        liApplication = this;
        mContext = getApplicationContext();

        //微信
        iwxapi = WXAPIFactory.createWXAPI(this, "wx3ace2439c788cbe8", true);
        iwxapi.registerApp("wx3ace2439c788cbe8");
        //qq
        mTencent = Tencent.createInstance("1106539471", getApplicationContext());

        //二维码 --- 扫码
        ZXingLibrary.initDisplayOpinion(this);

        //获取版本号
        Utils.getVersionCode(this);

        //注册阿里云推送
        PushServiceFactory.init(this);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(this, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("TAG", "init cloudchannel success");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.i("TAG", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}

