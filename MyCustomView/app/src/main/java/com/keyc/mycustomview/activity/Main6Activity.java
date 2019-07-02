package com.keyc.mycustomview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.KinMember;
import com.keyc.mycustomview.listener.OnKinMemberViewsClickListener;
import com.keyc.mycustomview.utils.Utils;
import com.keyc.mycustomview.view.FamilyTreeCustomView;
import com.keyc.mycustomview.view.HVScrollView;

public class Main6Activity extends AppCompatActivity implements View.OnClickListener {

    private HVScrollView mHVScrollView;
    private RelativeLayout mRelativeLayoutOne;
    private Button btnEnlarge;  //放大
    private Button btnShrinkDown; //缩小
    private int mWidth;   //屏幕宽度
    private int mHeight;   //屏幕高度
    // 初始化缩放中心
    private float mPointX;
    private float mPointY;
    private float mCurrentScale = 1f;//当前缩放比例
    private float mMaxScale = 1.5f;//最大缩放比例
    private float mMinScale = 0.5f;//最小缩放比例

    private FamilyTreeCustomView ftvTree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        //获取屏幕宽高
        WindowManager wm = this.getWindowManager();
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
        mPointX = mWidth/2;
        mPointY = mHeight/2;

        btnEnlarge = (Button) findViewById(R.id.btn_enlarge);
        btnShrinkDown = (Button) findViewById(R.id.btn_shrink_down);
        mHVScrollView = (HVScrollView) findViewById(R.id.hv_scrollview_6);
        mRelativeLayoutOne = (RelativeLayout) findViewById(R.id.layout_6);

        btnEnlarge.setOnClickListener(this);
        btnShrinkDown.setOnClickListener(this);
//        mHVScrollView.setOnTouchListener(this);

        ftvTree = new FamilyTreeCustomView(this);

        /**
         * 添加点击事件
         */
        ftvTree.setOnViewsClick(new OnKinMemberViewsClickListener() {
            @Override
            public void onViewsClick(KinMember kinMember) {
                Toast.makeText(Main6Activity.this, kinMember.getCall() + "  " + kinMember.getName() + "（"+kinMember.getSex() + "）", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 读取json数据，并解析成对应的bean对象
         */
        String json = Utils.getJson("kinMember.json", this);
        KinMember kinMember = new Gson().fromJson(json, new TypeToken<KinMember>(){}.getType());

        /**
         * 添加数据
         */
        ftvTree.setData(kinMember);

        /**
         * 重新测量被添加的视图的宽高，然后告诉父view的LayoutParams
         * 将view.measure(0,0)设置为0的时候表示，此时不需要考虑父控件的布局问题，直接使用getMeasuredWidth和getMeasuredHeight获取此view的自身的实际大小
         * 将0解包后，所对应的 SpecMode = 0，SpecSize = 0. SpecMode 0 对应的模式为 UNSPECIFIED.
         * UNSPECIFIED的官方解释：父容器不会对子元素加以任何约束，子元素可以是任何大小。
         */
        ftvTree.measure(0, 0);

        /**
         * 将视图滚动到目标位置（以自我为中心）
         */
//        int left = (int) (ftvTree.getMeasuredWidth() - mWidth)/2;
//        int top = (int) (ftvTree.getMeasuredHeight() - mHeight)/2;
//        mHVScrollView.scrollTo(left, top);
//        mRelativeLayoutOne.scrollTo(left, top);
//        ftvTree.scrollToPosition(left, top);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ftvTree.getMeasuredWidth(), ftvTree.getMeasuredHeight());
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;
        mRelativeLayoutOne.addView(ftvTree, layoutParams);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enlarge:
                if (mCurrentScale < mMaxScale) {
                    mCurrentScale += 0.01f;
                    mRelativeLayoutOne.setPivotX(mPointX);
                    mRelativeLayoutOne.setPivotY(mPointY);
                    mRelativeLayoutOne.setScaleX(mCurrentScale);
                    mRelativeLayoutOne.setScaleY(mCurrentScale);
                }
                break;
            case R.id.btn_shrink_down:
                if (mCurrentScale > mMinScale) {
                    mCurrentScale -= 0.01f;
                    mRelativeLayoutOne.setPivotX(mPointX);
                    mRelativeLayoutOne.setPivotY(mPointY);
                    mRelativeLayoutOne.setScaleX(mCurrentScale);
                    mRelativeLayoutOne.setScaleY(mCurrentScale);
                }
                break;
        }
    }

//    // 初始的两个手指按下的触摸点的距离
//    private float oriDis = 1f;
//    private int mCurrentX;//当前X轴偏移量
//    private int mCurrentY;//当前Y轴偏移量
//    private int mLastTouchX;//最后一次触摸的X坐标
//    private int mLastTouchY;//最后一次触摸的Y坐标
//    private int mLastInterceptX;
//    private int mLastInterceptY;
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        Log.i("TAG", "onTouchEvent: count=" + event.getPointerCount());
//        Log.i("TAG", "onTouchEvent: action=" + event.getAction());
//        if (event.getPointerCount() == 1) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                mCurrentX = ftvTree.getScrollX();
//                mCurrentY = ftvTree.getScrollY();
//                mLastTouchX = (int) event.getX();
//                mLastTouchY = (int) event.getY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    final int currentTouchX = (int) event.getX();
//                    final int currentTouchY = (int) event.getY();
//
//                    final int distanceX = currentTouchX - mLastTouchX;
//                    final int distanceY = currentTouchY - mLastTouchY;
//
//                    mCurrentX -= distanceX;
//                    mCurrentY -= distanceY;
//
//                    Log.i("TAG", "onTouchEvent: mCurrentX=" + mCurrentX);
//
////                if (mCurrentX < getLeft()) {
////                    mCurrentX = getLeft();
////                } else if (mCurrentX > getRight() - mShowWidthPX) {
////                    mCurrentX = getRight() - mShowWidthPX;
////                }
////
////                if (mCurrentY < getTop()) {
////                    mCurrentY = getTop();
////                } else if (mCurrentY > getBottom() - mShowHeightPX) {
////                    mCurrentY = getBottom() - mShowHeightPX;
////                }
//
//                    ftvTree.scrollTo(mCurrentX, mCurrentY);
//                    mLastTouchX = currentTouchX;
//                    mLastTouchY = currentTouchY;
//                    break;
//                case MotionEvent.ACTION_UP:
//                    break;
//            }
//        } else if (event.getPointerCount() == 2){
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    // 两个手指滑动
//                    float newDist = distance(event);
//                    if (newDist - oriDis >= 5f) {
//                        ftvTree.doEnlarge();
//                        oriDis = newDist;
//
//                    } else if (newDist - oriDis <= -5f) {
//                        ftvTree.doShrinkDown();
//                        oriDis = newDist;
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    break;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 计算两手指间的距离
//     */
//    private float distance(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
}
