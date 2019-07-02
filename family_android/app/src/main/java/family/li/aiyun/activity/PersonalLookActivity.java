package family.li.aiyun.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.ConfigInfo;
import family.li.aiyun.bean.ConfigRank;
import family.li.aiyun.bean.UserInfo;
import family.li.aiyun.bean.UserProfile;
import family.li.aiyun.bean.UserRelation;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static family.li.aiyun.LiApplication.getContext;

public class PersonalLookActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvBack;   //返回
    private ImageView mIvHead;   //头像
    private TextView mTvName;   //姓名
    private TextView mTvAlias;  //别名
    private TextView mTvSex;    //性别
    private TextView mTvRanking;   //姊妹排行
    private TextView mTvPhone;    //电话
    private LinearLayout mLlBox;    //展开收起更多资料
    private TextView mTvId;      //身份证
    private TextView mTvFather;   //父亲姓名
    private TextView mTvMother;   //母亲姓名
    private TextView mTvTag;      //标记
    private TextView mTvEra;      //房代
    private TextView mTvWord;         //字辈
    private TextView mTvNativePlace;     //籍贯
    private TextView mTvEducation;      //学历
    private TextView mTvSchool;       //毕业院校
    private TextView mTvJob;    //职业
    private TextView mTvCompany;      //单位
    private TextView mTvCompanyAddress;  //单位地址
    private TextView mTvIntroduction;  //简介
    private LinearLayout mLlMore;  //展开收起
    private TextView mTvBox;         //展开收起
    private ImageView mIvBox;         //展开收起
    private TextView mTvDate;    //出生日期
    private TextView mTvAddress;   //家庭住址
    private TextView mTvLive;    //是否健在
    private LinearLayout mLlDie;    //是否健在
    private TextView mTvDieDate;     //去世时间

    private boolean isShowBox = false;  //展开收起 true 展开， false 收起
    private UserInfo mUserInfo; //人员所有信息
    private List<ConfigInfo> mEducationConfig = new ArrayList<>();  //学历
    private List<ConfigInfo> mRankingManConfig = new ArrayList<>();  //排行（男）
    private List<ConfigInfo> mRankingWoManConfig = new ArrayList<>();  //排行（女）
    private int mSex = 1; //默认性别 1 男 2 女

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_look);

        //获取学历数据配置
        getEducationsConfig();

        //获取排行数据配置
        getFamilyRankConfig();

        //初始化view
        initViews();

        if (getIntent().getStringExtra("user_id") != null) {
            String user_id = getIntent().getStringExtra("user_id");
            //获取人员数据信息
            getPersonalData(user_id);
        }
    }

    /**
     * 获取学历数据配置
     */
    private void getEducationsConfig() {
        HashMap param = new HashMap();
        HttpManager.getInstance().post("index/Config/educations", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    mEducationConfig = new Gson().fromJson(json, new TypeToken<List<ConfigInfo>>(){}.getType());
                }
            }

            @Override
            public void requestFail(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestError(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestStart() {

            }

            @Override
            public void requestComplete() {

            }
        });
    }

    /**
     * 获取排行数据配置
     */
    private void getFamilyRankConfig() {
        HashMap param = new HashMap();
        HttpManager.getInstance().post("index/Config/familyRank", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    ConfigRank rank = new Gson().fromJson(json, new TypeToken<ConfigRank>(){}.getType());
                    mRankingManConfig = rank.getMale();
                    mRankingWoManConfig = rank.getFemale();
                }
            }

            @Override
            public void requestFail(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestError(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestStart() {

            }

            @Override
            public void requestComplete() {

            }
        });
    }

    /**
     * 获取人员数据信息
     */
    private void getPersonalData(String mUserId) {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("user_id", mUserId);
        HttpManager.getInstance().post("index/User/getDetailWithParents", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    mUserInfo = new Gson().fromJson(json, new TypeToken<UserInfo>(){}.getType());
                    //设置人员信息
                    setPersonalData();
                }
            }

            @Override
            public void requestFail(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestError(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestStart() {
                if (getDialogFragment() != null) {
                    getDialogFragment().show(getSupportFragmentManager(), "get_detail_progress");
                }
            }

            @Override
            public void requestComplete() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getDialogFragment() != null) {
                            getDialogFragment().dismiss();
                        }
                    }
                }, 300);
            }
        });
    }

    /**
     * 设置人员信息
     */
    @SuppressLint("SetTextI18n")
    private void setPersonalData() {
        if (mUserInfo != null) {
            UserProfile mUserProfile = mUserInfo.getUser_profile();
            UserRelation mUserRelation = mUserInfo.getUser_relation();
            if (!TextUtils.isEmpty(mUserInfo.getPhone_display())) {
                mTvPhone.setText(mUserInfo.getPhone_display());
            }
            if (mUserInfo.getHide_info() == 0) {
                mLlMore.setVisibility(View.VISIBLE);
            } else {
                if (Utils.USER_ID.equals(mUserInfo.getId() + "")) {
                    mLlMore.setVisibility(View.VISIBLE);
                } else {
                    mLlMore.setVisibility(View.GONE);
                }
            }
            if (mUserRelation != null) {
                if (!TextUtils.isEmpty(mUserRelation.getFather_name())) {
                    mTvFather.setText(mUserRelation.getFather_name());
                }
                if (!TextUtils.isEmpty(mUserRelation.getMother_name())) {
                    mTvMother.setText(mUserRelation.getMother_name());
                }
                if (mUserRelation.getSex() == 2) {
                    mSex = 2;
                    mTvSex.setText("女");
                } else {
                    mSex = 1;
                    mTvSex.setText("男");
                }
            }
            if (mUserProfile != null) {
                Glide.with(getContext())
                        .load(!TextUtils.isEmpty(mUserProfile.getHead_img()) ? mUserProfile.getHead_img() : ((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle))
                        .placeholder((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                        .error((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                        .fallback((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(mIvHead);
                if (!TextUtils.isEmpty(mUserProfile.getName())) {
                    mTvName.setText(mUserProfile.getName());
                }
                if (!TextUtils.isEmpty(mUserProfile.getAlias())) {
                    mTvAlias.setText(mUserProfile.getAlias());
                }
                if (mUserProfile.getRank() != 0) {
                    try {
                        mTvRanking.setText(mUserProfile.getSex() == 1 ? (mRankingManConfig.get(mUserProfile.getRank() - 1).getName() + "") : (mRankingWoManConfig.get(mUserProfile.getRank() - 1).getName() + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(mUserProfile.getId_card())) {
                    mTvId.setText(mUserProfile.getId_card());
                }
                if (!TextUtils.isEmpty(mUserProfile.getMark())) {
                    mTvTag.setText(mUserProfile.getMark());
                }
                if (!TextUtils.isEmpty(mUserProfile.getFamily()) && !TextUtils.isEmpty(mUserProfile.getGeneration()) ) {
                    mTvEra.setText("第" + mUserProfile.getFamily() + "房    第" + mUserProfile.getGeneration() + "代");
                }
                if (!TextUtils.isEmpty(mUserProfile.getZi())) {
                    mTvWord.setText(mUserProfile.getZi());
                }
                if (!TextUtils.isEmpty(mUserProfile.getOrigin())) {
                    mTvNativePlace.setText(mUserProfile.getOrigin());
                }
                if (mUserProfile.getEducation() != 0) {
                    try {
                        mTvEducation.setText(mEducationConfig.get(mUserProfile.getEducation() - 1).getName() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(mUserProfile.getSchool())) {
                    mTvSchool.setText(mUserProfile.getSchool());
                }
                if (!TextUtils.isEmpty(mUserProfile.getJob())) {
                    mTvJob.setText(mUserProfile.getJob());
                }
                if (!TextUtils.isEmpty(mUserProfile.getCompany())) {
                    mTvCompany.setText(mUserProfile.getCompany());
                }
                if (!TextUtils.isEmpty(mUserProfile.getWork_place())) {
                    mTvCompanyAddress.setText(mUserProfile.getWork_place());
                }
                if (!TextUtils.isEmpty(mUserProfile.getWork_place())) {
                    mTvCompanyAddress.setText(mUserProfile.getWork_place());
                }
                if (!TextUtils.isEmpty(mUserProfile.getBrief())) {
                    mTvIntroduction.setText(mUserProfile.getBrief());
                }
                if (mUserProfile.getBirth_year() != 0) {
                    try {
                        mTvDate.setText(mUserProfile.getBirth_year() + "年" + mUserProfile.getBirth_month() + "月" + mUserProfile.getBirth_date() + "日");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(mUserProfile.getAddress())) {
                    mTvAddress.setText(mUserProfile.getAddress());
                }
                if (mUserProfile.getIs_alive() == 0) {
                    mTvLive.setText("否");
                    mLlDie.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(mUserProfile.getPass_away_time())) {
                        mTvDieDate.setText(mUserProfile.getPass_away_time());
                    }
                } else {
                    mTvLive.setText("是");
                    mLlDie.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 初始化view
     */
    private void initViews() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvHead = (ImageView) findViewById(R.id.iv_head);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvAlias = (TextView) findViewById(R.id.tv_alias);
        mTvSex = (TextView) findViewById(R.id.tv_sex);
        mTvRanking = (TextView) findViewById(R.id.tv_ranking);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mLlBox = (LinearLayout) findViewById(R.id.ll_box);
        mTvId = (TextView) findViewById(R.id.tv_id);
        mTvFather = (TextView) findViewById(R.id.tv_father);
        mTvMother = (TextView) findViewById(R.id.tv_mother);
        mTvTag = (TextView) findViewById(R.id.tv_tag);
        mTvEra = (TextView) findViewById(R.id.tv_era);
        mTvWord = (TextView) findViewById(R.id.tv_word);
        mTvNativePlace = (TextView) findViewById(R.id.tv_native_place);
        mTvEducation = (TextView) findViewById(R.id.tv_education);
        mTvSchool = (TextView) findViewById(R.id.tv_school);
        mTvJob = (TextView) findViewById(R.id.tv_job);
        mTvCompany = (TextView) findViewById(R.id.tv_company);
        mTvCompanyAddress = (TextView) findViewById(R.id.tv_company_address);
        mTvIntroduction = (TextView) findViewById(R.id.tv_introduction);
        mLlMore = (LinearLayout) findViewById(R.id.ll_more);
        mTvBox = (TextView) findViewById(R.id.tv_box);
        mIvBox = (ImageView) findViewById(R.id.iv_box);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvLive = (TextView) findViewById(R.id.tv_live);
        mLlDie = (LinearLayout) findViewById(R.id.ll_die_date);
        mTvDieDate = (TextView) findViewById(R.id.tv_die_date);

        mIvBack.setOnClickListener(this);
        mLlMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:  //返回上一页
                finish();
                break;

            case R.id.ll_more:   //点击展开收起
                setShowBox();
                break;
        }
    }

    /**
     * 设置展开收起
     */
    private void setShowBox() {
        if (isShowBox) {
            isShowBox = false;
            mTvBox.setText("展开更多资料");
//            mLlBox.setVisibility(View.VISIBLE);
//            RotateAnimation rotateAnimation = new RotateAnimation(0,0);
//            rotateAnimation.setDuration(300);
//            rotateAnimation.setFillAfter(true);
//            mIvBox.startAnimation(rotateAnimation);

            //展开收起列表
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLlBox.getLayoutParams();
            layoutParams.height = 0;
            mLlBox.setLayoutParams(layoutParams);

//            ValueAnimator animator = ValueAnimator.ofInt(0, mLlBox.getHeight());
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                @Override
//                public void onAnimationUpdate(ValueAnimator arg0) {
//                    int value = (int) arg0.getAnimatedValue();
//                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLlBox.getLayoutParams();
//                    layoutParams.height = value;
//                    mLlBox.setLayoutParams(layoutParams);
//                }
//            });
//            animator.setDuration(300);
//            animator.start();

            //旋转箭头
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_box_rotate_anim1);
            animation.setFillAfter(true);
            mIvBox.startAnimation(animation);
        } else {
            isShowBox = true;
            mTvBox.setText("收起更多资料");
//            mLlBox.setVisibility(View.GONE);
            //code 实现
//            RotateAnimation rotateAnimation = new RotateAnimation(0,180); //起始角度，结束角度
//            rotateAnimation.setDuration(300);  //执行时间
//            rotateAnimation.setFillAfter(true);   //执行完后是否停留在此效果
//            mIvBox.startAnimation(rotateAnimation);

            //展开收起列表
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLlBox.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            mLlBox.setLayoutParams(layoutParams);
//            ValueAnimator animator = ValueAnimator.ofInt(mLlBox.getHeight(), 0);
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                @Override
//                public void onAnimationUpdate(ValueAnimator arg0) {
//                    int value = (int) arg0.getAnimatedValue();
//                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLlBox.getLayoutParams();
//                    layoutParams.height = value;
//                    mLlBox.setLayoutParams(layoutParams);
//                }
//            });
//            animator.setDuration(300);
//            animator.start();

            //旋转箭头
            //xml 实现
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_box_rotate_anim);
            animation.setFillAfter(true);
            mIvBox.startAnimation(animation);
        }
    }
}
