package com.keyc.mycustomview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.KinMember;
import com.keyc.mycustomview.listener.OnKinMemberViewsClickListener;
import com.keyc.mycustomview.listener.OnKinViewsClickListener;
import com.keyc.mycustomview.utils.DisplayUtil;
import com.keyc.mycustomview.utils.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keyC on 2019/6/5.
 * 自定义族谱树2
 */

public class FamilyTreeCustomView extends ViewGroup {

    private static final int MAX_HEIGHT_DP = 590;//最大高度为590dp
    private static final int SPACE_WIDTH_DP = 20;//间距为20dp
    private static final int ITEM_WIDTH_DP = 50;//家庭成员View宽度50dp
    private static final int ITEM_HEIGHT_DP = 80;//家庭成员View高度80dp
    private static final float CALL_TEXT_SIZE_SP = 9f;//称呼文字大小9sp
    private static final float NAME_TEXT_SIZE_SP = 11f;//名称文字大小11sp
    private static final int LINE_WIDTH_DP = 2;//连线宽度2dp
    private static final int SCROLL_WIDTH = 2;//移动超过2dp，响应滑动，否则属于点击

    private float mCurrentScale = 1f;//当前缩放比例
    private float mMaxScale = 1.5f;//最大缩放比例
    private float mMinScale = 0.5f;//最小缩放比例

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

    private KinMember mMyPerson;//我的
    private KinMember mMySpouse;//配偶
    private KinMember mMyFather;//父亲
    private KinMember mMyMother;//母亲
    private List<KinMember> mMyBrothers;//兄弟姐妹
    private List<KinMember> mMyChildren;//子女
    private KinMember mMyFosterFather;//养父
    private KinMember mMyFosterMother;//养母

    private View mMineView;//我的View
    private View mSpouseView;//配偶View
    private View mFatherView;//父亲View
    private View mMotherView;//母亲View
    private List<View> mBrothersView;//兄弟姐妹View
    private View mPaternalGrandFatherView;//爷爷View
    private View mPaternalGrandMotherView;//奶奶View
    private View mMaternalGrandFatherView;//外公View
    private View mMaternalGrandMotherView;//外婆View
    private List<View> mChildrenView;//子女View
    private List<View> mChildSpouseView;//子女配偶View
    private List<View> mGrandChildrenView;//孙子女View

    private int mGrandChildrenMaxWidth;//孙子女所占总长度

    private Paint mPaint;//连线样式
    private Path mPath;//路径

    private int mScrollWidth;//移动范围
    private int mCurrentX;//当前X轴偏移量
    private int mCurrentY;//当前Y轴偏移量
    private int mLastTouchX;//最后一次触摸的X坐标
    private int mLastTouchY;//最后一次触摸的Y坐标
    private int mLastInterceptX;
    private int mLastInterceptY;

    public int mCurrentLeft = 0;//当前选中View的Left距离
    public int mCurrentTop = 0;//当前选中View的Top距离
    public int mCurrentScrollX = 0;//当前滚动位置
    public int mCurrentScrollY = 0;//当前滚动位置

    //点击监听事件
    private OnKinMemberViewsClickListener listener;
    //数据源
    private KinMember kinMember;


    public FamilyTreeCustomView(Context context) {
        this(context, null, 0);
    }

    public FamilyTreeCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FamilyTreeCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 解决被 addView 后不执行 onDraw 方法
         */
        setWillNotDraw(false);

