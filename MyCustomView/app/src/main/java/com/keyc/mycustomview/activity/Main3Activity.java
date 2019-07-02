package com.keyc.mycustomview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.keyc.mycustomview.R;
import com.nineoldandroids.view.ViewHelper;

public class Main3Activity extends AppCompatActivity {
    private ScaleGestureDetector mScaleGestureDetector = null;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_main3, null);
        setContentView(view);

        mScaleGestureDetector = new ScaleGestureDetector(this,
                new ScaleGestureListener());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 返回给ScaleGestureDetector来处理
        return mScaleGestureDetector.onTouchEvent(event);
    }

    public class ScaleGestureListener implements
            ScaleGestureDetector.OnScaleGestureListener {

        private float scale;
        private float preScale = 1;// 默认前一次缩放比例为1

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float previousSpan = detector.getPreviousSpan();
            float currentSpan = detector.getCurrentSpan();
            if (currentSpan < previousSpan) {
                // 缩小
                // scale = preScale-detector.getScaleFactor()/3;
                scale = preScale - (previousSpan - currentSpan) / 1000;
            } else {
                // 放大
                // scale = preScale+detector.getScaleFactor()/3;
                scale = preScale + (currentSpan - previousSpan) / 1000;
            }

            // 缩放view
            ViewHelper.setScaleX(view, scale );// x方向上缩小
            ViewHelper.setScaleY(view, scale );// y方向上缩小

            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // 一定要返回true才会进入onScale()这个函数
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            preScale = scale;//记录本次缩放比例
        }
    }
}
