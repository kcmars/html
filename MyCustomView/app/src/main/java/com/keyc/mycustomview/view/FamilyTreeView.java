package com.keyc.mycustomview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.Kin;
import com.keyc.mycustomview.bean.KinPoint;
import com.keyc.mycustomview.bean.Person;
import com.keyc.mycustomview.bean.Point;
import com.keyc.mycustomview.listener.OnKinViewsClickListener;
import com.keyc.mycustomview.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;
//
///**
// * Created by keyC on 2019/6/3.
// * 自定义族谱树形图
// */
//
//public class FamilyTreeView extends ViewGroup {
//
//    private static final int MAX_HEIGHT_DP = 590;//最大高度为590dp
//    private static final int SPACE_WIDTH_DP = 20;//间距为20dp
//    private static final int ITEM_WIDTH_DP = 50;//家庭成员View宽度50dp
//    private static final int ITEM_HEIGHT_DP = 80;//家庭成员View高度80dp
//    private static final float CALL_TEXT_SIZE_SP = 9f;//称呼文字大小9sp
//    private static final float NAME_TEXT_SIZE_SP = 11f;//名称文字大小11sp
//    private static final int LINE_WIDTH_DP = 2;//连线宽度2dp
//    private static final int SCROLL_WIDTH = 2;//移动超过2dp，响应滑动，否则属于点击
//
//    private float mCurrentScale = 1f;//当前缩放比例
//
//    private int mScreenWidth;//屏幕宽度PX
//    private int mScreenHeight;//屏幕高度PX
//
//    private int mItemWidthPX;//家庭成员View宽度PX
//    private int mItemHeightPX;//家庭成员View高度PX
//    private int mMaxWidthPX;//最大宽度PX
//    private int mMaxHeightPX;//最大高度PX
//    private int mSpacePX;//元素间距PX
//    private int mLineWidthPX;//连线宽度PX
//
//    private int mWidthMeasureSpec;
//    private int mHeightMeasureSpec;
//
//    private int mShowWidthPX;//在屏幕所占的宽度
//    private int mShowHeightPX;//在屏幕所占的高度
//
//    private View mMineView;//我的View
//    private View mSpouseView;//配偶View
//    private View mFatherView;//父亲View
//    private View mMotherView;//母亲View
//    private List<View> mBrothersView;//兄弟姐妹View
//    private View mPaternalGrandFatherView;//爷爷View
//    private View mPaternalGrandMotherView;//奶奶View
//    private View mMaternalGrandFatherView;//外公View
//    private View mMaternalGrandMotherView;//外婆View
//    private List<View> mChildrenView;//子女View
//    private List<View> mChildSpouseView;//子女配偶View
//    private List<View> mGrandChildrenView;//孙子女View
//
//    private int mGrandChildrenMaxWidth;//孙子女所占总长度
//
//    private Paint mPaint;//连线样式
//    private Path mPath;//路径
//
//    private int mScrollWidth;//移动范围
//    private int mCurrentX;//当前X轴偏移量
//    private int mCurrentY;//当前Y轴偏移量
//    private int mLastTouchX;//最后一次触摸的X坐标
//    private int mLastTouchY;//最后一次触摸的Y坐标
//    private int mLastInterceptX;
//    private int mLastInterceptY;
//
//    private int mCurrentLeft = 0;//当前选中View的Left距离
//    private int mCurrentTop = 0;//当前选中View的Top距离
//    private int mCurrentScrollX = 0;//当前滚动位置
//    private int mCurrentScrollY = 0;//当前滚动位置
//
//    private Kin kin;  //数据源
//    private LinearLayout.LayoutParams layoutParams;
//
//    public FamilyTreeView(Context context) {
//        this(context, null, 0);
//    }
//
//    public FamilyTreeView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public FamilyTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        mScreenWidth = DisplayUtil.getScreenWidth();
//        mScreenHeight = DisplayUtil.getScreenHeight();
//        mScrollWidth = DisplayUtil.dip2px(SCROLL_WIDTH);
//        mSpacePX = DisplayUtil.dip2px(SPACE_WIDTH_DP);
//        mLineWidthPX = DisplayUtil.dip2px(LINE_WIDTH_DP);
//        mItemWidthPX = DisplayUtil.dip2px(ITEM_WIDTH_DP);
//        mItemHeightPX = DisplayUtil.dip2px(ITEM_HEIGHT_DP);
//        mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mItemWidthPX, MeasureSpec.EXACTLY);
//        mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeightPX, MeasureSpec.EXACTLY);
//
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.reset();
//        mPaint.setColor(0xFF888888);
//        mPaint.setStrokeWidth(mLineWidthPX);
//        mPaint.setStyle(Paint.Style.STROKE);
////        mPaint.setPathEffect(new DashPathEffect(new float[]{mLineWidthPX, mLineWidthPX * 4}, 0));
//
//        mPath = new Path();
//        mPath.reset();
//    }
//
//    /**
//     * 添加数据
//     */
//    public void setData(Kin kin) {
//        this.kin = kin;
//        initWidthAndHeight();
//        /**
//         * 画线
//         */
//        /**
//         * 添加人员
//         */
//        mMineView = createFamilyView(kin);
//
//        invalidate();
//    }
//
//    /**
//     * 计算宽高
//     */
//    private void initWidthAndHeight() {
//        final int[] widthDP = {
//                390,//第一代最大宽度
//                280,//第二代最大宽度
//                ITEM_WIDTH_DP,//第三代最大宽度
//                ITEM_WIDTH_DP,//第四代最大宽度
//                ITEM_WIDTH_DP//第五代最大宽度
//        };
//        mMaxWidthPX = mScreenWidth;
//        for (int width : widthDP) {
//            final int widthPX = DisplayUtil.dip2px(width);
//            if (widthPX > mMaxWidthPX) {
//                mMaxWidthPX = widthPX;
//            }
//        }
//        mMaxHeightPX = Math.max(DisplayUtil.dip2px(MAX_HEIGHT_DP), mScreenHeight);
//    }
//
//    /**
//     * 创建添加人员对象
//     * @param kin
//     * @return
//     */
//    @SuppressLint({"CutPasteId", "SetTextI18n"})
//    private View createFamilyView(Kin kin) {
//        final View familyView = LayoutInflater.from(getContext()).inflate(R.test.item_family, this, false);
//        familyView.getLayoutParams().width = mItemWidthPX;
//        familyView.getLayoutParams().height = mItemHeightPX;
//        familyView.setTag(kin.getSelf());
//
//        final ImageView ivAvatar = (ImageView) familyView.findViewById(R.id.iv_avatar);
//        ivAvatar.getLayoutParams().height = mItemWidthPX;
//
//        final TextView tvCall = (TextView) familyView.findViewById(R.id.tv_call);
//        tvCall.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
//        tvCall.setTextSize(CALL_TEXT_SIZE_SP);
//        tvCall.setText("13333333333");
//
//        final TextView tvName = (TextView) familyView.findViewById(R.id.tv_name);
//        tvName.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
//        tvName.setTextSize(NAME_TEXT_SIZE_SP);
//        tvName.setText("姓名");
//
////        final String url = family.getMemberImg();
////        if (!TextUtils.isEmpty(url)) {
////            Glide.with(getContext())
////                    .load(url)
////                    .placeholder(R.drawable.family_avatar)
////                    .error(R.drawable.family_avatar)
////                    .centerCrop()
////                    .transform(new GlideCircleTransform(getContext()))
////                    .dontAnimate()
////                    .into(ivAvatar);
////        }
////        if (family.isSelect()) {
//            ivAvatar.setBackgroundResource(R.drawable.shape_red_circle);
////        }
//
////        familyView.setOnClickListener(click);
//
//        this.addView(familyView);
//        return familyView;
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        mShowWidthPX = MeasureSpec.getSize(widthMeasureSpec);
//        mShowHeightPX = MeasureSpec.getSize(heightMeasureSpec);
//
//        final int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View childView = getChildAt(i);
//            childView.measure(mWidthMeasureSpec, mHeightMeasureSpec);
//        }
//
//        setMeasuredDimension(mMaxWidthPX, mMaxHeightPX);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
////        super.onDraw(canvas);
//
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        if (mCurrentScrollX == 0 && mCurrentScrollY == 0) {
//            mCurrentScrollX = (left + right - mShowWidthPX) / 2;
//            mCurrentScrollY = (top + bottom - mShowHeightPX) / 2;
//        }
//        scrollTo(mCurrentScrollX, mCurrentScrollY);
//        if (mMineView != null) {
//            final int mineLeft;
//            final int mineTop;
//            if (mCurrentLeft == 0 && mCurrentTop == 0) {
//                mineLeft = (left + right - mItemWidthPX) / 2;
//                mineTop = (top + bottom - mItemHeightPX) / 2;
//            } else {
//                mineLeft = mCurrentLeft;
//                mineTop = mCurrentTop;
//            }
//            setChildViewFrame(mMineView, mineLeft, mineTop, mItemWidthPX, mItemHeightPX);
//        }
////        if (kin != null) {
////            mMineView = createFamilyView(kin);
////            setChildViewFrame(mMineView, 10, 10, mItemWidthPX, mItemHeightPX);
////        }
//    }
//
//    /**
//     * 设置view的显示参数
//     * @param childView
//     * @param left
//     * @param top
//     * @param width
//     * @param height
//     */
//    private void setChildViewFrame(View childView, int left, int top, int width, int height) {
//        childView.test(left, top, left + width, top + height);
//    }
//
//}

/**
 * Created by keyC on 2019/5/9.
 * 验证码输入框
 */

public class FamilyTreeView extends RelativeLayout {

    private int mItemWidthPX;//家庭成员View宽度PX
    private int mItemHeightPX;//家庭成员View高度PX
    private int mMargin = 50;  //自己与配偶的间距
    private static final int ITEM_WIDTH_DP = 50;//家庭成员View宽度50dp
    private static final int ITEM_HEIGHT_DP = 80;//家庭成员View高度80dp
    private int mStartLeft = 10;  //起始左边距
    private int mStartTop = 10;  //起始上边距

    private int mWidth = 0;
    private int mHeight = 0;

    private OnKinViewsClickListener listener;

    private Kin kin;  //数据源
    private KinPoint mKinPoint; //坐标数据源

    /**
     * 文本字体大小
     */
    private int textSize = 13;

    public FamilyTreeView(Context context) {
        this(context, null);
    }

    public FamilyTreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FamilyTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mItemWidthPX = DisplayUtil.dip2px(ITEM_WIDTH_DP);
        mItemHeightPX = DisplayUtil.dip2px(ITEM_HEIGHT_DP);
    }

    /**
     * 添加数据
     * @param kin  数据源
     * @param width  屏幕宽度
     * @param height  屏幕高度
     */
    public void setData(Kin kin, int width, int height) {
        this.kin = kin;                                        //设置数据源
        mStartLeft = width/2-(mItemWidthPX * 2 + mMargin)/2;   //设置起始数据的左边距，保证起始数据在屏幕的中间
//        mStartLeft = 10;   //设置起始数据的左边距，保证起始数据在屏幕的中间
        mStartTop = 10;                                         //设置起始数据的上边距，保证起始数据在屏幕的顶端
        initData(kin, mStartLeft, mStartTop);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //线条
        Paint mLinePaint = new Paint();
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(3.0f);// 设置笔触宽度
        mLinePaint.setStyle(Paint.Style.STROKE);// 设置画笔的填充类型(完全填充)
        mLinePaint.setAntiAlias(true);   //设置抗锯齿效果

        /**
         * 画线条
         */
        if (kin != null) {
//            drawLines(canvas, mLinePaint, kin, mWidth/2, mStartTop);
            drawLines(canvas, mLinePaint, mKinPoint);
            Log.i("TAG", "onDraw: 1");
        }
    }


    private List<View> views = new ArrayList<>();

    /**
     * 初始化数据源，画线
     * @param kin  数据源
     * @param left  左边距
     * @param top   上边距
     */
    private void initData(Kin kin, int left, int top) {

        if (kin != null) {
            mKinPoint = new KinPoint();
            final Person person = kin.getSelf();
            final Person spouse = kin.getSpouse();
            final ArrayList<Kin> kins = kin.getChildren();
            if (person != null) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_kin, this, false);
                LinearLayout selfLayout = (LinearLayout) view.findViewById(R.id.ll_self);

                final ImageView ivSelfAvatar = (ImageView) view.findViewById(R.id.iv_self_avatar);
                final TextView tvSelfCall = (TextView) view.findViewById(R.id.tv_self_call);
                tvSelfCall.setText(person.getSex());
                final TextView tvSelfName = (TextView) view.findViewById(R.id.tv_self_name);
                tvSelfName.setText(person.getName());
                ivSelfAvatar.setBackgroundResource(R.drawable.shape_red_circle);
                selfLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onViewsClick(person);
                        }
                    }
                });

                /**
                 * 存储自己的坐标，自己的正下方
                 */
                views.add(selfLayout);
                Point point = new Point();
                point.setX(selfLayout.getLeft() + selfLayout.getWidth()/2);
                point.setY(selfLayout.getTop() + selfLayout.getHeight());
                mKinPoint.setPoint(point);
                Log.i("TAG", "initData: " + selfLayout.getLayoutParams().width);

                View viewLine = (View) view.findViewById(R.id.view_line);
                LinearLayout spouseLayout = (LinearLayout) view.findViewById(R.id.ll_spouse);
                LinearLayout childLayout = (LinearLayout) view.findViewById(R.id.ll_child);

                if (spouse != null) {
                    viewLine.setVisibility(View.VISIBLE);
                    spouseLayout.setVisibility(View.VISIBLE);

                    final ImageView ivSpouseAvatar = (ImageView) view.findViewById(R.id.iv_spouse_avatar);
                    final TextView tvSpouseCall = (TextView) view.findViewById(R.id.tv_spouse_call);
                    tvSpouseCall.setText(spouse.getSex());
                    final TextView tvSpouseName = (TextView) view.findViewById(R.id.tv_spouse_name);
                    tvSpouseName.setText(spouse.getName());
                    ivSpouseAvatar.setBackgroundResource(R.drawable.shape_red_circle);
                    spouseLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onViewsClick(spouse);
                            }
                        }
                    });
                } else {
                    viewLine.setVisibility(View.GONE);
                    spouseLayout.setVisibility(View.GONE);
                }

                if (kins != null && kins.size() > 0) {
                    List<KinPoint> list = new ArrayList<>();
                    for (int i = 0; i < kins.size(); i++) {
                        KinPoint kinPoint = new KinPoint();
                        addChildView(childLayout, kins.get(i), kinPoint);
                    }
                    mKinPoint.setChild(list);
                }

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(CENTER_IN_PARENT,TRUE);
                addView(view, params);

            }
        }

