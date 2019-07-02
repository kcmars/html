package com.keyc.mycustomview.activity;

/**
 * Created by Administrator on 2019/6/20.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onBaseIntent();

        onBasePreLayout();

        setContentView(onBaseLayoutId(savedInstanceState));

        onBaseBindView();

        onLoadData();
    }

    /**
     * Intent进来的数据处理
     */
    protected abstract void onBaseIntent();

    /**
     * 设置布局之前的处理
     */
    protected abstract void onBasePreLayout();

    /**
     * 返回布局文件
     *
     * @return id
     */
    protected abstract int onBaseLayoutId(@Nullable Bundle savedInstanceState);

    protected abstract void onBaseBindView();

    /**
     * 加载数据
     */
    protected abstract void onLoadData();

}
