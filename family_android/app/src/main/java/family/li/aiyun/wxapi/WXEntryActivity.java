package family.li.aiyun.wxapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.ShareInfo;
import family.li.aiyun.util.DesUtils;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.QRCodeUtils;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;
import static family.li.aiyun.LiApplication.iwxapi;
import static family.li.aiyun.LiApplication.mTencent;


/**
 * Created by keyC on 2019/6/18.
 * 微信分享
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler, View.OnClickListener {

    private ImageView mIvBack;
    private ImageView mIvQRCode;
    private ImageView mIvQQ;
    private ImageView mIvWX;
    private ImageView mIvSMS;
    private TextView mTvText;

//    private IWXAPI iwxapi;
//
//    private Tencent mTencent;// 新建Tencent实例用于调用分享方法

    private List<ShareInfo> mShareInfo = new ArrayList<>();
    private String phone = ""; //邀请人的电话
    private String name = ""; //邀请人的姓名
    private String be_user_id = ""; //被邀请人的user_id
    private String be_name = ""; //被邀请人的姓名

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        //微信
//        iwxapi = WXAPIFactory.createWXAPI(this, "wx3ace2439c788cbe8", true);
        iwxapi.handleIntent(getIntent(), this);
//        iwxapi.registerApp("wx3ace2439c788cbe8");
//        //qq
//        mTencent = Tencent.createInstance("1106539471", getApplicationContext());

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvQRCode = (ImageView) findViewById(R.id.iv_qr_code);
        mIvQQ = (ImageView) findViewById(R.id.iv_qq);
        mIvWX = (ImageView) findViewById(R.id.iv_wx);
        mIvSMS = (ImageView) findViewById(R.id.iv_sms);
        mTvText = (TextView) findViewById(R.id.tv_text);

        mIvBack.setOnClickListener(this);
        mIvQQ.setOnClickListener(this);
        mIvWX.setOnClickListener(this);
        mIvSMS.setOnClickListener(this);

        //邀请人的电话
        if (getIntent().getStringExtra("phone") != null) {
            phone = getIntent().getStringExtra("phone");
        }
        //邀请人的姓名
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        }
        //被邀请人的user_id
        if (getIntent().getStringExtra("be_user_id") != null) {
            be_user_id = getIntent().getStringExtra("be_user_id");
        }
//        //被邀请人的姓名
//        if (getIntent().getStringExtra("be_name") != null) {
//            be_name = getIntent().getStringExtra("be_name");
//            mTvText.setText("邀请 " + be_name + " 加入");
//            mIvSMS.setVisibility(View.GONE);
//        } else {
//            mTvText.setText("天下李氏一家亲");
//            mIvSMS.setVisibility(View.VISIBLE);
//        }

        //获取分享类容
        getShareConfig();

        String url = "http://sit.zuapi.a56999.com/web/register?user_id=" + Utils.USER_ID;
        //创建分享二维码
        createShareCode(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:   //返回上一页
                finish();
                break;

            case R.id.iv_qq:   //分享到qq
                shareToQQ();
                break;

            case R.id.iv_wx:  //分享到微信
                shareToWX();
                break;

            case R.id.iv_sms:   //短信分享
                shareToSMS();
                break;
        }
    }

    /**
     * 获取分享类容
     */
    private void getShareConfig() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("user_id", Utils.USER_ID);
        param.put("phone", phone);
        param.put("name", name);
        param.put("be_user_id", be_user_id);
        param.put("be_name", be_name);
        HttpManager.getInstance().post("index/Share/getShareContent", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    mShareInfo = new Gson().fromJson(json, new TypeToken<List<ShareInfo>>(){}.getType());
                    if (mShareInfo != null && mShareInfo.size() > 0 && mShareInfo.get(0) != null) {
//                        createShareCode(mShareInfo.get(0).getUrl());
                    }
                }
            }

            @Override
            public void requestFail(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestError(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestStart() {

            }

            @Override
            public void requestComplete() {

            }
        });
    }

    /**
     * 生成二维码
     */
    private void createShareCode(String url) {
        try {
            Bitmap logoBitmap = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Drawable vectorDrawable = getDrawable(R.mipmap.logo);
                logoBitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                        vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(logoBitmap);
                vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                vectorDrawable.draw(canvas);
            }else {
                logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
            }
            Bitmap code = QRCodeUtils.createQRCodeBitmap(url, 240, logoBitmap, 0.2F);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth=dm.widthPixels;
            if(code.getWidth() <= screenWidth){
                mIvQRCode.setImageBitmap(code);
            }else{
                Bitmap bmp = Bitmap.createScaledBitmap(code, screenWidth, code.getHeight()*screenWidth/code.getWidth(), true);
                mIvQRCode.setImageBitmap(bmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast("获取二维码失败，关闭重新获取");
        }
    }

    /**
     * 分享到微信
     */
    private void shareToWX() {
        if (mShareInfo != null && mShareInfo.size() > 0) {
            if (mShareInfo.size() > 2) {
                ShareInfo shareInfo = mShareInfo.get(2);
                if (shareInfo != null) {
                    share(1, shareInfo);
                } else {
                    ToastUtil.showToast("分享内容获取失败");
                }
            } else {
                ShareInfo shareInfo = mShareInfo.get(0);
                if (shareInfo != null) {
                    share(1, shareInfo);
                } else {
                    ToastUtil.showToast("分享内容获取失败");
                }
            }
        } else {
            ToastUtil.showToast("分享内容获取失败");
        }
    }

    /**
     * 分享朋友圈
     */
    private void shareToSMS() {
        if (mShareInfo != null && mShareInfo.size() > 0) {
            if (mShareInfo.size() > 3) {
                ShareInfo shareInfo = mShareInfo.get(3);
                if (shareInfo != null) {
                    share(2, shareInfo);
                } else {
                    ToastUtil.showToast("分享内容获取失败");
                }
            } else {
                ShareInfo shareInfo = mShareInfo.get(0);
                if (shareInfo != null) {
                    share(2, shareInfo);
                } else {
                    ToastUtil.showToast("分享内容获取失败");
                }
            }
        } else {
            ToastUtil.showToast("分享内容获取失败");
        }
    }

    /**
     * 分享
     * @param type
     */
    private void share(int type, ShareInfo shareInfo) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = shareInfo.getUrl();
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = shareInfo.getTitle();
        msg.description = shareInfo.getDescription();
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        msg.thumbData = bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("Req");
        req.message = msg;
        switch (type) {
            case 1:  //微信
                req.scene = WXSceneSession;
                break;
            case 2:  //朋友圈
                req.scene = WXSceneTimeline;
                break;
        }
        iwxapi.sendReq(req);
    }

    /**
     * 分享到qq
     */
    private void shareToQQ() {
        ShareInfo shareInfo = mShareInfo.get(0);
        if (shareInfo != null) {
            final Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, shareInfo.getTitle());
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  shareInfo.getDescription());
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareInfo.getImg_url());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  shareInfo.getUrl());
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "李氏族谱");
            mTencent.shareToQQ(WXEntryActivity.this, params, new MyIUiListener());
        } else {
            ToastUtil.showToast("分享内容获取失败");
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }
        ToastUtil.showToast(result);
        finish();
        Log.i("TAG", "onResp: " + resp.errCode);
    }

    class MyIUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            // 操作成功
            Log.i("TAG", "share-qq-onComplete: " + o.toString());
        }
        @Override
        public void onError(UiError uiError) {
            // 分享异常
            Log.i("TAG", "share-qq-onComplete: " + uiError.errorMessage);
            Log.i("TAG", "share-qq-onComplete: " + uiError.errorDetail);
            Log.i("TAG", "share-qq-onComplete: " + uiError.errorCode);
        }
        @Override
        public void onCancel() {
            // 取消分享
            Log.i("TAG", "share-qq-onComplete: " );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, new MyIUiListener());
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, new MyIUiListener());
            }
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