//        if (kin != null) {
//            Person self = kin.getSelf();
//            Person spouse = kin.getSpouse();
//            ArrayList<Kin> kins = kin.getChildren();
//            /**
//             * 自己
//             */
//            if (self != null) {
//                initViews(self, left, top);
//            }
//            /**
//             * 配偶
//             */
//            if (spouse != null) {
//                initViews(spouse, left + mItemWidthPX + mMargin, top);
//            }
//            /**
//             * 孩子
//             * 使界面协调性，孩子从屏幕中间向左右两边分散，最顶层父类保持在屏幕宽度中间
//             */
//            if (kins != null && kins.size() > 0) {
//                /**
//                 * 累计孩子的数量，计算父类兄弟间的间隔距离
//                 */
//                int childNum = 0;
//
//                for (int i = 0; i < kins.size(); i++) {
//                    if (i == 0) {
//                        initData(kins.get(i), left, top + mMargin*2 + mItemHeightPX);
//                    } else {
//                        if (i < kins.size()/2) { //在父类左边显示
//                            initData(kins.get(i), left + childNum * (mItemWidthPX + mMargin) * 2, top + mMargin*2 + mItemHeightPX);
//
//                        } else { //在父类右边显示
//                            initData(kins.get(i), left + childNum * (mItemWidthPX + mMargin) * 2, top + mMargin*2 + mItemHeightPX);
//
//                        }
//                    }
//                    if (kins.get(i).getChildren().size() > 0) {
//                        childNum += kins.get(i).getChildren().size();   //有孩子，累加孩子的数量
//                    } else {
//                        childNum += 1;      //没有孩子的，加1，表示父母站的位置
//                    }
//                }
//            }
//        }
    }

    /**
     * 添加子元素
     */
    private void addChildView(LinearLayout layout, Kin kin, KinPoint kinPoint) {
        if (kin != null) {
            final Person person = kin.getSelf();
            final Person spouse = kin.getSpouse();
            final ArrayList<Kin> kins = kin.getChildren();
            if (person != null) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_kin, this, false);
                LinearLayout selfLayout = (LinearLayout) view.findViewById(R.id.ll_self);

                final ImageView ivSelfAvatar = (ImageView) view.findViewById(R.id.iv_self_avatar);
                final TextView tvSelfCall = (TextView) view.findViewById(R.id.tv_self_call);
                tvSelfCall.setText(person.getSex());
                final TextView tvSelfName = (TextView) view.findViewById(R.id.tv_self_name);
                tvSelfName.setText(person.getName());
                ivSelfAvatar.setBackgroundResource(R.drawable.shape_red_circle);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onViewsClick(person);
                        }
                    }
                });

                /**
                 * 存储自己的坐标，自己的正下方
                 */
                views.add(selfLayout);
                Point point = new Point();
                point.setX(selfLayout.getLeft() + selfLayout.getWidth()/2);
                point.setY(selfLayout.getTop() + selfLayout.getHeight());
                kinPoint.setPoint(point);

                View viewLine = (View) view.findViewById(R.id.view_line);
                LinearLayout spouseLayout = (LinearLayout) view.findViewById(R.id.ll_spouse);
                LinearLayout childLayout = (LinearLayout) view.findViewById(R.id.ll_child);

                if (spouse != null) {
                    viewLine.setVisibility(View.VISIBLE);
                    spouseLayout.setVisibility(View.VISIBLE);

                    final ImageView ivSpouseAvatar = (ImageView) view.findViewById(R.id.iv_spouse_avatar);
                    final TextView tvSpouseCall = (TextView) view.findViewById(R.id.tv_spouse_call);
                    tvSpouseCall.setText(spouse.getSex());
                    final TextView tvSpouseName = (TextView) view.findViewById(R.id.tv_spouse_name);
                    tvSpouseName.setText(spouse.getName());
                    ivSpouseAvatar.setBackgroundResource(R.drawable.shape_red_circle);
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onViewsClick(spouse);
                            }
                        }
                    });
                } else {
                    viewLine.setVisibility(View.GONE);
                    spouseLayout.setVisibility(View.GONE);
                }

                if (kins != null && kins.size() > 0) {
                    List<KinPoint> list = new ArrayList<>();
                    for (int i = 0; i < kins.size(); i++) {
                        KinPoint kinPoint1 = new KinPoint();
                        addChildView(childLayout, kins.get(i), kinPoint1);
                    }
                    kinPoint.setChild(list);
                }

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(CENTER_IN_PARENT,TRUE);
                layout.addView(view, params);
            }
        }
    }

    /**
     * 画线条
     * @param canvas 画布
     * @param paint 画笔
     * @param kinPoint  坐标数据源
     */
    private void drawLines(Canvas canvas, Paint paint, KinPoint kinPoint) {
        Log.i("TAG", "onDraw: views=size=" + views.size());
        for (int i = 0; i < views.size(); i++) {
            Log.i("TAG", "onDraw: views=i=" + i);
            Log.i("TAG", "onDraw: views=getLeft=" + views.get(i).getLeft());
            Log.i("TAG", "onDraw: views=getLeft=" + ((ViewGroup) views.get(i).getParent()).getLeft());
        }
//        if (kinPoint != null) {
//            Point point = kinPoint.getPoint();
//            List<KinPoint> child = kinPoint.getChild();
//            if (child != null && child.size() > 0) {
//                for (int i = 0; i < child.size(); i++) {
//                    float[] pts = {
//                            point.getX(), point.getY(), point.getX(), point.getY() + 25,
//                            point.getX(), point.getY() + 25, child.get(i).getPoint().getX(), point.getY() + 25,
//                            child.get(i).getPoint().getX(), point.getY() + 25, child.get(i).getPoint().getX(), point.getY() + 50,
//                    };
//                    canvas.drawLines(pts, paint);
//
//                    if (child.get(i).getChild() != null && child.get(i).getChild().size() > 0) {
//                        drawLines(canvas, paint, child.get(i));
//                    }
//                }
//            }
//        }
    }

    /**
     * 初始化单个元素视图
     * @param left
     * @param top
     */
    private void initViews(final Person person, int left, int top) {
        final View familyView = LayoutInflater.from(getContext()).inflate(R.layout.item_family, this, false);
        familyView.getLayoutParams().width = mItemWidthPX;
        familyView.getLayoutParams().height = mItemHeightPX;
        familyView.setTag(person);

        final ImageView ivAvatar = (ImageView) familyView.findViewById(R.id.iv_avatar);
        ivAvatar.getLayoutParams().height = mItemWidthPX;

        final TextView tvCall = (TextView) familyView.findViewById(R.id.tv_call);
        tvCall.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvCall.setTextSize(textSize);
        tvCall.setText(person.getSex());

        final TextView tvName = (TextView) familyView.findViewById(R.id.tv_name);
        tvName.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvName.setTextSize(textSize);
        tvName.setText(person.getName());

//        final String url = family.getMemberImg();
//        if (!TextUtils.isEmpty(url)) {
//            Glide.with(getContext())
//                    .load(url)
//                    .placeholder(R.drawable.family_avatar)
//                    .error(R.drawable.family_avatar)
//                    .centerCrop()
//                    .transform(new GlideCircleTransform(getContext()))
//                    .dontAnimate()
//                    .into(ivAvatar);
//        }
//        if (family.isSelect()) {
        ivAvatar.setBackgroundResource(R.drawable.shape_red_circle);
//        }
        familyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onViewsClick(person);
                }
            }
        });
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(familyView.getLayoutParams());
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        familyView.setLayoutParams(layoutParams);
//        addView(familyView, layoutParams);
        addView(familyView);
    }

    /**
     * 画线条
     */
