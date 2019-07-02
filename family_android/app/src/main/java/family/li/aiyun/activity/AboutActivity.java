package family.li.aiyun.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.VersionInfo;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.SystemUtils;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

import java.io.File;
import java.util.HashMap;

public class AboutActivity extends AppActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private TextView mTvVersion;
    private LinearLayout mLlIntro;
    private LinearLayout mLlCheck;

    //新版本更新下载提示
    private MaterialDialog mDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mLlIntro = (LinearLayout) findViewById(R.id.ll_introduction);
        mLlCheck = (LinearLayout) findViewById(R.id.ll_check_version);

        mIvBack.setOnClickListener(this);
        mLlIntro.setOnClickListener(this);
        mLlCheck.setOnClickListener(this);

        mTvVersion.setText("Version  " + Utils.APP_VERSION_NAME);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:  //返回上一页
                finish();
                break;

            case R.id.ll_introduction:  //功能介绍
                intent.setClass(AboutActivity.this, IntroductionActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_check_version:  //检查版本更新
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Utils.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    checkVersion();
                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == Utils.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                checkVersion();
            } else {
                // Permission Denied
                ToastUtil.showToast("未允许系统存储权限！请到系统设置中去设置");
            }
        }
    }

    /**
     * 检查版本更新
     */
    private void checkVersion() {
        HashMap param = new HashMap();
        HttpManager.getInstance().post("index/Config/version", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    VersionInfo versionInfo = new Gson().fromJson(json, new TypeToken<VersionInfo>(){}.getType());;
                    if (versionInfo != null) {
                        uploadDialog(versionInfo);
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
     * 更新提示
     */
    private void uploadDialog(final VersionInfo mVersionInfo) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(AboutActivity.this)
                .title("新版本更新提示")
                .iconRes(R.mipmap.logo)
                .canceledOnTouchOutside(false)
                .content(mVersionInfo.getContent())
                .positiveText("马上更新")
                .negativeText("稍后")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (mDialog != null) {
                            mDialog = null;
                        }
                        mDialog = new MaterialDialog.Builder(AboutActivity.this)
                                .autoDismiss(false)
                                .progress(false, 100, true)
                                .progressNumberFormat("%1d/%2d")
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
                                .build();
                        mDialog.show();
                        //更新操作
                        downloadNewApp(mVersionInfo.getUrl());
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                });
        MaterialDialog   mDialog = builder.build();
        mDialog.show();
    }

    /**
     * 下载新版本
     */
    public void downloadNewApp(String mUrl) {
        Log.i("TAG", "downloadNewApp: " + mUrl);
        HttpManager.getInstance().download(mUrl, "download", new HttpManager.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(final File file) {
                mDialog.dismiss();
                //下载完成后销毁当前的activity，否则不会自动 启动
                finish();
//                Utils.savePreference(AboutActivity.this, "apkFile", file.toString());
                //安装新版本
                SystemUtils.startActionFile(AboutActivity.this, file, "application/vnd.android.package-archive");
            }

            @Override
            public void onDownloading(int progress) {
                mDialog.setProgress(progress);
                Log.i("TAG", "downloadNewApp-progress=: " + progress);
            }

            @Override
            public void onDownloadFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("下载失败");
                    }
                });

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
