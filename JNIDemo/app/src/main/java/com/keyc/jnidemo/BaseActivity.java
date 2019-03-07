package com.keyc.jnidemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.keyc.jnidemo.helper.BaseClick;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class BaseActivity extends AppCompatActivity implements View.OnClickListener, BaseClick {

    private static final String TAG = "BaseActivity-TAG";

    private FrameLayout mContentLayout;
    private FrameLayout mLoadingLayout;
    private FrameLayout mTitleBar;
    private TextView mTvLeft;
    private TextView mTvTitle;
    private TextView mTvRight;
    private ImageView mIvLeft;
    private ImageView mIvRight;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        mTitleBar = (FrameLayout) findViewById(R.id.fl_title_bar);
        mContentLayout = (FrameLayout) findViewById(R.id.layout_content);
        mLoadingLayout = (FrameLayout) findViewById(R.id.fl_loading);
        mTvLeft = (TextView) findViewById(R.id.tv_left);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvRight = (TextView) findViewById(R.id.tv_right);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mIvRight = (ImageView) findViewById(R.id.iv_right);
        mTvLeft.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mIvLeft.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mTitleBar.setOutlineProvider(new ViewOutlineProvider() {
            @SuppressLint("NewApi")
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRect(0,0, view.getWidth(), view.getHeight());
            }
        });
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResID, mContentLayout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
    }

    //设置标题
    @Override
    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    /**
     * 显示左侧按钮
     * @param resourceId 资源id
     * @param show 是否显示
     * @param type 显示类型 文字 图片
     */
    public void showLeft(int resourceId, boolean show, String type) {
        if (show) {
            switch (type) {
                case "text":
                    mTvLeft.setText(resourceId);
                    mTvLeft.setVisibility(View.VISIBLE);
                    mIvLeft.setVisibility(View.GONE);
                    break;

                case "img":
                    mIvLeft.setImageResource(resourceId);
                    mIvLeft.setVisibility(View.VISIBLE);
                    mTvLeft.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        } else {
            mTvLeft.setVisibility(View.GONE);
            mIvLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 显示右侧按钮
     * @param resourceId 资源id
     * @param show 是否显示
     * @param type 显示类型 文字 图片
     */
    public void showRight(int resourceId, boolean show, String type) {
        if (show) {
            switch (type) {
                case "text":
                    mTvRight.setText(resourceId);
                    mTvRight.setVisibility(View.VISIBLE);
                    mIvRight.setVisibility(View.GONE);
                    break;

                case "img":
                    mIvRight.setImageResource(resourceId);
                    mIvRight.setVisibility(View.VISIBLE);
                    mTvRight.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        } else {
            mTvRight.setVisibility(View.GONE);
            mIvRight.setVisibility(View.GONE);
        }
    }

    /**
     * 拨打电话
     * @param tel
     */
    public void call(String tel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BaseActivityPermissionsDispatcher.callPhoneWithCheck(this, tel);
        } else {
            callPhone(tel);
        }
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void callPhone(String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        startActivity(intent);
    }

    /**
     * 左侧返回按钮点击事件
     * 一般用于关闭当前页面并返回上一个页面
     */
    @Override
    public void onBackLeft() {
        Log.i(TAG, "onBackLeft: ");
        finish();
    }

    /**
     * 左侧图片按钮点击事件
     * 一般用于在主界面进入个人中心或设置
     */
    @Override
    public void onImgLeft() {
        Log.i(TAG, "onImgLeft: ");
        finish();
    }

    /**
     * 右侧文字按钮点击事件
     * 一般用于在右上角保存资料或者取消业务等
     */
    @Override
    public void onTextRight() {
        Log.i(TAG, "onTextRight: ");
    }

    /**
     * 右侧图片按钮点击事件
     * 一般用于多级页面全部关闭或者显示消息中心
     */
    @Override
    public void onImgRight() {
        Log.i(TAG, "onImgRight: ");
    }

    @Override
    public void onClick(final View v) {
        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.tv_left:
                this.onBackLeft();
                break;

            case R.id.iv_left:
                this.onImgLeft();
                break;

            case R.id.tv_right:
                this.onTextRight();
                break;

            case R.id.iv_right:
                this.onImgRight();
                break;

            default:break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setEnabled(true);
            }
        }, 1000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
