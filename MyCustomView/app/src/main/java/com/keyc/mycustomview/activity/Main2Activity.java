package com.keyc.mycustomview.activity;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.Person;
import com.keyc.mycustomview.bean.Kin;
import com.keyc.mycustomview.view.DrawGeometryView;
import com.keyc.mycustomview.view.HVScrollView;
import com.keyc.mycustomview.view.LineCustomView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private HVScrollView mHVScrollView;
    private RelativeLayout mRelativeLayoutOne;
    private RelativeLayout.LayoutParams layoutParams;
    private RelativeLayout.LayoutParams layoutParams1;
    private int bt_x = 200;  //定义按钮的大小
    private int bt_margin = 100;  //定义按钮的间距
    private int mWidth;   //屏幕宽度
    private int mHeight;   //屏幕高度
    private Button btnEnlarge;  //放大
    private Button btnShrinkDown; //缩小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //获取屏幕宽高
        WindowManager wm = this.getWindowManager();
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
        mPointX = mWidth/2;
        mPointY = mHeight/2;

        btnEnlarge = (Button) findViewById(R.id.btn_enlarge);
        btnShrinkDown = (Button) findViewById(R.id.btn_shrink_down);
        mHVScrollView = (HVScrollView) findViewById(R.id.hv_scrollview);
        mRelativeLayoutOne = (RelativeLayout) findViewById(R.id.layout_one);
        mHVScrollView.setOnTouchListener(this);

        btnEnlarge.setOnClickListener(this);
        btnShrinkDown.setOnClickListener(this);

        //准备对象
        Kin kin = new Kin();
        Person self = new Person();
        Person spouse = new Person();
        ArrayList<Kin> children = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Kin kin1 = new Kin();
                Person self1 = new Person();
                Person spouse1 = new Person();
                ArrayList<Kin> children1 = new ArrayList<>();
                for (int j = 0; j < 5; j++) {
                    Kin kin2 = new Kin();
                    Person self2 = new Person();
                    Person spouse2 = new Person();
                    ArrayList<Kin> children2 = new ArrayList<>();

                    kin2.setSelf(self2);
                    kin2.setSpouse(spouse2);
                    kin2.setChildren(children2);
                    children1.add(kin2);
                }

                kin1.setSelf(self1);
                kin1.setSpouse(spouse1);
                kin1.setChildren(children1);
                children.add(kin1);
            }

        kin.setSelf(self);
        kin.setSpouse(spouse);
        kin.setChildren(children);


        //添加按钮
        drawButton(kin, 10, 10);
    }