        mScreenWidth = DisplayUtil.getScreenWidth();
        mScreenHeight = DisplayUtil.getScreenHeight();
        mScrollWidth = DisplayUtil.dip2px(SCROLL_WIDTH);
        mSpacePX = DisplayUtil.dip2px(SPACE_WIDTH_DP);
        mLineWidthPX = DisplayUtil.dip2px(LINE_WIDTH_DP);
        mItemWidthPX = DisplayUtil.dip2px(ITEM_WIDTH_DP);
        mItemHeightPX = DisplayUtil.dip2px(ITEM_HEIGHT_DP);
        mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mItemWidthPX, MeasureSpec.EXACTLY);
        mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeightPX, MeasureSpec.EXACTLY);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.reset();
        mPaint.setColor(0xFF888888);
        mPaint.setStrokeWidth(mLineWidthPX);
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setPathEffect(new DashPathEffect(new float[]{mLineWidthPX, mLineWidthPX * 4}, 0));

        mPath = new Path();
        mPath.reset();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mShowWidthPX = MeasureSpec.getSize(widthMeasureSpec);
        mShowHeightPX = MeasureSpec.getSize(heightMeasureSpec);

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            childView.measure(mWidthMeasureSpec, mHeightMeasureSpec);
        }

        setMeasuredDimension(mMaxWidthPX, mMaxHeightPX);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mCurrentScrollX == 0 && mCurrentScrollY == 0) {
//            mCurrentScrollX = (left + right - mShowWidthPX) / 2;
            mCurrentScrollX = (left + right - mScreenWidth) / 2;
//            mCurrentScrollY = (top + bottom - mShowHeightPX) / 2;
            mCurrentScrollY = (top + bottom - mScreenHeight) / 2;
            Log.i("TAG", "onLayout: left=" + left);
            Log.i("TAG", "onLayout: top=" + top);
            Log.i("TAG", "onLayout: right=" + right);
            Log.i("TAG", "onLayout: bottom=" + bottom);
            Log.i("TAG", "onLayout: x1=" + mScreenWidth);
            Log.i("TAG", "onLayout: x2=" + mCurrentScrollX);
        }
        Log.i("TAG", "onLayout: x3=" + mCurrentScrollX);
