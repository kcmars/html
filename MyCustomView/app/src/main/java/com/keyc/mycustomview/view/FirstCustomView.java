package com.keyc.mycustomview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.keyc.mycustomview.R;
import com.keyc.mycustomview.utils.Utils;

/**
 * Created by keyC on 2019/5/22.
 * 自定义view
 */

@SuppressLint("AppCompatCustomView")
public class FirstCustomView extends View {

    private int mRadius;   //基本元素的半径
//    private int mWidth;   //控件的宽度
//    private int mHeight;   //控件的高度
    private int mTextSize;  //文字大小
    private int mTextColor;  //文字颜色
    private int mBackgroundColor;  //背景颜色
    private int mLineColor;  //线条颜色
    private Paint mTextPaint;  //文字画笔
    private Paint mBackGroundPaint;  //背景画笔
    private Paint mLinePaint;  //线条画笔

    private Context context;
    private int width; //  测量宽度 FreeView的宽度
    private int height; // 测量高度 FreeView的高度
    private int maxWidth; // 最大宽度 window 的宽度
    private int maxHeight; // 最大高度 window 的高度
    private float downX;   //触摸坐标x（离开坐标x）
    private float downY;   //触摸坐标y（离开坐标y）
    private int moveX;    //移动距离x
    private int moveY;    //移动距离y

    private Canvas mTextCanvas;  //文字画布
    private Canvas mBackGroundCanvas;  //背景画布

    //定义同一层元素之间（父亲与母亲）的间隔为元素宽度的一半，保持协调
    int mMarginX;
    //定义相邻两层元素之间（父级与子级）的单向间隔（不是指两个元素之间的间隔）为元素高度的一半，保持协调
    int mMarginY;

    public FirstCustomView(Context context) {
        this(context, null);
    }

    public FirstCustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ResourceAsColor")
    public FirstCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, 0);
        int customCount = a.getIndexCount();
        for (int i = 0; i < customCount; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomView_radius:
                    mRadius = (int) a.getDimension(R.styleable.CustomView_radius, dp2x(50));
                    mMarginX = mRadius;
                    mMarginY = mRadius;
                    break;

                case R.styleable.CustomView_padding:
                    break;

                case R.styleable.CustomView_text:
                    break;

                case R.styleable.CustomView_textSize:
                    mTextSize = a.getDimensionPixelSize(R.styleable.CustomView_textSize, sp2x(14));
                    break;

                case R.styleable.CustomView_textColor:
                    mTextColor = a.getColor(R.styleable.CustomView_textColor, R.color.colorAccent);
                    break;

                case R.styleable.CustomView_backgroundColor:
                    mBackgroundColor = a.getColor(R.styleable.CustomView_backgroundColor, R.color.colorPrimary);
                    break;

                case R.styleable.CustomView_lineColor:
                    mLineColor = a.getColor(R.styleable.CustomView_lineColor, R.color.colorGrey);
                    break;
            }
        }
        a.recycle();

        //文字
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);   //设置抗锯齿效果
        mTextPaint.setTextAlign(Paint.Align.CENTER);  //设置文字对齐方式

        //背景
        mBackGroundPaint = new Paint();
        mBackGroundPaint.setColor(mBackgroundColor);
        mBackGroundPaint.setAntiAlias(true);   //设置抗锯齿效果

        //线条
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setAntiAlias(true);   //设置抗锯齿效果

    }

    /**
     * 自定义view的测量方法
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //布局的宽高都是有这个方法指定
        //指定控件的宽高,需要测量
        //式获取宽高的模
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        maxWidth = Utils.getMaxWidth(context);
//        maxHeight = UiUtil.getMaxHeight(context)-getStatusBarHeight();// 此时减去状态栏高度 注意如果有状态栏 要减去状态栏 如下行 得到的是可活动的高度
        maxHeight = Utils.getMaxHeight(context)-getStatusBarHeight() - getNavigationBarHeight();
        /**
         * MeasureSpec.AT_MOST : 在布局中指定了wrap_content
         * MeasureSpec.EXACTLY : 在不居中指定了确切的值  100dp   match_parent  fill_parent
         * MeasureSpec.UNSPECIFIED : 尽可能的大,很少能用到，ListView , ScrollView 在测量子布局的时候会用UNSPECIFIED
         */
        //1.确定的值，这时候不需要计算的
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        //2.不确定的值，wrap_content 需要计算
        if (widthMode == MeasureSpec.AT_MOST) {
            @SuppressLint("DrawAllocation") Rect bounds = new Rect();
            //计算的宽度 与字体的长度有关 用画笔测量
            width = bounds.width()+getPaddingLeft()+getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            @SuppressLint("DrawAllocation") Rect bounds = new Rect();
            //计算的宽度 与字体的长度有关 用画笔测量
            height = bounds.height()+getPaddingTop()+getPaddingBottom();
        }
        //设置宽高 调用了此方法就必须要注释 super 方法
        setMeasuredDimension(width, height);
    }

    /**
     * 定义点击监听方法
     * 1.定义一个接口对象listener
     * 2.获得接口对象的方法
     * 3.定义一个接口
     */
    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        public void onClick(String name);
    }


    /**
     * 处理事件分发
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (this.isEnabled()) {
            //获取到手指处的横坐标和纵坐标
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // 点击动作处理 每次点击时将拖动状态改为 false 并且记录下点击时的坐标 downX downY
                    downX = event.getX(); // 点击触屏时的x坐标 用于离开屏幕时的x坐标作计算
                    downY = event.getY(); // 点击触屏时的y坐标 用于离开屏幕时的y坐标作计算
                    break;
                case MotionEvent.ACTION_MOVE: // 滑动动作处理 记录离开屏幕时的 moveX  moveY 用于计算距离 和 判断滑动事件和点击事件 并作出响应
                    moveX = (int) (event.getX() - downX);
                    moveY = (int) (event.getY() - downY);

                    /**
                     * 滚动内容
                     * 判断点击和滑动，距离小于 10 为点击事件
                     * scrollTo() ——>让View从最初的地方到指定的地方去，也就是终点固定了的。
                     * scrollBy() ——>让View从现在的地方到指定的地方去，也就是永远没有尽头，终点是下一个起点。
                     */
                    if (Math.abs(moveX) > 10 || Math.abs(moveY) > 10) {
                        scrollBy(-moveX, -moveY);
                        downX = event.getX();
                        downY = event.getY();
                    }
