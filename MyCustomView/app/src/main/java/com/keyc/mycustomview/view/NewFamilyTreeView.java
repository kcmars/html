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
import com.keyc.mycustomview.bean.FamilyMember;
import com.keyc.mycustomview.bean.Person;
import com.keyc.mycustomview.bean.ViewBean;
import com.keyc.mycustomview.listener.OnFamilySelectListener;
import com.keyc.mycustomview.utils.DisplayUtil;
import com.keyc.mycustomview.utils.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keyC on 2019/6/17.
 * 新的族谱树
 */

public class NewFamilyTreeView extends ViewGroup {

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
    private ViewBean mViews; //统计所有成员的视图

    /**
     * 统计当前家族层数（代数）
     */
    private int mLayerNumber = 0;
    private int mNextNumber = 0;
    private int[] widthDP = new int[10000];//下一代总成员所占的总宽度 最大一万代
    private int[][] nextWidth = new int[10000][5];//下一代总成员所占的总宽度 最大一万代，每一代最大一千个子女


    private Paint mPaint;//连线样式
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

    public NewFamilyTreeView(Context context) {
        this(context, null, 0);
    }

    public NewFamilyTreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewFamilyTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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

    /**
     * 设置数据源
     * @param familyMember
     */
    public void setFamilyMember(FamilyMember familyMember) {
        this.mFamilyMember = familyMember;
        removeAllViews();
        initWidthAndHeight();
        initView();
        invalidate();
    }