//    private void drawLines(Canvas canvas, Paint mLinePaint, Kin kin, int left, int top) {
//
//        Person self = kin.getSelf();
//        Person spouse = kin.getSpouse();
//        ArrayList<Kin> kins = kin.getChildren();
//        /**
//         * 自己
//         */
//        if (self != null) {
//
//        }
//        /**
//         * 配偶
//         */
//        if (spouse != null) {
//            float s_x = left + mItemWidthPX;
//            float s_y = top + mItemHeightPX/2;
//            float e_x = left + mItemWidthPX + mMargin;
//            float e_y = top + mItemHeightPX/2;
//            canvas.drawLine(s_x, s_y, e_x, e_y, mLinePaint);
//        }
//        /**
//         * 孩子
//         */
//        if (kins != null && kins.size() > 0) {
//
//            /**
//             * 累计孩子的数量，计算父类兄弟间的间隔距离
//             */
//            int childNum = 0;
//
//            /**
//             * 父母到中间的纵向距离
//             */
//            float s_x = left + mItemWidthPX + 25;
//            float s_y = top + mItemHeightPX/2;
//            float e_x = left + mItemWidthPX + 25;
//            float e_y = top + mItemHeightPX + mMargin;
//            canvas.drawLine(s_x, s_y, e_x, e_y, mLinePaint);
//
//            for (int i = 0; i < kins.size(); i++) {
//                /**
//                 * 中间到儿子的横向距离
//                 */
//                float c_s_x_1 = left + mItemWidthPX + 25;
//                float c_s_y_1 = top + mItemHeightPX + mMargin;
//                float c_e_x_1 = left + i * (mItemWidthPX + 50) * 2 + mItemWidthPX/2;
//                float c_e_y_1 = top + mItemHeightPX + mMargin;
//
//                /**
//                 * 中间到儿子的纵向距离
//                 */
//                float c_s_x_2 = left + i * (mItemWidthPX + mMargin) * 2 + mItemWidthPX/2;
//                float c_s_y_2 = top + mItemHeightPX + mMargin;
//                float c_e_x_2 = left + i * (mItemWidthPX + mMargin) * 2 + mItemWidthPX/2;
//                float c_e_y_2 = top + mItemHeightPX + mMargin*2;
//
//                float[] pts = {
//                        c_s_x_1, c_s_y_1, c_e_x_1, c_e_y_1,
//                        c_s_x_2, c_s_y_2, c_e_x_2, c_e_y_2,
//                };
//                canvas.drawLines(pts, mLinePaint);
//                if (i == 0) {
//                    drawLines(canvas, mLinePaint, kins.get(i), left, top + mMargin*2 + mItemHeightPX);
//                } else {
//                    drawLines(canvas, mLinePaint, kins.get(i), left + childNum * (mItemWidthPX + mMargin) * 2, top + mMargin*2 + mItemHeightPX);
//                }
//                if (kins.get(i).getChildren().size() > 0) {
//                    childNum += kins.get(i).getChildren().size();   //有孩子，累加孩子的数量
//                } else {
//                    childNum += 1;      //没有孩子的，加1，表示父母站的位置
//                }
//            }
//        }
//    }

    /**
     * 设置点击监听
     */
    public void setOnViewsClick(OnKinViewsClickListener listener) {
        this.listener = listener;
    }