//        scrollTo(mCurrentScrollX, mCurrentScrollY);
        if (mMineView != null) {
            final int mineLeft;
            final int mineTop;
            if (mCurrentLeft == 0 && mCurrentTop == 0) {
                mineLeft = (left + right - mItemWidthPX) / 2;
                mineTop = (top + bottom - mItemHeightPX) / 2;
            } else {
                mineLeft = mCurrentLeft;
                mineTop = mCurrentTop;
            }

            setChildViewFrame(mMineView, mineLeft, mineTop, mItemWidthPX, mItemHeightPX);

            if (mSpouseView != null) {
                setChildViewFrame(mSpouseView, mineLeft + mItemWidthPX + mSpacePX, mineTop, mItemWidthPX, mItemHeightPX);
            }

            final int parentTop = mineTop - mSpacePX * 2 - mItemHeightPX;
            final int grandParentTop = parentTop - mSpacePX * 2 - mItemHeightPX;

            int fatherLeft = mineLeft;
            int motherLeft = mineLeft;

            if (haveBothParent()) {
                fatherLeft -= mItemWidthPX + mSpacePX;
                motherLeft += mItemWidthPX + mSpacePX;
            }

            if (mFatherView != null) {
                setChildViewFrame(mFatherView, fatherLeft, parentTop, mItemWidthPX, mItemHeightPX);

                setGrandParentFrame(mPaternalGrandFatherView, mPaternalGrandMotherView, fatherLeft, grandParentTop);
            }
            if (mMotherView != null) {
                setChildViewFrame(mMotherView, motherLeft, parentTop, mItemWidthPX, mItemHeightPX);

                setGrandParentFrame(mMaternalGrandFatherView, mMaternalGrandMotherView, motherLeft, grandParentTop);
            }

            if (mBrothersView != null && mBrothersView.size() > 0) {
                final int brotherCount = mBrothersView.size();
                for (int i = 0; i < brotherCount; i++) {
                    final View brotherView = mBrothersView.get(i);
                    setChildViewFrame(brotherView, mineLeft - (i + 1) * (mItemWidthPX + mSpacePX), mineTop, mItemWidthPX, mItemHeightPX);
                }
            }

            if (mChildrenView != null && mChildrenView.size() > 0) {
                final int childTop = mineTop + mItemHeightPX + mSpacePX * 2;
                int childLeft = mineLeft + mItemWidthPX / 2 - mGrandChildrenMaxWidth / 2;

                final int grandChildrenTop = childTop + mItemHeightPX + mSpacePX * 2;
                int grandChildrenLeft = childLeft;

                int grandchildIndex = 0;
                int childSpouseIndex = 0;
                final int childCount = mChildrenView.size();
                for (int i = 0; i < childCount; i++) {
                    final View myChildView = mChildrenView.get(i);
                    final KinMember myChild = mMyChildren.get(i);
                    final KinMember myChildSpouse = myChild.getSpouse();
                    final List<KinMember> myGrandChildren = myChild.getChildren();

                    if (myGrandChildren != null && myGrandChildren.size() > 0) {
                        final int startGrandChildLeft = grandChildrenLeft;
                        int endGrandChildLeft = grandChildrenLeft;

                        final int myGrandChildrenCount = myGrandChildren.size();
                        for (int j = 0; j < myGrandChildrenCount; j++) {
                            final View grandChildView = mGrandChildrenView.get(grandchildIndex);
                            setChildViewFrame(grandChildView, grandChildrenLeft, grandChildrenTop, mItemWidthPX, mItemHeightPX);
                            endGrandChildLeft = grandChildrenLeft;
                            grandChildrenLeft += mItemWidthPX + mSpacePX;
                            grandchildIndex++;
                        }

                        childLeft = (endGrandChildLeft - startGrandChildLeft) / 2 + startGrandChildLeft;
                    } else {
                        childLeft = grandChildrenLeft;
                        grandChildrenLeft += mSpacePX + mItemWidthPX;
                    }

                    setChildViewFrame(myChildView, childLeft, childTop, mItemWidthPX, mItemHeightPX);

                    if (myChildSpouse != null) {
                        final View spouseView = mChildSpouseView.get(childSpouseIndex);
                        final int spouseLeft = childLeft + mSpacePX + mItemWidthPX;

                        setChildViewFrame(spouseView, spouseLeft, childTop, mItemWidthPX, mItemHeightPX);
                        childSpouseIndex++;

                        grandChildrenLeft = Math.max(grandChildrenLeft, spouseLeft + mSpacePX + mItemWidthPX);
                    }
                }
            }
        }
    }

    /**
     * 滚动到指定位置
     * @param l
     * @param t
     */
    public void scrollToPosition(int l, int t) {
        scrollTo(l, t);
        mCurrentX = l;
        mCurrentY = t;
        Log.i("TAG", "onLayout: l=" + l);
        Log.i("TAG", "onLayout: t=" + t);
    }

    /**
     * 设置祖父母view的位置
     * @param grandFatherView 祖父
     * @param grandMotherView 祖母
     * @param defaultLeft 左边距
     * @param defaultTop 上边距
     */
    private void setGrandParentFrame(View grandFatherView, View grandMotherView, int defaultLeft, int defaultTop) {
        int grandFatherLeft = defaultLeft;
        int grandMotherLeft = defaultLeft;
        if (grandFatherView != null && grandMotherView != null) {
            grandFatherLeft -= mItemWidthPX * 2 / 3;
            grandMotherLeft += mItemWidthPX * 2 / 3;
        }

        if (grandFatherView != null) {
            setChildViewFrame(grandFatherView, grandFatherLeft, defaultTop, mItemWidthPX, mItemHeightPX);
        }

        if (grandMotherView != null) {
            setChildViewFrame(grandMotherView, grandMotherLeft, defaultTop, mItemWidthPX, mItemHeightPX);
        }
    }

    /**
     * 设置孩子view的位置
     * @param childView view
     * @param left  左边距
     * @param top   上边距
     * @param width  宽度
     * @param height  高度
     */
    private void setChildViewFrame(View childView, int left, int top, int width, int height) {
        childView.layout(left, top, left + width, top + height);
    }

    /**
     * 设置数据源
     * @param kinMember 族人
     */
    public void setData(KinMember kinMember) {
        recycleAllView();
        initData(kinMember);
        initWidthAndHeight();
        initView(kinMember);
        invalidate();
    }

    /**
     * 放大
     */
    public void doEnlarge() {
        if (mCurrentScale < mMaxScale) {
            mCurrentScale += 0.01f;
            setScaleX(mCurrentScale);
            setScaleY(mCurrentScale);
        }
    }

    /**
     * 缩小
     */
    public void doShrinkDown() {
        if (mCurrentScale > mMinScale) {
            mCurrentScale -= 0.01f;
            setScaleX(mCurrentScale);
            setScaleY(mCurrentScale);
        }
    }

    /**
     * 初始化数据
     * @param kinMember
     */
    private void initData(KinMember kinMember) {
        this.kinMember = kinMember;
        kinMember.setSelect(true);
        mMySpouse = kinMember.getSpouse();
        mMyFather = kinMember.getFather();
        mMyMother = kinMember.getMother();
        mMyBrothers = kinMember.getBrothers();
        mMyChildren = kinMember.getChildren();
        mMyFosterFather = kinMember.getFosterFather();
        mMyFosterMother = kinMember.getFosterMother();
    }

    private void initWidthAndHeight() {
        final int[] widthDP = {
                390,//第一代最大宽度
                280,//第二代最大宽度
                ITEM_WIDTH_DP,//第三代最大宽度
                ITEM_WIDTH_DP,//第四代最大宽度
                ITEM_WIDTH_DP//第五代最大宽度
        };

        if (mMySpouse != null) {
            widthDP[2] = ITEM_WIDTH_DP + SPACE_WIDTH_DP + ITEM_WIDTH_DP * 2;
        }
        if (mMyBrothers != null && mMyBrothers.size() > 1) {
            widthDP[2] = ITEM_WIDTH_DP + (SPACE_WIDTH_DP + ITEM_WIDTH_DP) * mMyBrothers.size() * 2;
        }

        if (mMyChildren != null) {
            widthDP[3] += (SPACE_WIDTH_DP + ITEM_WIDTH_DP) * mMyChildren.size();
            widthDP[4] = 0;
            for (int i = 0; i < mMyChildren.size(); i++) {
                final KinMember child = mMyChildren.get(i);
                final List<KinMember> grandChildrenList = child.getChildren();

                final int grandchildMaxWidthDP;
                if (grandChildrenList != null && grandChildrenList.size() > 0) {
                    final int grandchildCount = grandChildrenList.size();
                    if (grandchildCount == 1 && mMyChildren.size() == 1) {
                        grandchildMaxWidthDP = ITEM_WIDTH_DP + SPACE_WIDTH_DP;
                    } else if (grandchildCount == 2 && child.getSpouse() != null) {
                        grandchildMaxWidthDP = (ITEM_WIDTH_DP + SPACE_WIDTH_DP) * 5 / 2;
                    } else {
                        grandchildMaxWidthDP = (ITEM_WIDTH_DP + SPACE_WIDTH_DP) * grandchildCount;
                    }
                } else {
                    if (mMyChildren.size() > 1) {
                        if (child.getSpouse() != null) {
                            grandchildMaxWidthDP = (ITEM_WIDTH_DP + SPACE_WIDTH_DP) * 2;
                        } else {
                            grandchildMaxWidthDP = ITEM_WIDTH_DP + SPACE_WIDTH_DP;
                        }
                    } else {
                        grandchildMaxWidthDP = ITEM_WIDTH_DP + SPACE_WIDTH_DP;
                    }
                }
                widthDP[4] += grandchildMaxWidthDP;
            }
            widthDP[4] -= SPACE_WIDTH_DP;
            mGrandChildrenMaxWidth = DisplayUtil.dip2px(widthDP[4]);
        }

        mMaxWidthPX = mScreenWidth;
        for (int width : widthDP) {
            final int widthPX = DisplayUtil.dip2px(width);
            if (widthPX > mMaxWidthPX) {
                mMaxWidthPX = widthPX;
            }
        }

        mMaxHeightPX = Math.max(DisplayUtil.dip2px(MAX_HEIGHT_DP), mScreenHeight);
    }

    /**
     * 初始化人员信息
     */
    private void initView(KinMember kinMember) {
        mMineView = createFamilyView(kinMember);
        if (mMySpouse != null) {
            mSpouseView = createFamilyView(mMySpouse);
        }

        if (mMyFather != null) {
            mFatherView = createFamilyView(mMyFather);
            final KinMember myPaternalGrandFather = mMyFather.getFather();
            final KinMember myPaternalGrandMother = mMyFather.getMother();
            if (myPaternalGrandFather != null) {
                mPaternalGrandFatherView = createFamilyView(myPaternalGrandFather);
            }
            if (myPaternalGrandMother != null) {
                mPaternalGrandMotherView = createFamilyView(myPaternalGrandMother);
            }

        }
        if (mMyMother != null) {
            mMotherView = createFamilyView(mMyMother);
            final KinMember myMaternalGrandFather = mMyMother.getFather();
            final KinMember myMaternalGrandMother = mMyMother.getMother();

            if (myMaternalGrandFather != null) {
                mMaternalGrandFatherView = createFamilyView(myMaternalGrandFather);
            }
            if (myMaternalGrandMother != null) {
                mMaternalGrandMotherView = createFamilyView(myMaternalGrandMother);
            }
        }

        mBrothersView.clear();
        if (mMyBrothers != null) {
            for (KinMember family : mMyBrothers) {
                mBrothersView.add(createFamilyView(family));
            }
        }

        mChildrenView.clear();
        if (mMyChildren != null) {
            for (KinMember family : mMyChildren) {
                mChildrenView.add(createFamilyView(family));
                final KinMember childSpouse = family.getSpouse();
                if (childSpouse != null) {
                    mChildSpouseView.add(createFamilyView(childSpouse));
                }

                final List<KinMember> grandChildrens = family.getChildren();

                if (grandChildrens != null && grandChildrens.size() > 0) {
                    for (KinMember childFamily : grandChildrens) {
                        mGrandChildrenView.add(createFamilyView(childFamily));
                    }
                }
            }
        }
    }

    /**
     * 创建人员对象
     * @param kinMember 人员信息
     * @return
     */
    @SuppressLint("SetTextI18n")
    private View createFamilyView(final KinMember kinMember) {
        final View familyView = LayoutInflater.from(getContext()).inflate(R.layout.item_family, this, false);
        familyView.getLayoutParams().width = mItemWidthPX;
        familyView.getLayoutParams().height = mItemHeightPX;
        familyView.setTag(kinMember);

        final ImageView ivAvatar = (ImageView) familyView.findViewById(R.id.iv_avatar);
        ivAvatar.getLayoutParams().height = mItemWidthPX;

        final TextView tvCall = (TextView) familyView.findViewById(R.id.tv_call);
        tvCall.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvCall.setTextSize(CALL_TEXT_SIZE_SP);
        tvCall.setText(kinMember.getCall());

        final TextView tvName = (TextView) familyView.findViewById(R.id.tv_name);
        tvName.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvName.setTextSize(NAME_TEXT_SIZE_SP);
        tvName.setText(kinMember.getName() + "(" + kinMember.getSex() + ")");

        final String url = kinMember.getPortrait();
        if (!TextUtils.isEmpty(url)) {
            Glide.with(getContext())
                    .load(url)
                    .placeholder(R.drawable.family_avatar)
                    .error(R.drawable.family_avatar)
                    .centerCrop()
                    .transform(new GlideCircleTransform(getContext()))
                    .dontAnimate()
                    .into(ivAvatar);
        } else {
            ivAvatar.setBackgroundResource(R.drawable.shape_red_circle);
        }
        if (kinMember.isSelect()) {
            ivAvatar.setBackgroundResource(R.drawable.shape_red_circle);
        }

        familyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onViewsClick(kinMember);
                }
            }
        });

        this.addView(familyView);
        return familyView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSpouseLine(canvas);  //画配偶的连线
        drawParentLine(canvas);  //画父母的连线
        drawBrothersLine(canvas);  //画兄弟姐妹的连线
        drawChildrenLine(canvas);  //画孩子的连线
        Log.i("TAG", "onDraw: 11111");
    }

    /**
     * 画配偶的连线
     * @param canvas
     */
    private void drawSpouseLine(Canvas canvas) {
        if (mSpouseView != null) {
            final int horizontalLineStartX = (int) mMineView.getX() + mItemWidthPX / 2;
            final int horizontalLineStopX = (int) mSpouseView.getX() + mItemWidthPX / 2;
            final int horizontalLineY = (int) mSpouseView.getY() + mItemWidthPX / 2;
            mPath.reset();
            mPath.moveTo(horizontalLineStartX, horizontalLineY);
            mPath.lineTo(horizontalLineStopX, horizontalLineY);
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * 画父母的连线
     * @param canvas
     */
    private void drawParentLine(Canvas canvas) {
        final int mineX = (int) mMineView.getX();
        final int mineY = (int) mMineView.getY();

        /**
         * 单亲
         */
        if (haveEitherParent()) {
            final int verticalLineX = mineX + mItemWidthPX / 2;
            final int verticalLineStartY = mineY + mItemWidthPX / 2;
            int verticalLineEndY = verticalLineStartY;

            if (mFatherView != null) {
                verticalLineEndY = (int) mFatherView.getY() + mItemWidthPX / 2;
            } else if (mMotherView != null) {
                verticalLineEndY = (int) mMotherView.getY() + mItemWidthPX / 2;
            }
            mPath.reset();
            mPath.moveTo(verticalLineX, verticalLineStartY);
            mPath.lineTo(verticalLineX, verticalLineEndY);
            canvas.drawPath(mPath, mPaint);
        }

        /**
         * 双亲
         */
        if (haveBothParent()) {
            final int lineStartX = (int) mFatherView.getX() + mItemWidthPX / 2;
            final int lineEndX = (int) mMotherView.getX() + mItemWidthPX / 2;
            final int lineY = (int) mFatherView.getY() + mItemWidthPX / 2;
            mPath.reset();
            mPath.moveTo(lineStartX, lineY);
            mPath.lineTo(lineEndX, lineY);
            canvas.drawPath(mPath, mPaint);
        }

        /**
         * 祖父母
         */
        if (mPaternalGrandFatherView != null || mPaternalGrandMotherView != null) {
            drawGrandParentLine(canvas, mFatherView, mPaternalGrandFatherView, mPaternalGrandMotherView);
        }
        /**
         * 外祖父母
         */
        if (mMaternalGrandFatherView != null || mMaternalGrandMotherView != null) {
            drawGrandParentLine(canvas, mMotherView, mMaternalGrandFatherView, mMaternalGrandMotherView);
        }
    }

    /**
     * 画祖父母的连线
     * @param canvas
     * @param parentView 父母
     * @param grandFatherView 祖父
     * @param grandMotherView 祖母
     */
    private void drawGrandParentLine(Canvas canvas, View parentView, View grandFatherView, View grandMotherView) {
        final int verticalLineX = (int) parentView.getX() + mItemWidthPX / 2;
        final int verticalLineStartY = (int) parentView.getY() + mItemWidthPX / 2;
        int verticalLineEndY = verticalLineStartY;
        if (grandFatherView != null) {
            verticalLineEndY = (int) grandFatherView.getY() + mItemWidthPX / 2;
        } else if (grandMotherView != null) {
            verticalLineEndY = (int) grandMotherView.getY() + mItemWidthPX / 2;
        }

        mPath.reset();
        mPath.moveTo(verticalLineX, verticalLineStartY);
        mPath.lineTo(verticalLineX, verticalLineEndY);
        canvas.drawPath(mPath, mPaint);

        if (grandFatherView != null && grandMotherView != null) {
            final int horizontalLineStartX = (int) grandFatherView.getX() + mItemWidthPX / 2;
            final int horizontalLineEndX = (int) grandMotherView.getX() + mItemWidthPX / 2;
            mPath.reset();
            mPath.moveTo(horizontalLineStartX, verticalLineEndY);
            mPath.lineTo(horizontalLineEndX, verticalLineEndY);
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * 画兄弟姐妹的连线
     * @param canvas
     */
    private void drawBrothersLine(Canvas canvas) {
        if (mBrothersView != null && mBrothersView.size() > 0) {
            final int brotherCount = mBrothersView.size();
            final View brotherView = mBrothersView.get(brotherCount - 1);

            final int horizontalLineStartX = (int) brotherView.getX() + mItemWidthPX / 2;
            final int horizontalLineEndX = (int) mMineView.getX() + mItemWidthPX / 2;
            final int horizontalLineY = (int) brotherView.getY() - mSpacePX;
            mPath.reset();
            mPath.moveTo(horizontalLineStartX, horizontalLineY);
            mPath.lineTo(horizontalLineEndX, horizontalLineY);
            canvas.drawPath(mPath, mPaint);

            for (View bView : mBrothersView) {
                final int vLineStartY = (int) bView.getY() + mSpacePX;
                final int hLineX = (int) bView.getX() + mItemWidthPX / 2;
                mPath.reset();
                mPath.moveTo(hLineX, vLineStartY);
                mPath.lineTo(hLineX, horizontalLineY);
                canvas.drawPath(mPath, mPaint);
            }
        }
    }

    /**
     * 画孩子的连线
     * @param canvas
     */
    private void drawChildrenLine(Canvas canvas) {
        if (mMyChildren != null && mMyChildren.size() > 0) {
            final int myVerticalLineX = (int) mMineView.getX() + mItemWidthPX / 2;
            final int myVerticalLineStartY = (int) mMineView.getY() + mItemHeightPX;
            final int myVerticalLinesStopY = myVerticalLineStartY + mSpacePX;
            mPath.reset();
            mPath.moveTo(myVerticalLineX, myVerticalLineStartY);
            mPath.lineTo(myVerticalLineX, myVerticalLinesStopY);
            canvas.drawPath(mPath, mPaint);

            int index = 0;
            int childSpouseIndex = 0;
            final int childrenViewCount = mChildrenView.size();
            for (int i = 0; i < childrenViewCount; i++) {
                final View startChildView = mChildrenView.get(i);
                final int childLineY = (int) startChildView.getY() - mSpacePX;
                final int childVerticalLineEndY = (int) startChildView.getY() + mItemWidthPX / 2;
                final int childLineStartX = (int) startChildView.getX() + mItemWidthPX / 2;
                mPath.reset();
                mPath.moveTo(childLineStartX, childLineY);
                mPath.lineTo(childLineStartX, childVerticalLineEndY);
                canvas.drawPath(mPath, mPaint);

                final KinMember childSpouse = mMyChildren.get(i).getSpouse();
                if (childSpouse != null) {
                    final View childSpouseView = mChildSpouseView.get(childSpouseIndex);
                    final int spouseLineEndX = (int) childSpouseView.getX() + mItemWidthPX / 2;
                    mPath.reset();
                    mPath.moveTo(childLineStartX, childVerticalLineEndY);
                    mPath.lineTo(spouseLineEndX, childVerticalLineEndY);
                    canvas.drawPath(mPath, mPaint);
                    childSpouseIndex++;
                }

                if (i < childrenViewCount - 1) {
                    final View endChildView = mChildrenView.get(i + 1);
                    final int horizontalLineStopX = (int) endChildView.getX() + mItemWidthPX / 2;
                    mPath.reset();
                    mPath.moveTo(childLineStartX, childLineY);
                    mPath.lineTo(horizontalLineStopX, childLineY);
                    canvas.drawPath(mPath, mPaint);
                }

                final List<KinMember> grandChildren = mMyChildren.get(i).getChildren();
                if (grandChildren != null) {
                    final int grandChildrenCount = grandChildren.size();
                    for (int j = 0; j < grandChildrenCount; j++) {
                        final View startView = mGrandChildrenView.get(j + index);
                        final int grandchildLineX = (int) startView.getX() + mItemWidthPX / 2;
                        final int grandchildLineStartY = (int) startView.getY() - mSpacePX;
                        final int garndchildLineEndY = (int) startView.getY();
                        mPath.reset();
                        mPath.moveTo(grandchildLineX, grandchildLineStartY);
                        mPath.lineTo(grandchildLineX, garndchildLineEndY);
                        canvas.drawPath(mPath, mPaint);

                        if (j < grandChildrenCount - 1) {
                            final View endView = mGrandChildrenView.get(j + 1 + index);
                            final int hLineStopX = (int) endView.getX() + mItemWidthPX / 2;
                            mPath.reset();
                            mPath.moveTo(grandchildLineX, grandchildLineStartY);
                            mPath.lineTo(hLineStopX, grandchildLineStartY);
                            canvas.drawPath(mPath, mPaint);
                        }
                    }

                    if (grandChildrenCount > 0) {
                        final View grandChildView = mGrandChildrenView.get(index);
                        final int vLineX = (int) startChildView.getX() + mItemWidthPX / 2;
                        final int vLineStopY = (int) startChildView.getY() + mItemHeightPX;
                        final int hLineY = (int) grandChildView.getY() - mSpacePX;
                        mPath.reset();
                        mPath.moveTo(vLineX, hLineY);
                        mPath.lineTo(vLineX, vLineStopY);
                        canvas.drawPath(mPath, mPaint);

                        index += grandChildrenCount;
                    }
                }
            }
        }
    }

    /**
     * 单亲
     * @return
     */
    private boolean haveEitherParent() {
        return mFatherView != null || mMotherView != null;
    }

    /**
     * 双亲
     * @return
     */
    private boolean haveBothParent() {
        return mFatherView != null && mMotherView != null;
    }

    /**
     * 清除所有数据
     */
    private void recycleAllView() {
        removeAllViews();
        mMineView = null;
        mSpouseView = null;
        mFatherView = null;
        mMotherView = null;
        mPaternalGrandFatherView = null;
        mPaternalGrandMotherView = null;
        mMaternalGrandFatherView = null;
        mMaternalGrandMotherView = null;

        if (mBrothersView != null) {
            mBrothersView.clear();
        } else {
            mBrothersView = new ArrayList<>();
        }
        if (mChildrenView != null) {
            mChildrenView.clear();
        } else {
            mChildrenView = new ArrayList<>();
        }
        if (mChildSpouseView != null) {
            mChildSpouseView.clear();
        } else {
            mChildSpouseView = new ArrayList<>();
        }
        if (mGrandChildrenView != null) {
            mGrandChildrenView.clear();
        } else {
            mGrandChildrenView = new ArrayList<>();
        }

        mMySpouse = null;
        mMyFather = null;
        mMyMother = null;
        if (mMyBrothers != null) {
            mMyBrothers.clear();
            mMyBrothers = null;
        }
        if (mMyChildren != null) {
            mMyChildren.clear();
            mMyChildren = null;
        }
    }

    /**
     * 设置点击监听
     */
    public void setOnViewsClick(OnKinMemberViewsClickListener listener) {
        this.listener = listener;
    }


}
