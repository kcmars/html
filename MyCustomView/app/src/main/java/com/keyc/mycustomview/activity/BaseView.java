package com.keyc.mycustomview.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.keyc.mycustomview.R;

/**
 * Created by Administrator on 2019/6/11.
 */

public class BaseView extends ViewGroup {
    public BaseView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.test,this);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