//    /**
//     * 设置滑动监听，主要判断两指缩放
//     */
//    private static final int NONE = 0;
//    private static final int DRAG = 1;
//    private static final int ZOOM = 2;
//    private int mode = NONE;
//
//    // 第一个按下的手指的点
//    private float downX = 0;
//    private float downY = 0;
//    private int moveX;    //移动距离x
//    private int moveY;    //移动距离y
//    private float mScale = 1;  //缩放比例
//    // 初始的两个手指按下的触摸点的距离
//    private float oriDis = 1f;
//    // 初始化缩放中心
//    private float mPointX;
//    private float mPointY;
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);
//
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                // 第一个手指按下事件
//                downX = event.getX();
//                downY = event.getY();
//                mode = DRAG;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                // 第二个手指按下事件
//                oriDis = distance(event);
//                if (oriDis > 10f) {
//                    mode = ZOOM;
//                }
////                setScaleX(mScale);
////                setScaleY(mScale);
//
//                break;
//            case MotionEvent.ACTION_UP:
////                float px = mWidth/2 + mHVScrollView.getScrollX();
////                float py = mHeight/2 + mHVScrollView.getScrollY();
////                mPointX = px;
////                mPointY = py;
////                Log.i("TAG", "onTouch1111-px=: " + px + "---py=" + py);
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                // 手指放开事件
//                mode = NONE;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveX = (int) (event.getX() - downX);
//                moveY = (int) (event.getY() - downY);
//                // 手指滑动事件
//                if (mode == DRAG) {
////                    if (Math.abs(moveX) > 10 || Math.abs(moveY) > 10) {
////                        // 一个手指拖动
////                        mHVScrollView.scrollBy(-moveX, -moveY);
////                        mPointX = mWidth/2 + mHVScrollView.getScrollX();
////                        mPointY = mHeight/2 + mHVScrollView.getScrollY();
////                        downX = event.getX();
////                        downY = event.getY();
////                    }
//                } else if (mode == ZOOM) {
//                    // 两个手指滑动
//                    float newDist = distance(event);
//                    if (newDist - oriDis >= 5f) {
//                        mScale+=0.01;
//                        // 缩放view，设置缩放中心和缩放大小
////                        mPointX = mWidth/2 + mHVScrollView.getScrollX();
////                        mPointY = mHeight/2 + mHVScrollView.getScrollY();
////                        setPivotX(mPointX);
////                        setPivotY(mPointY);
//                        setScaleX(mScale);
//                        setScaleY(mScale);
//                        oriDis = newDist;
//
//                    } else if (newDist - oriDis <= -5f) {
//                        mScale-=0.01;
//                        // 缩放view，设置缩放中心和缩放大小
////                        mPointX = mWidth/2 + mHVScrollView.getScrollX();
////                        mPointY = mHeight/2 + mHVScrollView.getScrollY();
////                        setPivotX(mPointX);
////                        setPivotY(mPointY);
//                        setScaleX(mScale);
//                        setScaleY(mScale);
//                        oriDis = newDist;
//                    }
//                }
//                break;
//        }
//        Log.i("TAG", "onTouchEvent: " +  event.getPointerCount());
//        if(event.getPointerCount()==2) {
//            return true;
//        } else {
//            return false;
//        }
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