//                    int l,r,t,b; // 上下左右四点移动后的偏移量
//                    //计算偏移量 设置偏移量 = 3 时 为判断点击事件和滑动事件的峰值
//                    l = (int) (getLeft() + moveX);
//                    r = l;
//                    t = (int) (getTop() + moveY);
//                    b = t;
                    //不划出边界判断,最大值为边界值
                    // 如果你的需求是可以划出边界 此时你要计算可以划出边界的偏移量 最大不能超过自身宽度或者是高度  如果超过自身的宽度和高度 view 划出边界后 就无法再拖动到界面内了 注意
//                    if(l<0){ // left 小于 0 就是滑出边界 赋值为 0 ; right 右边的坐标就是自身宽度 如果可以划出边界 left right top bottom 最小值的绝对值 不能大于自身的宽高
//                        l=0;
//                        r=l;
//                    }else if(r> maxWidth){ // 判断 right 并赋值
//                        r= maxWidth;
//                        l=r-100;
//                    }
//                    if(t<0){ // top
//                        t=0;
//                        b=t+100;
//                    }else if(b> maxHeight){ // bottom
//                        b= maxHeight;
//                        t=b-100;
//                    }
//                    Log.i("TAG", "onTouchEvent: " + "l=" + l + " , r=" + r +  " , t=" + t + " , b=" + b );
//                    this.test(l, t, r, b); // 重置view在layout 中位置
                    break;

                case MotionEvent.ACTION_UP: // 不处理
                    /**
                     * 判断点击和滑动，触摸时间小于 0.2s 并且移动距离小于 10 为点击事件
                     */
                    if ((event.getEventTime() - event.getDownTime() < 200) && (Math.abs(moveX) < 10 && Math.abs(moveY) < 10)) {
                        if(listener!=null) {
                            listener.onClick("你好");
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL: // 不处理
                    setPressed(false);
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * 用于绘制
     */
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        //设置画布背景颜色，可以为控件设置背景色
//        canvas.drawColor(R.color.colorPrimary);

        //画圆，圆心中点 x 坐标， y 坐标， 半径， 画笔； 颜色在画笔上设置
//        canvas.drawCircle(mRadius, mRadius, mRadius, mBackGroundPaint);

        //画只有一个孩子的关系图
        onDrawUnit(canvas, 2);

    }

    /**
     * 画一个树状关系图（由多个单元关系组成）
     * 单元关系：
     * 以 父亲 母亲 为父级元素， 孩子（数量可变）为子级元素，为一个单元关系
     * 通过递归方式完成单元关系的重复画
     */
    private void onDrawRelation() {

    }

    /**
     * 画一个单元关系，以一个人为一个基本元素，定义宽高 100x100dp 的圆形状态
     * 父亲 母亲 孩子 连线连接，如果孩子数量为 0 ，则不继续下画
     * @param canvas 画布对象
     * @param childNum 孩子数量
     */
    private void onDrawUnit(Canvas canvas, int childNum) {
        if (childNum > 0) {
            /**
             * 画父亲
             * 参数一：元素对象的中心点的 x 坐标
             * 参数二：元素对象的中心点的 y 坐标
             * 参数三：元素对象的半径大小
             * 参数四：画笔对象
             */
            //记录父亲元素的中心 x 坐标
            int mFatherX = mRadius;
            //记录父亲元素的中心 y 坐标
            int mFatherY = mRadius;

            canvas.drawCircle(mFatherX, mFatherY, mRadius, mBackGroundPaint);
            //画父亲文字， 画布对象，文本内容，开始位置
            onDrawText(canvas, "父亲", mFatherX, mFatherY);

            /**
             * 画母亲
             * 母亲对象一般在父亲对象的右边，两者间隔定义 50dp
             * 此时母亲对象的中心点 x 坐标的值就应该是父亲元素的中心点 x + mMarginX + (mRadius * 2)(母亲元素的宽度)
             */
            //记录母亲元素的中心 x 坐标
            int mMotherX = mFatherX + mMarginX + (mRadius * 2);
            //记录母亲元素的中心 y 坐标
            int mMotherY = mFatherY;

            canvas.drawCircle(mMotherX, mMotherY, mRadius, mBackGroundPaint);
            //画母亲文字， 画布对象，文本内容，开始位置
            onDrawText(canvas, "母亲", mMotherX, mMotherY);

            /**
             * 画父级层连接线
             * 连接父亲元素和母亲元素
             * 父级元素之间的连接线从父亲元素的右侧到母亲元素的左侧作为 x 轴长
             * x 轴长等于 父亲元素的右侧到母亲元素的左侧（（mMotherX - mRadius） - （mFatherX + mRadius））
             * y 轴长不变
             */
            //每条直线占用四个数据，表示起点和终点的 x，y
            float[] pl = {
                    mFatherX + mRadius, mFatherY, mMotherX - mRadius, mFatherY
            };
            canvas.drawLines(pl, mLinePaint);

            /**
             * 画父级与子级之间的连接线
             * 连接父级与子级，分为两种情况
             * 一：只有一个孩子，直接连接，连接轴长 y 等于 mFatherY（父亲元素的中心 y 坐标）+ radius * 2(元素高度) + 20（中间连接线长度）
             * 二：不止一个孩子，先画中间连接线 20，再画每个孩子之间的连接线，同上父级层连接线方式（结构不同）
             */
            if (childNum == 1) {
                /**
                 * 先画连接线，直接连通子元素
                 * 中间连接线的 x 轴是从父级层连接线的中间开始计算
                 */
                float x_s = ((mFatherX) + mMotherX)/2;             //起点 x = 父亲和母亲元素的 x 轴之和的一半
                float y_s = mFatherY;                              //起点 y = mFatherY（父亲元素的中心 y 坐标）
                float x_e = ((mFatherX) + mMotherX)/2;             //起点 x = 父亲和母亲元素的 x 轴之和的一半
                float y_e = y_s + mRadius + mMarginY + mMarginY;   //终点 y = y（起点 y）+ radius（元素高度）+ mMarginY（上间距）+ mMarginY（下间距）
                canvas.drawLine(x_s, y_s, x_e, y_e, mLinePaint);

                /**
                 * 再画孩子元素
                 */
                //记录孩子（下一级的父亲）元素的中心 x 坐标
                int mChildX = (mFatherX + mMotherX)/2;
                //记录孩子（下一级的父亲）元素的中心 y 坐标
                int mChildY = mFatherY + mRadius + mMarginY + mMarginY + mRadius;

                canvas.drawCircle(mChildX, mChildY, mRadius, mBackGroundPaint);
                //画儿子文字， 画布对象，文本内容，开始位置
                onDrawText(canvas, "儿子", mChildX, mChildY);

            } else {
                /**
                 * 先画层与层之间的连接线
                 */
                float x_s = ((mFatherX) + mMotherX)/2;          //起点 x = 父亲和母亲元素的 x 轴之和的一半
                float y_s = mFatherY;                           //起点 y = mFatherY（父亲元素的中心 y 坐标）
                float x_e = ((mFatherX) + mMotherX)/2;          //起点 x = 父亲和母亲元素的 x 轴之和的一半
                float y_e = y_s + mRadius + mMarginY;           //终点 y = y（起点 y）+ radius（元素高度）+ mMarginY（间距）
                canvas.drawLine(x_s, y_s, x_e, y_e, mLinePaint);

                //记录第一个子元素的坐标
                float firstChildX = mFatherX;
                float firstChildY = mFatherY + mRadius + mMarginY + mMarginY + mRadius;
                for (int i = 0; i < childNum; i++) {

                    /**
                     * 画子元素层连接线连接线
                     * 分两步画，先画子元素自己向上的链接先，在画两个子元素之间的连接线
                     */
                    float c_x_s = firstChildX + ((mMotherX - mFatherX) * i * 2);
                    float c_y_s = firstChildY;
                    float c_x_e = firstChildX + ((mMotherX - mFatherX) * i * 2);
                    float c_y_e = firstChildY -  mRadius - mMarginY;
                    canvas.drawLine(c_x_s, c_y_s, c_x_e, c_y_e, mLinePaint);

                    /**
                     * 画孩子元素，两个孩子之间存在配偶元素，所以实际间距是 2 倍
                     */
                    //记录孩子（下一级的父亲）元素的中心 x 坐标
                    int mChildX = mFatherX + ((mMotherX - mFatherX) * i * 2);
                    //记录孩子（下一级的父亲）元素的中心 y 坐标
                    int mChildY = (int) firstChildY;

                    canvas.drawCircle(mChildX, mChildY, mRadius, mBackGroundPaint);
                    //画儿子文字， 画布对象，文本内容，开始位置
                    onDrawText(canvas, "儿子" + i, mChildX, mChildY);
                }
                //记录最后一个子元素的坐标
                float lastChildX = mFatherX + ((mMotherX - mFatherX) * (childNum - 1) * 2);
                float lastChildY = firstChildY;
                /**
                 * 最后画子元素之间的连接线
                 */
                canvas.drawLine(firstChildX, firstChildY -  mRadius - mMarginY, lastChildX, lastChildY -  mRadius - mMarginY, mLinePaint);
            }
        }
    }

    /**
     * 画文字内容
     * @param canvas 画布对象
     * @param text   文字内容
     * @param x      起始 x 位置int
     * @param y      起始 y 位置int
     */
    private void onDrawText(Canvas canvas, String text, int x, int y) {
        //画文字
        //x是开始的位置
        //y是基线
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        //底部到基线的位置
        int dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        //中心线
        /**
         * 让文字在竖直方向居中显示
         */
        int baseLine = y + dy;
        //画文字， 文本内容， 开始位置， 基线位置， 画笔
        canvas.drawText(text, x, baseLine, mTextPaint);
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public int getStatusBarHeight(){
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取导航栏高度
     * @return
     */
    public int getNavigationBarHeight() {
        int rid = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid!=0){
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;
    }

    /**
     * sp
     * @param sp
     * @return
     */
    public int sp2x(int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * dip
     * @param dp
     * @return
     */
    public float dp2x(int dp) {
        return (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
