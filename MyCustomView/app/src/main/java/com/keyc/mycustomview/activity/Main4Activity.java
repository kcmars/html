package com.keyc.mycustomview.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.Kin;
import com.keyc.mycustomview.bean.Person;
import com.keyc.mycustomview.listener.OnKinViewsClickListener;
import com.keyc.mycustomview.utils.Utils;
import com.keyc.mycustomview.view.FamilyTreeView;
import com.keyc.mycustomview.view.HVScrollView;
import com.nineoldandroids.view.ViewHelper;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private HVScrollView mHVScrollView;
    private FamilyTreeView ftvTree;
    private int mWidth;   //屏幕宽度
    private int mHeight;   //屏幕高度
    private Button btnEnlarge;  //放大
    private Button btnShrinkDown; //缩小
    // 初始化缩放中心
    private float mPointX;
    private float mPointY;
    private float mCurrentScale = 1f;//当前缩放比例
    private float mMaxScale = 1f;//最大缩放比例
    private float mMinScale = 0.1f;//最小缩放比例

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());
        //获取屏幕宽高
        WindowManager wm = this.getWindowManager();
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
        mPointX = mWidth/2;
        mPointY = mHeight/2;

        btnEnlarge = (Button) findViewById(R.id.btn_enlarge);
        btnShrinkDown = (Button) findViewById(R.id.btn_shrink_down);
        mHVScrollView = (HVScrollView) findViewById(R.id.h_v_scrollview);
        ftvTree = (FamilyTreeView) findViewById(R.id.ftv_tree);

        btnEnlarge.setOnClickListener(this);
        btnShrinkDown.setOnClickListener(this);
        mHVScrollView.setOnTouchListener(this);

        /**
         * 读取json数据，并解析成对应的bean对象
         */
        String json = Utils.getJson("kin.json", this);
        Kin kin = new Gson().fromJson(json, new TypeToken<Kin>(){}.getType());

        /**
         * 添加数据
         */
        ftvTree.setData(kin, mWidth, mHeight);

        /**
         * 添加点击事件
         */
        ftvTree.setOnViewsClick(new OnKinViewsClickListener() {
            @Override
            public void onViewsClick(Person person) {
                Toast.makeText(Main4Activity.this, person.getName() + "（"+person.getSex() + "）", Toast.LENGTH_SHORT).show();
            }
        });

        ftvTree.measure(0, 0);
        Log.i("TAG", "onCreate: w==" + ftvTree.getMeasuredWidth());
        Log.i("TAG", "onCreate: h==" + ftvTree.getMeasuredHeight());

        float w_s = (float) mWidth/(float) ftvTree.getMeasuredWidth();
        float h_s = (float) mHeight/(float) ftvTree.getMeasuredHeight();

        Log.i("TAG", "onCreate: mWidth==" + mWidth);
        Log.i("TAG", "onCreate: mHeight==" + mHeight);

        Log.i("TAG", "onCreate: w_s==" + w_s);
        Log.i("TAG", "onCreate: h_s==" + h_s);

        mCurrentScale = w_s < h_s ? w_s : h_s;
        ftvTree.setPivotX(0);
        ftvTree.setPivotY(mPointY);
        ftvTree.setScaleX(mCurrentScale);
        ftvTree.setScaleY(mCurrentScale);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enlarge:
                enlarge();
                break;
            case R.id.btn_shrink_down:
                shrink();
                break;
        }
    }

    /**
     * 放大
     */
    private void enlarge() {
        if (mCurrentScale < mMaxScale) {
            mCurrentScale += 0.05f;
            ftvTree.setPivotX(mPointX);
            ftvTree.setPivotY(mPointY);
            ftvTree.setScaleX(mCurrentScale);
            ftvTree.setScaleY(mCurrentScale);
        }
    }

    /**
     * 缩小
     */
    private void shrink() {
        if (mCurrentScale > mMinScale) {
            mCurrentScale -= 0.05f;
            ftvTree.setPivotX(mPointX);
            ftvTree.setPivotY(mPointY);
            ftvTree.setScaleX(mCurrentScale);
            ftvTree.setScaleY(mCurrentScale);
        }
    }


    /**
     * 设置滑动监听，主要判断两指缩放
     */
    // 初始的两个手指按下的触摸点的距离
    private float oriDis = 1f;
    private int mCurrentX;//当前X轴偏移量
    private int mCurrentY;//当前Y轴偏移量
    private int mLastTouchX;//最后一次触摸的X坐标
    private int mLastTouchY;//最后一次触摸的Y坐标
    private int mLastInterceptX;
    private int mLastInterceptY;
    private ScaleGestureDetector mScaleGestureDetector = null;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("TAG", "onTouchEvent: " + event.getPointerCount());
        if (event.getPointerCount() == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mCurrentX = mHVScrollView.getScrollX();
                    mCurrentY = mHVScrollView.getScrollY();
                    mLastTouchX = (int) event.getX();
                    mLastTouchY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    final int currentTouchX = (int) event.getX();
                    final int currentTouchY = (int) event.getY();

                    final int distanceX = currentTouchX - mLastTouchX;
                    final int distanceY = currentTouchY - mLastTouchY;

                    mCurrentX -= distanceX;
                    mCurrentY -= distanceY;

//                if (mCurrentX < getLeft()) {
//                    mCurrentX = getLeft();
//                } else if (mCurrentX > getRight() - mShowWidthPX) {
//                    mCurrentX = getRight() - mShowWidthPX;
//                }
//
//                if (mCurrentY < getTop()) {
//                    mCurrentY = getTop();
//                } else if (mCurrentY > getBottom() - mShowHeightPX) {
//                    mCurrentY = getBottom() - mShowHeightPX;
//                }

                    mHVScrollView.scrollBy(-distanceX, -distanceY);
                    mPointX = mWidth/2 + mHVScrollView.getScrollX();
                    mPointY = mHeight/2 + mHVScrollView.getScrollY();
                    mLastTouchX = currentTouchX;
                    mLastTouchY = currentTouchY;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        } else {
            return mScaleGestureDetector.onTouchEvent(event);
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    // 两个手指滑动
//                    float newDist = distance(event);
//                    if (newDist - oriDis >= 5f) {
//                        enlarge();
//                        oriDis = newDist;
//
//                    } else if (newDist - oriDis <= -5f) {
//                        shrink();
//                        oriDis = newDist;
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    break;
//            }
//            return true;
        }
    }

    /**
     * 计算两手指间的距离
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
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
            ViewHelper.setScaleX(ftvTree, scale );// x方向上缩小
            ViewHelper.setScaleY(ftvTree, scale );// y方向上缩小

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