    /**
     * 初始化每一代的宽度
     */
    private void initWidthAndHeight() {

        /**
         * 统计所有的成员
         * 第一代可以确定，要么一人要么两人
         * 从第二代开始统计
         */
        if (mFamilyMember != null) {
            mLayerNumber += 1;
            /**
             * 有配偶
             * 第一代
             */
            if (mFamilyMember.getSpouse() != null) {
                widthDP[mLayerNumber] = ITEM_WIDTH_DP + SPACE_WIDTH_DP + ITEM_WIDTH_DP * 2;
                int[] childW = {ITEM_WIDTH_DP + SPACE_WIDTH_DP + ITEM_WIDTH_DP * 2};
                nextWidth[mLayerNumber] = childW;
            }
            /**
             * 有孩子
             * 第一代
             */
            if (mFamilyMember.getChildren() != null && mFamilyMember.getChildren().size() > 0) {
                mLayerNumber += 1;
                List<FamilyMember> children = mFamilyMember.getChildren();

                /**
                 * 当代所有孩子的宽度
                 * 第二代
                 */
                widthDP[mLayerNumber] = (SPACE_WIDTH_DP + ITEM_WIDTH_DP) * children.size();

                /**
                 * 统计第二代每个成员的所有孩子的宽度
                 */
                int[] childW = new int[children.size()];
                nextWidth[mLayerNumber] = childW;

                /**
                 * 统计下一代（第三代开始）的成员数量（不包含配偶）
                 */
                int nowNumber = 0;

                /**
                 * 第二代成员数量
                 */
                for (int i = 0; i < children.size(); i++) {
                    nextWidth[mLayerNumber][i] = (SPACE_WIDTH_DP + ITEM_WIDTH_DP);
                    /**
                     * 如果孩子存在配偶，则当代（第二代）所有孩子的宽度要加上配偶的宽度
                     */
                    if (children.get(i).getSpouse() != null) {
                        widthDP[mLayerNumber] += (SPACE_WIDTH_DP + ITEM_WIDTH_DP);
                        nextWidth[mLayerNumber][i] += (SPACE_WIDTH_DP + ITEM_WIDTH_DP);
                    }
                    /**
                     * 继续循环下一代（第三代）
                     * 当第三代开始，每一当代的人员数量统计宽度会影响到上一代的统计宽度
                     * 所以统计完当代宽度后，需要反向向上一代在统计一次宽度（在其基础上加上新增人员个数的宽度）
                     * 所以需要统计当代所有元素和上一代所有元素数量比较，超过上一代数量就需要在上一代的基础上增加多余的宽度
                     */
                    statisticsWidth(children.get(i), mLayerNumber, widthDP, children.size(), nowNumber);
                }
            }
        }

        /**
         * 将视图宽度设置为统计最宽宽度
         */
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
     * 统计当代宽度（第三代开始递归此方法）
     * @param familyMember 数据源
     * @param mLayerNumber 上一代层数
     * @param widthDP      统计宽度
     * @param number       上一代的成员数量（不包含配偶）
     * @param nowNumber    当代的成员数量（不包含配偶）
     */
    private void statisticsWidth(FamilyMember familyMember, int mLayerNumber, int[] widthDP, int number, int nowNumber) {
        if (familyMember.getChildren() != null && familyMember.getChildren().size() > 0) {
            mLayerNumber += 1;
            List<FamilyMember> children = familyMember.getChildren();
            nowNumber += children.size();
            widthDP[mLayerNumber] += (SPACE_WIDTH_DP + ITEM_WIDTH_DP) * children.size();
            /**
             * 统计第二代以后每个成员的所有孩子的宽度
             */
            int[] childW = new int[children.size()];
            nextWidth[mLayerNumber] = childW;

            /**
             * 处理上一层的宽度（所有上一层，循环向上统计）
             */
            for (int i = 1; i < mLayerNumber; i++) {
                widthDP[mLayerNumber-i] += (SPACE_WIDTH_DP + ITEM_WIDTH_DP) * children.size();
            }

            for (int i = 0; i < familyMember.getChildren().size(); i++) {
                nextWidth[mLayerNumber][i] = (SPACE_WIDTH_DP + ITEM_WIDTH_DP);
                /**
                 * 如果孩子存在配偶，则当代所有孩子的宽度要加上配偶的宽度
                 */
                if (children.get(i).getSpouse() != null) {
                    widthDP[mLayerNumber] += (SPACE_WIDTH_DP + ITEM_WIDTH_DP);
                    nextWidth[mLayerNumber][i] += (SPACE_WIDTH_DP + ITEM_WIDTH_DP);
                }
                /**
                 * 继续循环下一代
                 * 当第三代开始，每一当代的人员数量统计宽度会影响到上一代的统计宽度
                 * 所以统计完当代宽度后，需要反向向上一代在统计一次宽度（在其基础上加上新增人员个数的宽度）
                 * 所以需要统计当代所有元素和上一代所有元素数量比较，超过上一代数量就需要在上一代的基础上增加多余的宽度
                 */
                statisticsWidth(children.get(i), mLayerNumber, widthDP, familyMember.getChildren().size(), nowNumber);
            }
        }
    }

    /**
     * 初始化成员视图
     */
    private void initView() {
        if (mFamilyMember != null) {
            mViews = new ViewBean();
            /**
             * 添加自己的视图
             */
            View mMineView = createFamilyView(mFamilyMember.getSelf());
            mViews.setMineView(mMineView);

            /**
             * 添加配偶的视图
             */
            if (mFamilyMember.getSpouse() != null) {
                View mSpouseView = createFamilyView(mFamilyMember.getSpouse());
                mViews.setSpouseView(mSpouseView);
            }

            /**
             * 添加孩子的视图
             */
            if (mFamilyMember.getChildren() != null && mFamilyMember.getChildren().size() > 0) {
                /**
                 * 统计当代孩子的视图
                 */
                List<ViewBean> views = new ArrayList<>();
                List<FamilyMember> children = mFamilyMember.getChildren();

                for (int i = 0; i < children.size(); i++) {

                    statisticsViews(views, children.get(i));
                }

                mViews.setChildrenView(views);
            }
        }
    }

    /**
     * 统计当代所有成员的视图（第二代开始递归此方法统计）
     * @param views
     * @param familyMember
     */
    private void statisticsViews(List<ViewBean> views, FamilyMember familyMember) {
        if (familyMember != null) {
            ViewBean viewBean = new ViewBean();
            /**
             * 添加自己的视图
             */
            if (familyMember.getSelf() != null) {
                View selfView = createFamilyView(familyMember.getSelf());
                viewBean.setMineView(selfView);
            }

            /**
             * 添加配偶的视图
             */
            if (familyMember.getSpouse() != null) {
                View spouseView = createFamilyView(familyMember.getSpouse());
                viewBean.setSpouseView(spouseView);
            }

            /**
             * 添加孩子的视图
             */
            if (familyMember.getChildren() != null && familyMember.getChildren().size() > 0) {
                /**
                 * 统计当代孩子的视图
                 */
                List<ViewBean> views1 = new ArrayList<>();
                List<FamilyMember> children = familyMember.getChildren();

                for (int i = 0; i < children.size(); i++) {
                    statisticsViews(views1, children.get(i));
                }
                viewBean.setChildrenView(views1);
            }
            views.add(viewBean);
        }
    }

    @SuppressLint("SetTextI18n")
    private View createFamilyView(Person person) {
        final View personView = LayoutInflater.from(getContext()).inflate(R.layout.item_family, this, false);
        personView.getLayoutParams().width = mItemWidthPX;
        personView.getLayoutParams().height = mItemHeightPX;
        personView.setTag(person);

        final ImageView ivAvatar = (ImageView) personView.findViewById(R.id.iv_avatar);
        ivAvatar.getLayoutParams().height = mItemWidthPX;

        final TextView tvCall = (TextView) personView.findViewById(R.id.tv_call);
        tvCall.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvCall.setTextSize(CALL_TEXT_SIZE_SP);
        tvCall.setText(person.getCall());

        final TextView tvName = (TextView) personView.findViewById(R.id.tv_name);
        tvName.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvName.setTextSize(NAME_TEXT_SIZE_SP);
        tvName.setText(person.getName());

        Glide.with(getContext())
                .load(R.drawable.shape_red_circle)
                .placeholder(R.drawable.family_avatar)
                .error(R.drawable.family_avatar)
                .centerCrop()
                .transform(new GlideCircleTransform(getContext()))
                .dontAnimate()
                .into(ivAvatar);

        personView.setOnClickListener(click);

        this.addView(personView);
        return personView;
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
            mCurrentScrollX = (left + right - mShowWidthPX) / 2;
            mCurrentScrollY = (top + bottom - mShowHeightPX) / 2;
        }
        scrollTo(mCurrentScrollX, mCurrentScrollY);
        if (mViews != null) {
            mNextNumber += 1;
            View mMineView = mViews.getMineView();
            if (mMineView != null) {
                int mineLeft;
                int mineTop;
                if (mCurrentLeft == 0 && mCurrentTop == 0) {
                    mineLeft = (left + right - mItemWidthPX) / 2;
                    mineTop = (top + bottom - mItemHeightPX) / 2;
                } else {
                    mineLeft = mCurrentLeft;
                    mineTop = mCurrentTop;
                }

                /**
                 * 设置 MineView 的位置
                 */
                setViewFrame(mMineView, mineLeft, mineTop, mItemWidthPX, mItemHeightPX);

                /**
                 * 设置 SpouseView 的位置
                 */
                View spouseView = null;
                if (mViews.getSpouseView() != null) {
                    spouseView = mViews.getSpouseView();
                    setViewFrame(spouseView, mineLeft + mItemWidthPX + mSpacePX, mineTop, mItemWidthPX, mItemHeightPX);
                }

                /**
                 * 设置 ChildrenView 的位置
                 */
                if (mViews.getChildrenView() != null && mViews.getChildrenView().size() > 0) {
                    List<ViewBean> childViews = mViews.getChildrenView();
                    setChildrenView(mMineView, spouseView, childViews, mFamilyMember, mineTop, mineLeft);
                }
            }
        }
    }