//     let Kin = {
//         self: {name: "张一", sex: "男"},
//         spouse: {name: "张一配", sex: "女"},
//         children: [{
//             self: {name: "张二一", sex: "男"},
//             spouse: {name: "张二一配", sex: "女"},
//             children: [{
//                 self: {name: "张二一一", sex: "女"},
//                 spouse: {name: "张二一一配", sex: "男"},
//                 children: []
//             }, {
//                 self: {name: "张二一二", sex: "男"},
//                 spouse: null,
//                 children: []
//             }]
//         }, {
//             self: {name: "张二二", id: 1, sex: "男"},
//             spouse: {name: "张二二配", id: 2, sex: "女"},
//             children: [{
//                 self: {name: "张二二一", id: 1, sex: "男"},
//                 spouse: {name: "张二二一配", id: 2, sex: "女"},
//                 children: [{
//                     self: {name: "张二二一一", sex: "男"},
//                     spouse: null,
//                     children: []
//                 }]
//             }]
//         }]
//     };


    /**
     * 添加子元素button按钮
     * 递归模式添加（自己，配偶，孩子）
     * @param kin    成员对象（自己，配偶，孩子（自己，配偶，孩子（循环）））
     * @param left   左边距
     * @param top    上边距
     */
    @SuppressLint("SetTextI18n")
    private void drawButton(Kin kin, int left, int top) {
        Person self = kin.getSelf();
        Person spouse = kin.getSpouse();
        ArrayList<Kin> kins = kin.getChildren();
        /**
         * 自己
         */
        if (self != null) {
            Button button = new Button(this);
            button.setBackgroundResource(R.drawable.allokbutton);
            button.setText(self.getName() + "\\n" + self.getSex());
            button.setTextColor(Color.WHITE);

            layoutParams = new RelativeLayout.LayoutParams(bt_x, bt_x);
            layoutParams.leftMargin = left;
            layoutParams.topMargin = top;

            mRelativeLayoutOne.addView(button, layoutParams);
        }
        /**
         * 配偶
         */
        if (spouse != null) {
            Button button1 = new Button(this);
            button1.setBackgroundResource(R.drawable.allokbutton);
            button1.setText(spouse.getName() + "\\n" + spouse.getSex());
            button1.setTextColor(Color.WHITE);

            layoutParams = new RelativeLayout.LayoutParams(bt_x, bt_x);
            layoutParams.leftMargin = left + bt_x + bt_margin;
            layoutParams.topMargin = top;

            mRelativeLayoutOne.addView(button1, layoutParams);

            /**
             * 画自己与配偶的连线
             */
//            DrawGeometryView view = new DrawGeometryView(this, 50, 50, left + bt_x + bt_margin, top, null);
//            layoutParams1 = new RelativeLayout.LayoutParams(500, 500);
//            view.invalidate();
//            layoutParams1.topMargin = left;// line_y-600;//Math.min(line_y+100,button_y+100
//            layoutParams1.leftMargin = top;// line_x+300;
//            mRelativeLayoutOne.addView(view, layoutParams1);
            LineCustomView lineCustomView = new LineCustomView(this, 0, top + bt_x/2, bt_margin, top + bt_x/2);
            layoutParams1 = new RelativeLayout.LayoutParams(bt_x, bt_x);
            layoutParams1.leftMargin = left + bt_x;
            layoutParams1.topMargin = top;
            mRelativeLayoutOne.addView(lineCustomView, layoutParams1);
        }
        /**
         * 孩子
         */
        if (kins != null && kins.size() > 0) {
            for (int i = 0; i < kins.size(); i++) {
                drawButton(kins.get(i), left + i * (bt_x + bt_margin) * 2, top + bt_x + bt_margin);
            }
        }
    }

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    // 第一个按下的手指的点
    private float downX = 0;
    private float downY = 0;
    private int moveX;    //移动距离x
    private int moveY;    //移动距离y
    private float mScale = 1;  //缩放比例
    // 初始的两个手指按下的触摸点的距离
    private float oriDis = 1f;
    // 初始化缩放中心
    private float mPointX;
    private float mPointY;

    @SuppressLint("ClickableViewAccessibility")

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // 第一个手指按下事件
                downX = event.getX();
                downY = event.getY();
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // 第二个手指按下事件
                oriDis = distance(event);
                if (oriDis > 10f) {
                    mode = ZOOM;
                }
                Log.i("TAG", "onTouch1111-mPointX=: " + mPointX + "---mPointY=" + mPointY);
                mRelativeLayoutOne.setPivotX(mPointX);
                mRelativeLayoutOne.setPivotY(mPointY);
                mRelativeLayoutOne.setScaleX(mScale);
                mRelativeLayoutOne.setScaleY(mScale);

                break;
            case MotionEvent.ACTION_UP:
                float px = mWidth/2 + mHVScrollView.getScrollX();
                float py = mHeight/2 + mHVScrollView.getScrollY();
                mPointX = px;
                mPointY = py;
                Log.i("TAG", "onTouch1111-px=: " + px + "---py=" + py);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 手指放开事件
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) (event.getX() - downX);
                moveY = (int) (event.getY() - downY);
                // 手指滑动事件
                if (mode == DRAG) {
                    if (Math.abs(moveX) > 10 || Math.abs(moveY) > 10) {
                        // 一个手指拖动
                        mHVScrollView.scrollBy(-moveX, -moveY);
                        mPointX = mWidth/2 + mHVScrollView.getScrollX();
                        mPointY = mHeight/2 + mHVScrollView.getScrollY();
                        downX = event.getX();
                        downY = event.getY();
                    }
                } else if (mode == ZOOM) {
                    // 两个手指滑动
                    float newDist = distance(event);
                    if (newDist - oriDis >= 5f) {
                        mScale+=0.01;
                        // 缩放view，设置缩放中心和缩放大小
                        mPointX = mWidth/2 + mHVScrollView.getScrollX();
                        mPointY = mHeight/2 + mHVScrollView.getScrollY();
                        mRelativeLayoutOne.setPivotX(mPointX);
                        mRelativeLayoutOne.setPivotY(mPointY);
                        mRelativeLayoutOne.setScaleX(mScale);
                        mRelativeLayoutOne.setScaleY(mScale);
                        oriDis = newDist;
                        
                    } else if (newDist - oriDis <= -5f) {
                        mScale-=0.01;
                        // 缩放view，设置缩放中心和缩放大小
                        mPointX = mWidth/2 + mHVScrollView.getScrollX();
                        mPointY = mHeight/2 + mHVScrollView.getScrollY();
                        mRelativeLayoutOne.setPivotX(mPointX);
                        mRelativeLayoutOne.setPivotY(mPointY);
                        mRelativeLayoutOne.setScaleX(mScale);
                        mRelativeLayoutOne.setScaleY(mScale);
                        oriDis = newDist;
                    }
                }
                break;
        }
        return true;
    }

    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float mCurrentScale = 1f;//当前缩放比例
    private float mMaxScale = 1.5f;//最大缩放比例
    private float mMinScale = 0.5f;//最小缩放比例

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
}
