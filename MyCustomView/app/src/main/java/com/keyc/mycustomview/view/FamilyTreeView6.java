package com.keyc.mycustomview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.FamilyMember;
import com.keyc.mycustomview.bean.FamilyTree;
import com.keyc.mycustomview.bean.Person;
import com.keyc.mycustomview.listener.OnFamilySelectListener;
import com.keyc.mycustomview.utils.DisplayUtil;
import com.keyc.mycustomview.utils.GlideCircleTransform;

import static com.keyc.mycustomview.utils.DisplayUtil.getScreenHeight;
import static com.keyc.mycustomview.utils.DisplayUtil.getScreenWidth;

public class FamilyTreeView6 extends LinearLayout {

    private FamilyTree mFamilyTree;
    private Paint mPaint;

    private static final int MAX_HEIGHT_DP = 590;//最大高度为590dp
    private static final int SPACE_WIDTH_DP = 20;//间距为20dp
    private static final int ITEM_WIDTH_DP = 50;//家庭成员View宽度50dp
    private static final int ITEM_HEIGHT_DP = 80;//家庭成员View高度80dp
    private static final float CALL_TEXT_SIZE_SP = 9f;//称呼文字大小9sp
    private static final float NAME_TEXT_SIZE_SP = 11f;//名称文字大小11sp
    private static final int LINE_WIDTH_DP = 2;//连线宽度2dp
    private static final int SCROLL_WIDTH = 2;//移动超过2dp，响应滑动，否则属于点击

    private OnFamilySelectListener mOnFamilySelectListener;

    private float mCurrentScale = 1f;//当前缩放比例

    private int mScreenWidth;//屏幕宽度PX
    private int mScreenHeight;//屏幕高度PX

    private int mItemWidthPX;//家庭成员View宽度PX
    private int mItemHeightPX;//家庭成员View高度PX
    private int mMaxWidthPX;//最大宽度PX
    private int mMaxHeightPX;//最大高度PX
    private int mSpacePX;//元素间距PX
    private int mLineWidthPX;//连线宽度PX

    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;

    private int mShowWidthPX;//在屏幕所占的宽度
    private int mShowHeightPX;//在屏幕所占的高度

    private FamilyMember mFamilyMember;  //数据源

    private Path mPath;//路径

    private int mScrollWidth;//移动范围
    private int mCurrentX;//当前X轴偏移量
    private int mCurrentY;//当前Y轴偏移量
    private int mLastTouchX;//最后一次触摸的X坐标
    private int mLastTouchY;//最后一次触摸的Y坐标
    private int mLastInterceptX;
    private int mLastInterceptY;

    private int mCurrentLeft = 0;//当前选中View的Left距离
    private int mCurrentTop = 0;//当前选中View的Top距离
    private int mCurrentScrollX = 0;//当前滚动位置
    private int mCurrentScrollY = 0;//当前滚动位置


    public FamilyTreeView6(@NonNull Context context) {
        this(context, null);
    }