    /**
     * 设置孩子 view 的位置（递归此方法，设置第二代开始每个成员的位置）
     * 因为下一代的数量会影响到上一代的间距，所以当下一代成员位置确定后，需要重新确定上一代成员的位置，反向递归重置至第二代
     * @param mineTop   左边距离
     * @param mineLeft  上边距离
     */
    private void setChildrenView(View mineView, View spouseView, List<ViewBean> childViews, FamilyMember familyMember, int mineTop, int mineLeft) {
        mNextNumber += 1;
        Log.i("TAG", "setChildrenView: mineLeft=" + mineLeft);
        /**
         * 这一代成员的上边距等于 上一代的的上边距 加上 成员高度 加上 两个基本间距（上下之间因为有横向线条指向兄弟，所以为两个基本间距）
         */
        int childTop = mineTop + mItemHeightPX + mSpacePX * 2;
        int allW = 0;
//        for (int i = 0; i < nextWidth[mNextNumber].length; i++) {
//            allW += nextWidth[mNextNumber][i];
//        }
        /**
         * 这一代成员的左边距等于 上一代的左边距 加上 这一代的前面的所有成员占据的宽度（指在你在前面的兄弟的宽度之和） 加上 半个成员宽度
         */
        int childLeft = mineLeft;
        int parentLeft = mineLeft;

        for (int i = 0; i < childViews.size(); i++) {
            final View mineView1 = childViews.get(i).getMineView();
            final FamilyMember familyMember1 = familyMember.getChildren().get(i);
            final List<FamilyMember> children = familyMember1.getChildren();

//            int childLeft = mineLeft;

            /**
             * 当前view的左边距 等于 前面当代view的前面的view的累加
             */
//            childLeft += (mItemWidthPX + mSpacePX);
            if (i > 0) {
                parentLeft += mItemWidthPX + mSpacePX;
            }
            /**
             * 设置 ChildrenView 的位置
             */
            setViewFrame(mineView1, childLeft, childTop, mItemWidthPX, mItemHeightPX);

            /**
             * 设置 ChildrenView 配偶的位置
             */
            View spouseView1 = null;
            if (childViews.get(i).getSpouseView() != null) {
                spouseView1 = childViews.get(i).getSpouseView();
                final int spouseLeft = childLeft + (mItemWidthPX + mSpacePX);

                setViewFrame(spouseView1, spouseLeft, childTop, mItemWidthPX, mItemHeightPX);
                /**
                 * 如果孩子数量大于一个，则加上孩子的宽度，反向循环设置上一代的位置
                 */
                if (i > 0) {
                    parentLeft += mItemWidthPX + mSpacePX;
                }
                /**
                 * 循环设置下一代的成员的位置
                 */
                if (children != null && children.size() > 0) {
                    List<ViewBean> childrenView = childViews.get(i).getChildrenView();

                    setThirdView(mineView1, spouseView1, childrenView, familyMember1, childTop, childLeft, parentLeft, i);
                }

                /**
                 * 前面view如果存在配偶view的话，需要加上配偶view的占得宽度
                 */
//                childLeft = Math.max(childLeft, spouseLeft + mSpacePX + mItemWidthPX);
                childLeft += ((mItemWidthPX + mSpacePX) + (mItemWidthPX + mSpacePX));
            } else {
                /**
                 * 循环设置下一代的成员的位置
                 */
                /**
                 * 如果孩子数量大于一个，则加上孩子的宽度，反向循环设置上一代的位置
                 */
                if (i > 0) {
                    parentLeft += mItemWidthPX + mSpacePX;
                }
                if (children != null && children.size() > 0) {
                    List<ViewBean> childrenView = childViews.get(i).getChildrenView();

                    setThirdView(mineView1, spouseView1, childrenView, familyMember1, childTop, childLeft, parentLeft, i);
                }
                childLeft += (mItemWidthPX + mSpacePX);
            }

//            setViewFrame(mineView, parentLeft, mineTop, mItemWidthPX, mItemHeightPX);
//            setViewFrame(spouseView, parentLeft + (mItemWidthPX + mItemWidthPX/2), mineTop, mItemWidthPX, mItemHeightPX);
//            for (int n = mNextNumber-1; n > 0; n--) {
//                setViewFrame(mineView, parentLeft, mineTop, mItemWidthPX, mItemHeightPX);
//            }
        }
    }

