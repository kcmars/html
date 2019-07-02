package com.keyc.mycustomview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by keyC on 2019/6/3.
 * 画线条
 */

@SuppressLint("DrawAllocation")
public class LineCustomView extends View {
    private float startX = 0;
    private float startY = 0;
    private float stopX = 0;
    private float stopY = 0;

    public LineCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineCustomView(Context context, float startX, float startY, float stopX, float stopY) {
        super(context);
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //线条
        Paint mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setAntiAlias(true);   //设置抗锯齿效果
        mLinePaint.setStrokeWidth(3.0f);// 设置笔触宽度
        mLinePaint.setStyle(Paint.Style.STROKE);// 设置画笔的填充类型(完全填充)
        canvas.drawLine(startX, startY, stopX, stopY, mLinePaint);
        Log.i("TAG", "onDraw: " + startX + "---" + startY + "---" + stopX + "---" + stopY);
    }
}