    public FamilyTreeView6(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FamilyTreeView6(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth = getScreenWidth();
        mScreenHeight = getScreenHeight();
        mScrollWidth = DisplayUtil.dip2px(SCROLL_WIDTH);
        mSpacePX = DisplayUtil.dip2px(SPACE_WIDTH_DP);
        mLineWidthPX = DisplayUtil.dip2px(LINE_WIDTH_DP);
        mItemWidthPX = DisplayUtil.dip2px(ITEM_WIDTH_DP);
        mItemHeightPX = DisplayUtil.dip2px(ITEM_HEIGHT_DP);
        mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mItemWidthPX, MeasureSpec.EXACTLY);
        mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeightPX, MeasureSpec.EXACTLY);
        Log.i("TAG", "FamilyTreeView6: " + mItemWidthPX);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2.f);
        mPaint.setColor(Color.parseColor("#999999"));
        mPaint.setAntiAlias(true);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFamilyTree != null) {
            this.draw(mFamilyTree, 0.f, mPaint, canvas);
        }
    }

    public FamilyTree getFamilyTree() {
        return mFamilyTree;
    }

    public void setFamilyTree(FamilyTree mFamilyTree, int width, int height) {
        this.mFamilyTree = mFamilyTree;
//        create(mFamilyTree, 0f);
        invalidate();
    }

    /**
     * 创建成员对象视图
     * @param rootTree
     * @return
     */
    @SuppressLint("SetTextI18n")
    private View createFamilyView(FamilyTree rootTree, float left, float top) {
        Log.i("TAG", "createFamilyView: " + mItemWidthPX);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.leftMargin = left;
//        params.topMargin = top;

        final View personView = LayoutInflater.from(getContext()).inflate(R.layout.item_family, this, false);
        personView.getLayoutParams().width = mItemWidthPX;
        personView.getLayoutParams().height = mItemHeightPX;
        personView.setX(left);
        personView.setY(top);
        personView.setTag(rootTree);
//        personView.setLayoutParams(params);
        final ImageView ivAvatar = (ImageView) personView.findViewById(R.id.iv_avatar);
        ivAvatar.getLayoutParams().height = mItemWidthPX;

        final TextView tvCall = (TextView) personView.findViewById(R.id.tv_call);
        tvCall.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvCall.setTextSize(CALL_TEXT_SIZE_SP);
        tvCall.setText(rootTree.getTree().getName());

        final TextView tvName = (TextView) personView.findViewById(R.id.tv_name);
        tvName.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvName.setTextSize(NAME_TEXT_SIZE_SP);
        tvName.setText(rootTree.getTree().getName());

        Glide.with(getContext())
                .load(R.mipmap.iv_front_view)
                .placeholder(R.mipmap.iv_front_view)
                .error(R.mipmap.iv_front_view)
                .centerCrop()
                .transform(new GlideCircleTransform(getContext()))
                .dontAnimate()
                .into(ivAvatar);

        personView.setOnClickListener(click);

        this.addView(personView);
        return personView;
    }

    private void create(FamilyTree rootTree, float depth) {
        float left = (float) (rootTree.getX() * 30 * 1.5 + 115f);
        float top = (float) (depth * 30f * 1.5 + 115f);
        View view = createFamilyView(rootTree, left, top);
        for (FamilyTree childTree : rootTree.getChildren()) {
            create(childTree, depth + 1);
        }
    }

    private void draw(FamilyTree rootTree, float depth, Paint paint, Canvas canvas) {
        canvas.drawCircle((float) (rootTree.getX() * 30 * 1.5 + 115f), (float) (depth * 30f * 1.5 + 115f), 25.f, paint);
        canvas.drawText(rootTree.getTree().getName(),(float) (rootTree.getX() * 30 * 1.5 + 115f), (float) (depth * 30f * 1.5 + 115f), paint);
        for (FamilyTree childTree : rootTree.getChildren()) {
            canvas.drawLine((float) (rootTree.getX() * 30 * 1.5 + 115f), (float) (depth * 30f * 1.5 + 115f), (float) (childTree.getX() * 30 * 1.5 + 115f), (float) ((depth + 1) * 30f * 1.5 + 115f), paint);
            this.draw(childTree, depth + 1, paint, canvas);
        }
    }

    public void setOnFamilySelectListener(OnFamilySelectListener onFamilySelectListener) {
        this.mOnFamilySelectListener = onFamilySelectListener;
    }

    private OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnFamilySelectListener != null) {
                mCurrentLeft = v.getLeft();
                mCurrentTop = v.getTop();
                mCurrentScrollX = getScrollX();
                mCurrentScrollY = getScrollY();
                mOnFamilySelectListener.onFamilySelect((Person) v.getTag());
            }
        }
    };


    public Bitmap createBitmap3(View v, int width, int height) {
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bmp;
    }

}