    /**
     * 第三代位置
     */
    private void setThirdView(View mineView, View spouseView, List<ViewBean> childViews,
                              FamilyMember familyMember, int mineTop, int mineLeft, int parentLeft, int n) {
        mNextNumber += 1;
        /**
         * 这一代成员的上边距等于 上一代的的上边距 加上 成员高度 加上 两个基本间距（上下之间因为有横向线条指向兄弟，所以为两个基本间距）
         */
        int childTop = mineTop + mItemHeightPX + mSpacePX * 2;
        /**
         * 这一代成员的左边距等于 上一代的左边距 加上 这一代的前面的所有成员占据的宽度（指在你在前面的兄弟的宽度之和） 加上 半个成员宽度
         */
        int childLeft = mineLeft;
        int parentLeft1 = parentLeft;

        Log.i("TAG", "setThirdView: " + parentLeft);

        for (int i = 0; i < childViews.size(); i++) {
            final View mineView1 = childViews.get(i).getMineView();

            /**
             * 设置 ChildrenView 的位置
             */
            setViewFrame(mineView1, childLeft, childTop, mItemWidthPX, mItemHeightPX);

            /**
             * 设置 ChildrenView 配偶的位置
             */
            View spouseView1 = null;
            if (childViews.get(i).getSpouseView() != null) {
                spouseView1 = childViews.get(i).getSpouseView();
                final int spouseLeft = childLeft + mSpacePX + mItemWidthPX;

                setViewFrame(spouseView1, spouseLeft, childTop, mItemWidthPX, mItemHeightPX);
                /**
                 * 如果孩子数量大于一个，则加上孩子的宽度，反向循环设置上一代的位置
                 */
                if (i > 0) {
                    parentLeft1 += (mItemWidthPX + mSpacePX);
                }
                /**
                 * 前面view如果存在配偶view的话，需要加上配偶view的占得宽度
                 */
                childLeft += ((mItemWidthPX + mSpacePX) + (mItemWidthPX + mSpacePX));
            } else {
                /**
                 * 如果孩子数量大于一个，则加上孩子的宽度，反向循环设置上一代的位置
                 */
                if (i > 0) {
                    parentLeft1 += (mItemWidthPX + mSpacePX);
                }
                /**
                 * 当前view的左边距 等于 前面当代view的前面的view的累加
                 */
                childLeft += (mItemWidthPX + mSpacePX);
            }

            if (mineView != null && n > 0) {
                Log.i("TAG", "setViewFrame: left=" + parentLeft1);
                Log.i("TAG", "setThirdView: mineView=" + mineView);
                setViewFrame(mineView, parentLeft1, mineTop, mItemWidthPX, mItemHeightPX);
            }
            if (spouseView != null && n > 0) {
                setViewFrame(spouseView, parentLeft1 + (mItemWidthPX + mSpacePX), mineTop, mItemWidthPX, mItemHeightPX);
            }
        }
    }

    /**
     * 设置 view 的位置
     * @param childView
     * @param left
     * @param top
     * @param width
     * @param height
     */
    private void setViewFrame(View childView, int left, int top, int width, int height) {
        if (childView != null) {
            childView.layout(left, top, left + width, top + height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSpouseLine(canvas);
        drawChildrenLine(canvas);
    }

    /**
     * 画配偶连线
     * @param canvas
     */
    private void drawSpouseLine(Canvas canvas) {
        if (mViews != null) {
            if (mViews.getSpouseView() != null) {
                View mineView = mViews.getMineView();
                View spouseView = mViews.getSpouseView();
                final int horizontalLineStartX = (int) mineView.getX() + mItemWidthPX / 2;
                final int horizontalLineStopX = (int) spouseView.getX() + mItemWidthPX / 2;
                final int horizontalLineY = (int) spouseView.getY() + mItemWidthPX / 2;
                mPath.reset();
                mPath.moveTo(horizontalLineStartX, horizontalLineY);
                mPath.lineTo(horizontalLineStopX, horizontalLineY);
                canvas.drawPath(mPath, mPaint);

                /**
                 * 画下一代成员和配偶之间的连线
                 */
                if (mViews.getChildrenView() != null && mViews.getChildrenView().size() > 0) {
                    List<ViewBean> childView = mViews.getChildrenView();
                    for (int i = 0; i < childView.size(); i++) {
                        drawAllSpouseLine(canvas, childView.get(i));
                    }
                }
            }
        }
    }

    /**
     * 循环画所有成员与配偶之间的连线
     */
    private void drawAllSpouseLine(Canvas canvas, ViewBean viewBean) {
        if (viewBean != null) {
            if (viewBean.getSpouseView() != null) {
                View mineView = viewBean.getMineView();
                View spouseView = viewBean.getSpouseView();

                final int horizontalLineStartX = (int) mineView.getX() + mItemWidthPX / 2;
                final int horizontalLineStopX = (int) spouseView.getX() + mItemWidthPX / 2;
                final int horizontalLineY = (int) spouseView.getY() + mItemWidthPX / 2;
                mPath.reset();
                mPath.moveTo(horizontalLineStartX, horizontalLineY);
                mPath.lineTo(horizontalLineStopX, horizontalLineY);
                canvas.drawPath(mPath, mPaint);
            }

            /**
             * 画下一代成员和配偶之间的连线
             */
            if (viewBean.getChildrenView() != null && viewBean.getChildrenView().size() > 0) {
                List<ViewBean> childView = viewBean.getChildrenView();
                for (int i = 0; i < childView.size(); i++) {
                    drawAllSpouseLine(canvas, childView.get(i));
                }
            }
        }
    }

    /**
     * 画上下代连线
     * @param canvas
     */
    private void drawChildrenLine(Canvas canvas) {
        if (mViews != null) {
            if (mViews.getChildrenView() != null && mViews.getChildrenView().size() > 0) {
                drawAllChildrenLine(canvas, mViews);
            }
        }
    }

    /**
     * 循环画所有成员与配偶之间的连线
     */
    private void drawAllChildrenLine(Canvas canvas, ViewBean viewBean) {
        if (viewBean != null) {
            if (viewBean.getChildrenView() != null && viewBean.getChildrenView().size() > 0) {
                /**
                 * 画上一代到下一代的竖直方向的连线
                 */
                View mineView = viewBean.getMineView();
                float myVerticalLineX = mineView.getX() + mItemWidthPX / 2;
                float myVerticalLineStartY = mineView.getY() + mItemHeightPX;
                float myVerticalLinesStopY = myVerticalLineStartY + mSpacePX;
                mPath.reset();
                mPath.moveTo(myVerticalLineX, myVerticalLineStartY);
                mPath.lineTo(myVerticalLineX, myVerticalLinesStopY);
                canvas.drawPath(mPath, mPaint);

                /**
                 * 循环此画法
                 */
                List<ViewBean> childView = viewBean.getChildrenView();
                for (int i = 0; i < childView.size(); i++) {
                    View startChildView = childView.get(i).getMineView();
                    float childLineY = startChildView.getY() - mSpacePX;
                    float childVerticalLineEndY = startChildView.getY() + mItemWidthPX / 2;
                    float childLineStartX = startChildView.getX() + mItemWidthPX / 2;
                    mPath.reset();
                    mPath.moveTo(childLineStartX, childLineY);
                    mPath.lineTo(childLineStartX, childVerticalLineEndY);
                    canvas.drawPath(mPath, mPaint);

                    if (childView.get(i).getSpouseView() != null) {
                        View childSpouseView = childView.get(i).getSpouseView();
                        float spouseLineEndX = (int) childSpouseView.getX() + mItemWidthPX / 2;
                        mPath.reset();
                        mPath.moveTo(childLineStartX, childVerticalLineEndY);
                        mPath.lineTo(spouseLineEndX, childVerticalLineEndY);
                        canvas.drawPath(mPath, mPaint);
                    }

                    if (i < childView.size() - 1) {
                        View endChildView = childView.get(i + 1).getMineView();
                        float horizontalLineStopX = endChildView.getX() + mItemWidthPX / 2;
                        mPath.reset();
                        mPath.moveTo(childLineStartX, childLineY);
                        mPath.lineTo(horizontalLineStopX, childLineY);
                        canvas.drawPath(mPath, mPaint);
                    }

                    /**
                     * 循环此画法
                     */
                    if (childView.get(i).getChildrenView() != null && childView.get(i).getChildrenView().size() > 0) {
                        drawAllChildrenLine(canvas, childView.get(i));
                    }
                }
            }
        }
    }


    public void doEnlarge() {
        if (mCurrentScale < 1.5f) {
            mCurrentScale += 0.1f;
            setScaleX(mCurrentScale);
            setScaleY(mCurrentScale);
        }
    }

    public void doShrinkDown() {
        if (mCurrentScale > 1f) {
            mCurrentScale -= 0.1f;
            setScaleX(mCurrentScale);
            setScaleY(mCurrentScale);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mCurrentX = getScrollX();
//                mCurrentY = getScrollY();
//                mLastTouchX = (int) event.getX();
//                mLastTouchY = (int) event.getY();
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

                this.scrollTo(mCurrentX, mCurrentY);
                mLastTouchX = currentTouchX;
                mLastTouchY = currentTouchY;
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercerpt = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastInterceptX = (int) event.getX();
                mLastInterceptY = (int) event.getY();
                mCurrentX = getScrollX();
                mCurrentY = getScrollY();
                mLastTouchX = (int) event.getX();
                mLastTouchY = (int) event.getY();
                intercerpt = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final int distanceX = Math.abs((int) event.getX() - mLastInterceptX);
                final int distanceY = Math.abs((int) event.getY() - mLastInterceptY);
                if (distanceX < mScrollWidth && distanceY < mScrollWidth) {
                    intercerpt = false;
                } else {
                    intercerpt = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercerpt = false;
                break;
        }
        return intercerpt;
    }
}
