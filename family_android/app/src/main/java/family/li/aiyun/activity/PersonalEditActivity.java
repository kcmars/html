package family.li.aiyun.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.ConfigInfo;
import family.li.aiyun.bean.ConfigRank;
import family.li.aiyun.bean.UserInfo;
import family.li.aiyun.bean.UserProfile;
import family.li.aiyun.bean.UserRelation;
import family.li.aiyun.fragment.ConfigPickFragment;
import family.li.aiyun.fragment.DatePickFragment;
import family.li.aiyun.fragment.FamilyPickFragment;
import family.li.aiyun.fragment.NativePickFragment;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.SystemUtils;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;
import family.li.aiyun.view.CustomPickFragment;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static family.li.aiyun.LiApplication.getContext;

public class PersonalEditActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ImageView mIvBack;   //返回
    private TextView mTvTitle;   //标题
    private TextView mTvSave;    //保存
    private ImageView mIvHead;   //头像
    private EditText mEtName;   //姓名
    private EditText mEtAlias;  //别名
    private RadioGroup mRgSex;    //性别
    private RadioButton mRbSexMan;  //男
    private RadioButton mRbSexWoMan;   //女
    private LinearLayout mLlRanking;   //姊妹排行
    private EditText mEtRanking;    //排行
    private EditText mEtPhone;    //电话
    private TextView mTvContact;   //通讯录
    private LinearLayout mLlBox;    //展开收起更多资料
    private EditText mEtId;      //身份证
    private EditText mEtFather;   //父亲姓名
    private EditText mEtMother;   //母亲姓名
    private EditText mEtTag;      //标记
    private LinearLayout mLlEra;   //房代
    private EditText mEtEra;      //房代
    private EditText mEtWord;         //字辈
    private LinearLayout mLlNativePlace;  //籍贯
    private EditText mEtNativePlace;     //籍贯
    private LinearLayout mLlEducation;   //学历
    private EditText mEtEducation;      //学历
    private EditText mEtSchool;       //毕业院校
    private EditText mEtOccupation;    //职业
    private EditText mEtCompany;      //单位
    private EditText mEtCompanyAddress;  //单位地址
    private EditText mEtIntroduction;  //简介
    private LinearLayout mLlMore;  //展开收起更多资料
    private TextView mTvBox;         //展开收起
    private ImageView mIvBox;         //展开收起
    private LinearLayout mLlDate;    //出生日期
    private TextView mTvDate;    //出生日期
    private EditText mEtAddress;   //家庭住址
    private RadioGroup mRgLive;    //是否健在
    private RadioButton mRbLive;   //健在
    private RadioButton mRbDie;    //去世
    private LinearLayout mLlDieDate;  //去世时间
    private TextView mTvDieDate;     //去世时间


    private int mSex = 1;  //性别 1 男， 2 女
    private int mLive = 1;  //健在  1 健在， 0 死亡
    private boolean isShowBox = false;  //展开收起 true 展开， false 收起
    private String mBase64; //头像Base64
    private String mHeadImgUrl = ""; //头像图片地址
//    private String[] mRanking1 = {"长子", "次子", "三子", "四子", "五子"};  //排行
//    private String[] mRanking2 = {"长女", "次女", "三女", "四女", "五女"};  //排行
//    private String[] mEducationConfig = {"小学", "初中", "高中", "中专", "大专", "本科", "研究生", "博士", "小学以下"};  //学历
    private List<ConfigInfo> mEducationConfig = new ArrayList<>();  //学历
    private int mRanking = -1;  //学历
    private List<ConfigInfo> mRankingManConfig = new ArrayList<>();  //排行（男）
    private List<ConfigInfo> mRankingWoManConfig = new ArrayList<>();  //排行（女）
    private int mEducation = -1; //排行

    private int mLiveYear = 0; //出生日期年
    private int mLiveMonth = 0; //出生日期月
    private int mLiveDay = 0; //出生日期日
    private String mDieDate = ""; //去世日期
    private String mProvince = ""; //省份
    private String mCity = ""; //城市
    private String mFamily = ""; //房
    private String mGeneration = ""; //代

    private String mUserId = "";  //添加人员user_id
    private UserInfo mUserInfo; //人员所有信息
    private int mRelation = -2;  //添加人员与被添加人员的关系 -2 别人 -1 自己， 0 无关， 1 父亲， 2 母亲， 3 配偶， 5 兄弟， 6 儿子， 7 女儿
    private boolean isAdd = false;

    private boolean isChange = false;
    private int first_edit = 0; //是否第一次编辑 0 否 1 是

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        //获取学历数据配置
        getEducationsConfig();

        //获取排行数据配置
        getFamilyRankConfig();

        //初始化view
        initViews();

        //标题title
        if (getIntent().getStringExtra("title") != null && !TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            mTvTitle.setText(getIntent().getStringExtra("title"));
        }
        //是否首次修改自己的信息（首次注册后进入编辑自己的信息）
        if (getIntent().getIntExtra("first_edit", 0) != 0) {
            first_edit = getIntent().getIntExtra("first_edit", 0);
        }
        //查看者的id
        if (getIntent().getStringExtra("user_id") != null) {
            mUserId = getIntent().getStringExtra("user_id");
        }
        //查看者与被查看者的关系
        mRelation = getIntent().getIntExtra("user_relation", -2);

        //被添加人的性别
        mSex = getIntent().getIntExtra("user_sex", 1);
        if (mRbSexMan != null && mRbSexWoMan != null) {
            if (mSex == 1) {
                mRbSexMan.setChecked(true);
                mRbSexWoMan.setChecked(false);
            } else {
                mRbSexMan.setChecked(false);
                mRbSexWoMan.setChecked(true);
            }
        }
        if (mIvHead != null) {
            Glide.with(getContext())
                    .load((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                    .placeholder((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                    .error((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                    .fallback((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(mIvHead);
        }
        /**
         * 如果是添加新人，就不获取成员详细信息数据
         */
        isAdd = getIntent().getBooleanExtra("is_add", false);
        if (!isAdd) {
            getPersonalData();
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
     * 获取成员详细信息
     */
    private void getPersonalData() {
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
                    setUserInfo();
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
    private void setUserInfo() {
        if (mUserInfo != null) {
            UserProfile mUserProfile = mUserInfo.getUser_profile();
            UserRelation mUserRelation = mUserInfo.getUser_relation();
            if (!TextUtils.isEmpty(mUserInfo.getPhone_display())) {
                mEtPhone.setText(mUserInfo.getPhone_display());
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
                    mEtFather.setText(mUserRelation.getFather_name());
                }
                if (!TextUtils.isEmpty(mUserRelation.getMother_name())) {
                    mEtMother.setText(mUserRelation.getMother_name());
                }
                if (mUserRelation.getSex() == 2) {
                    mSex = 2;
                    mRbSexWoMan.setChecked(true);
                } else {
                    mSex = 1;
                    mRbSexMan.setChecked(true);
                }
            }
            if (mUserProfile != null) {
                if (!TextUtils.isEmpty(mUserProfile.getName())) {
                    mEtName.setText(mUserProfile.getName());
                }
                if (!TextUtils.isEmpty(mUserProfile.getAlias())) {
                    mEtAlias.setText(mUserProfile.getAlias());
                }
                Glide.with(getContext())
                        .load(!TextUtils.isEmpty(mUserProfile.getHead_img()) ? mUserProfile.getHead_img() : ((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle))
                        .placeholder((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                        .error((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                        .fallback((mSex == 2) ? R.mipmap.icon_woman_gray_circle : R.mipmap.icon_man_gray_circle)
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(mIvHead);
                if (first_edit == 0 && !isAdd) {
                    mRbSexMan.setEnabled(false);
                    mRbSexWoMan.setEnabled(false);
                } else {
                    mRbSexMan.setEnabled(true);
                    mRbSexWoMan.setEnabled(true);
                }
                if (mUserProfile.getRank() != 0) {
                    try {
                        mEtRanking.setText(mSex == 1 ? (mRankingManConfig.get(mUserProfile.getRank() - 1).getName() + "") : (mRankingWoManConfig.get(mUserProfile.getRank() - 1).getName() + ""));
                        mRanking = mUserProfile.getRank();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(mUserProfile.getId_card())) {
                    mEtId.setText(mUserProfile.getId_card());
                }
                if (!TextUtils.isEmpty(mUserProfile.getMark())) {
                    mEtTag.setText(mUserProfile.getMark());
                }
                if (!TextUtils.isEmpty(mUserProfile.getFamily()) && !TextUtils.isEmpty(mUserProfile.getGeneration()) ) {
                    mEtEra.setText("第" + mUserProfile.getFamily() + "房    第" + mUserProfile.getGeneration() + "代");
                    mFamily = mUserProfile.getFamily();
                    mGeneration = mUserProfile.getGeneration();
                }
                if (!TextUtils.isEmpty(mUserProfile.getZi())) {
                    mEtWord.setText(mUserProfile.getZi());
                }
                if (!TextUtils.isEmpty(mUserProfile.getOrigin())) {
                    mEtNativePlace.setText(mUserProfile.getOrigin());
                }
                if (mUserProfile.getEducation() != 0) {
                    try {
                        mEtEducation.setText(mEducationConfig.get(mUserProfile.getEducation() - 1).getName() + "");
                        mEducation = mUserProfile.getEducation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(mUserProfile.getSchool())) {
                    mEtSchool.setText(mUserProfile.getSchool());
                }
                if (!TextUtils.isEmpty(mUserProfile.getJob())) {
                    mEtOccupation.setText(mUserProfile.getJob());
                }
                if (!TextUtils.isEmpty(mUserProfile.getCompany())) {
                    mEtCompany.setText(mUserProfile.getCompany());
                }
                if (!TextUtils.isEmpty(mUserProfile.getWork_place())) {
                    mEtCompanyAddress.setText(mUserProfile.getWork_place());
                }
                if (!TextUtils.isEmpty(mUserProfile.getWork_place())) {
                    mEtCompanyAddress.setText(mUserProfile.getWork_place());
                }
                if (!TextUtils.isEmpty(mUserProfile.getBrief())) {
                    mEtIntroduction.setText(mUserProfile.getBrief());
                }
                if (mUserProfile.getBirth_year() != 0) {
                    mLiveYear = mUserProfile.getBirth_year();
                    mLiveMonth = mUserProfile.getBirth_month();
                    mLiveDay = mUserProfile.getBirth_date();
                    try {
                        mTvDate.setText(mUserProfile.getBirth_year() + "年" + mUserProfile.getBirth_month() + "月" + mUserProfile.getBirth_date() + "日");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(mUserProfile.getAddress())) {
                    mEtAddress.setText(mUserProfile.getAddress());
                }
                if (mUserProfile.getIs_alive() == 0) {
                    mLive = 0;
                    mRbDie.setChecked(true);
                    mLlDieDate.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(mUserProfile.getPass_away_time())) {
                        mTvDieDate.setText(mUserProfile.getPass_away_time());
                    }
                } else {
                    mLive = 1;
                    mRbLive.setChecked(true);
                    mLlDieDate.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 初始化view
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvSave = (TextView) findViewById(R.id.tv_save);
        mIvHead = (ImageView) findViewById(R.id.iv_head);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtAlias = (EditText) findViewById(R.id.et_alias);
        mRgSex = (RadioGroup) findViewById(R.id.rg_sex);
        mRbSexMan = (RadioButton) findViewById(R.id.rb_sex_man);
        mRbSexWoMan = (RadioButton) findViewById(R.id.rb_sex_woman);
        mLlRanking = (LinearLayout) findViewById(R.id.ll_ranking);
        mEtRanking = (EditText) findViewById(R.id.et_ranking);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mTvContact = (TextView) findViewById(R.id.tv_contact);
        mLlBox = (LinearLayout) findViewById(R.id.ll_box);
        mEtId = (EditText) findViewById(R.id.et_id);
        mEtFather = (EditText) findViewById(R.id.et_father);
        mEtMother = (EditText) findViewById(R.id.et_mother);
        mEtTag = (EditText) findViewById(R.id.et_tag);
        mLlEra = (LinearLayout) findViewById(R.id.ll_era);
        mEtEra = (EditText) findViewById(R.id.et_era);
        mEtWord = (EditText) findViewById(R.id.et_word);
        mLlNativePlace = (LinearLayout) findViewById(R.id.ll_native_place);
        mEtNativePlace = (EditText) findViewById(R.id.et_native_place);
        mLlEducation = (LinearLayout) findViewById(R.id.ll_education);
        mEtEducation = (EditText) findViewById(R.id.et_education);
        mEtSchool = (EditText) findViewById(R.id.et_school);
        mEtOccupation = (EditText) findViewById(R.id.et_occupation);
        mEtCompany = (EditText) findViewById(R.id.et_company);
        mEtCompanyAddress = (EditText) findViewById(R.id.et_company_address);
        mEtIntroduction = (EditText) findViewById(R.id.et_introduction);
        mLlMore = (LinearLayout) findViewById(R.id.ll_more);
        mTvBox = (TextView) findViewById(R.id.tv_box);
        mIvBox = (ImageView) findViewById(R.id.iv_box);
        mLlDate = (LinearLayout) findViewById(R.id.ll_date);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mEtAddress = (EditText) findViewById(R.id.et_address);
        mRgLive = (RadioGroup) findViewById(R.id.rg_live);
        mRbLive = (RadioButton) findViewById(R.id.rb_live);
        mRbDie = (RadioButton) findViewById(R.id.rb_die);
        mLlDieDate = (LinearLayout) findViewById(R.id.ll_die_date);
        mTvDieDate = (TextView) findViewById(R.id.tv_die_date);

        mIvBack.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        mIvHead.setOnClickListener(this);
        mLlRanking.setOnClickListener(this);
        mEtRanking.setOnClickListener(this);
        mTvContact.setOnClickListener(this);
        mLlEra.setOnClickListener(this);
        mEtEra.setOnClickListener(this);
        mEtNativePlace.setOnClickListener(this);
        mLlNativePlace.setOnClickListener(this);
        mLlEducation.setOnClickListener(this);
        mEtEducation.setOnClickListener(this);
        mLlMore.setOnClickListener(this);
        mLlDate.setOnClickListener(this);
        mLlDieDate.setOnClickListener(this);

        mRgSex.setOnCheckedChangeListener(this);
        mRgLive.setOnCheckedChangeListener(this);

        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (!(i != 3 && i != 8 && s.charAt(i) == ' ')) {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!TextUtils.isEmpty(sb.toString().trim()) && !sb.toString().equals(s.toString())) {
                    mEtPhone.setText(sb.toString());
                    mEtPhone.setSelection(sb.length());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    @Override
    public void onClick(View v) {
        hintKeyBoard();
        switch (v.getId()) {
            case R.id.iv_back:  //返回
                closePage();
                break;

            case R.id.tv_save:   //保存
                saveInfo();
                break;

            case R.id.iv_head:   //头像
                selectPicture();
                break;

            case R.id.ll_ranking:   //排行
                selectRanking();
                break;

            case R.id.et_ranking:   //排行
                selectRanking();
                break;

            case R.id.tv_contact:   //联系人
                getContact();
                break;

            case R.id.ll_era:   //房代
                selectFamilyGeneration("选择房代", mEtEra);
                break;

            case R.id.et_era:   //房代
                selectFamilyGeneration("选择房代", mEtEra);
                break;

            case R.id.ll_native_place:   //籍贯
                selectNative("选择籍贯", mEtNativePlace);
                break;

            case R.id.et_native_place:   //籍贯
                selectNative("选择籍贯", mEtNativePlace);
                break;

            case R.id.ll_education:   //学历
                selectEducation();
                break;

            case R.id.et_education:   //学历
                selectEducation();
                break;

            case R.id.ll_more:   //展开收起
                setShowBox();
                break;

            case R.id.ll_date:   //出生日期
                selectDate("出生日期", mTvDate);
                break;

            case R.id.ll_die_date:   //死亡日期
                selectDate("去世日期", mTvDieDate);
                break;

        }
    }

    /**
     * 选择排行
     */
    private void selectRanking() {
        ConfigPickFragment.newInstance("选择排行", mSex == 1 ? mRankingManConfig : mRankingWoManConfig, new ConfigPickFragment.OnSelectedListener() {
            @Override
            public void onSelected(String text, int index) {
                Log.i("TAG", "onSelected: " + index);
                mEtRanking.setText(text);
                mRanking = (index + 1);
            }
        }).show(getSupportFragmentManager(), "select_ranking");
    }

    /**
     * 选择房代
     */
    private void selectFamilyGeneration(String title, final EditText view) {
        FamilyPickFragment.newInstance(title, new FamilyPickFragment.OnSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelected(String family, String generation) {
                view.setText(family + "    " + generation);
                mFamily = family;
                mGeneration = generation;
            }
        }).show(getSupportFragmentManager(), "select_family_generation");
    }

    /**
     * 选择籍贯
     */
    private void selectNative(String title, final EditText view) {
        NativePickFragment.newInstance(title, new NativePickFragment.OnSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelected(String province, String city) {
                view.setText(province + "  " + city);
                mProvince = province;
                mCity = city;
            }
        }).show(getSupportFragmentManager(), "select_native");
    }

    /**
     * 选择学历
     */
    private void selectEducation() {
        ConfigPickFragment.newInstance("选择学历", mEducationConfig, new ConfigPickFragment.OnSelectedListener() {
            @Override
            public void onSelected(String text, int index) {
                mEtEducation.setText(text);
                mEducation = (index + 1);
            }
        }).show(getSupportFragmentManager(), "select_education");
    }

    /**
     * 选择日期
     * @param view
     */
    private void selectDate(String title, final TextView view) {
        DatePickFragment.newInstance(title, new DatePickFragment.OnSelectedTimeListener() {
            @Override
            public void onTimeSelected(Date date) {
//                view.setText(Utils.formatTime(date.getTime() / 1000 + ""));
//                if (view.getId() == mTvDate.getId()) {
//                    mLiveDate = date.getTime() / 1000 + "";
//                } else if (view.getId() == mTvDieDate.getId()) {
//                    mDieDate = date.getTime() / 1000 + "";
//                }
            }

            @Override
            public void onSelected(String year, String month, String day) {
                view.setText((year + month + day));
                if (view.getId() == mTvDate.getId()) {
                    mLiveYear = Utils.getNumbers(year);
                    mLiveMonth = Utils.getNumbers(month);
                    mLiveDay = Utils.getNumbers(day);
                } else if (view.getId() == mTvDieDate.getId()) {
                    mDieDate = (year + month + day);
                }
            }
        }).show(getSupportFragmentManager(), "select_date");
    }

    /**
     * 选择图片
     */
    private void selectPicture() {
        /**
         * create()方法参数一是上下文，在activity中传activity.this，在fragment中传fragment.this。参数二为请求码，用于结果回调onActivityResult中判断
         * selectPicture()方法参数分别为 是否裁剪、裁剪后图片的宽(单位px)、裁剪后图片的高、宽比例、高比例。都不传则默认为裁剪，宽200，高200，宽高比例为1：1。
         */
        PictureSelector
                .create(this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(false, 200, 200, 1, 1);
    }

    /**
     * 获取图片
     * @param path
     */
    private void getResult(final String path) {
        Luban.with(this)
                .load(path)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
//                .setTargetDir(getPath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @SuppressLint("NewApi")
                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        FileInputStream inputFile = null;
                        try {
                            inputFile = new FileInputStream(file);
                            byte[] mBytes = new byte[(int)file.length()];
                            inputFile.read(mBytes);
                            inputFile.close();
                            mBase64 = "data:image/png;base64," + Base64.encodeToString(mBytes, Base64.NO_WRAP);
                            uploadHeadImg(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Glide.with(PersonalEditActivity.this)
                                .load(path)
                                .placeholder(R.mipmap.icon_man_gray_circle)
                                .error(R.mipmap.icon_man_gray_circle)
                                .fallback(R.mipmap.icon_man_gray_circle)
                                .bitmapTransform(new CropCircleTransformation(getContext()))
                                .into(mIvHead);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                       ToastUtil.showToast("图片压缩失败，请重新选择图片");
                    }
                }).launch();    //启动压缩
    }

    /**
     * 上传头像
     */
    private void uploadHeadImg(File file) {
        HashMap param = new HashMap();
        param.put("device_no", SystemUtils.getDeviceId());
        param.put("system_type","2");
        param.put("system_version", Utils.APP_VERSION_NAME);
        param.put("system_name", android.os.Build.BRAND+" "+android.os.Build.MODEL);
        param.put("app_version", Utils.APP_VERSION_CODE+"");
        param.put("token", Utils.TOKEN);
        param.put("user_id", "-1");
        HttpManager.getInstance().uploadImgPost("index/User/uploadHeadImg", param, file, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    try {
                        mHeadImgUrl = HttpManager.PUBLIC_HOST + (String) data;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
     * 保存资料
     */
    private void saveInfo() {
        if (isAdd) {
            addData();
        } else {
            editData();
        }
    }

    /**
     * 添加资料
     */
    private void addData() {
        String name = mEtName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("请输入姓名");
            return;
        }
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("user_id", mUserId);
        param.put("admin_add", Utils.ADMIN);
        if (!TextUtils.isEmpty(mHeadImgUrl)) {
            param.put("head_img", mHeadImgUrl);
        }
        param.put("name", name);
        if (!TextUtils.isEmpty(mEtAlias.getText().toString())) {
            param.put("alias", mEtAlias.getText().toString());
        }
        if (!mEtPhone.getText().toString().contains("*")) {
            param.put("phone", Utils.replaceBlank(mEtPhone.getText().toString()));
        } else {
            param.put("phone", mUserInfo != null ? mUserInfo.getPhone() : "");
        }
        param.put("area_code", mUserInfo != null ? mUserInfo.getArea_code() : "86");
        if (!mEtId.getText().toString().contains("*") && !TextUtils.isEmpty(mEtId.getText().toString())) {
            param.put("id_card", mEtId.getText().toString());
        }
        if (mLiveYear != 0) {
            param.put("birth_year", mLiveYear);
        }
        if (mLiveMonth != 0) {
            param.put("birth_month", mLiveMonth);
        }
        if (mLiveDay != 0) {
            param.put("birth_date", mLiveDay);
        }
        param.put("is_alive", mLive + "");
        param.put("sex", mSex + "");
        param.put("relation", mRelation + "");
        param.put("is_ex", "0");
        if (!TextUtils.isEmpty(mEtTag.getText().toString())) {
            param.put("mark", mEtTag.getText().toString());
        }
        if (!TextUtils.isEmpty(mFamily)) {
            param.put("family", mFamily);
        }
        if (!TextUtils.isEmpty(mGeneration)) {
            param.put("generation", mGeneration);
        }
        if (!TextUtils.isEmpty(mEtWord.getText().toString())) {
            param.put("zi", mEtWord.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtNativePlace.getText().toString())) {
            param.put("origin", mEtNativePlace.getText().toString());
        }
        if (mEducation != -1) {
            param.put("education", mEducation);
        }
        if (!TextUtils.isEmpty(mEtSchool.getText().toString())) {
            param.put("school", mEtSchool.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtOccupation.getText().toString())) {
            param.put("job", mEtOccupation.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtCompanyAddress.getText().toString())) {
            param.put("work_place", mEtCompanyAddress.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtIntroduction.getText().toString())) {
            param.put("brief", mEtIntroduction.getText().toString());
        }
        if (!TextUtils.isEmpty(mDieDate)) {
            param.put("pass_away_time", mDieDate);
        }
        if (mRanking != -1) {
            param.put("rank", mRanking);
        }
        if (!TextUtils.isEmpty(mEtCompany.getText().toString())) {
            param.put("company", mEtCompany.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtAddress.getText().toString())) {
            param.put("address", mEtAddress.getText().toString());
        }
        HttpManager.getInstance().post("index/User/addRelative", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                ToastUtil.showToast(msg);
                isChange = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closePage();
                    }
                }, 500);
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
                    getDialogFragment().show(getSupportFragmentManager(), "add_progress");
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
     * 修改资料
     */
    private void editData() {
        String name = mEtName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("请输入姓名");
            return;
        }
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("user_id", mUserId);
        param.put("admin_add", Utils.ADMIN);
        if (first_edit != 0) {
            param.put("first_edit", "1");
        }
        if (!TextUtils.isEmpty(mHeadImgUrl)) {
            param.put("head_img", mHeadImgUrl);
        }
        param.put("name", name);
        if (!TextUtils.isEmpty(mEtAlias.getText().toString())) {
            param.put("alias", mEtAlias.getText().toString());
        }
        if (!mEtPhone.getText().toString().contains("*")) {
            param.put("phone", Utils.replaceBlank(mEtPhone.getText().toString()));
        } else {
            param.put("phone", mUserInfo != null ? mUserInfo.getPhone() : "");
        }
        param.put("area_code", mUserInfo != null ? mUserInfo.getArea_code() : "86");
        if (!mEtId.getText().toString().contains("*") && !TextUtils.isEmpty(mEtId.getText().toString())) {
            param.put("id_card", mEtId.getText().toString());
        }
        if (mLiveYear != 0) {
            param.put("birth_year", mLiveYear);
        }
        if (mLiveMonth != 0) {
            param.put("birth_month", mLiveMonth);
        }
        if (mLiveDay != 0) {
            param.put("birth_date", mLiveDay);
        }
        param.put("is_alive", mLive + "");
        param.put("sex", mSex + "");
        if (!TextUtils.isEmpty(mEtTag.getText().toString())) {
            param.put("mark", mEtTag.getText().toString());
        }
        if (!TextUtils.isEmpty(mFamily)) {
            param.put("family", mFamily);
        }
        if (!TextUtils.isEmpty(mGeneration)) {
            param.put("generation", mGeneration);
        }
        if (!TextUtils.isEmpty(mEtWord.getText().toString())) {
            param.put("zi", mEtWord.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtNativePlace.getText().toString())) {
            param.put("origin", mEtNativePlace.getText().toString());
        }
        if (mEducation != -1) {
            param.put("education", mEducation);
        }
        if (!TextUtils.isEmpty(mEtSchool.getText().toString())) {
            param.put("school", mEtSchool.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtOccupation.getText().toString())) {
            param.put("job", mEtOccupation.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtCompanyAddress.getText().toString())) {
            param.put("work_place", mEtCompanyAddress.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtIntroduction.getText().toString())) {
            param.put("brief", mEtIntroduction.getText().toString());
        }
        if (!TextUtils.isEmpty(mDieDate)) {
            param.put("pass_away_time", mDieDate);
        }
        if (mRanking != -1) {
            param.put("rank", mRanking);
        }
        if (!TextUtils.isEmpty(mEtCompany.getText().toString())) {
            param.put("company", mEtCompany.getText().toString());
        }
        if (!TextUtils.isEmpty(mEtAddress.getText().toString())) {
            param.put("address", mEtAddress.getText().toString());
        }
        HttpManager.getInstance().post("index/User/editUserInfoById", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                ToastUtil.showToast(msg);
                isChange = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closePage();
                    }
                }, 500);
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
                    getDialogFragment().show(getSupportFragmentManager(), "edit_progress");
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
     * 关闭页面
     */
    private void closePage() {
        Intent intent = new Intent();
        intent.putExtra("isChange", isChange);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        closePage();
    }

    /**
     * 权限检查
     * 打开通讯录获取联系人电话
     */
    private void getContact() {
        //**版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取**
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ContextCompat.checkSelfPermission() 方法 指定context和某个权限 返回PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
            if (ContextCompat.checkSelfPermission(PersonalEditActivity.this, android.Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                // 若不为GRANTED(即为DENIED)则要申请权限了
                // 申请权限 第一个为context 第二个可以指定多个请求的权限 第三个参数为请求码
                ActivityCompat.requestPermissions(PersonalEditActivity.this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        Utils.READ_REQUEST_CONTACTS_PERMISSIONS);
            } else {
                //权限已经被授予，在这里直接写要执行的相应方法即可
                intentToContact();
            }
        }else {
            // 低于6.0的手机直接访问
            intentToContact();
        }
    }

    /**
     * 用户权限 申请 的回调方法
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if(requestCode== Utils.READ_REQUEST_CONTACTS_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentToContact();
            } else {
                ToastUtil.showToast("授权被禁止，请前往设置打开权限");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    /**
     * 打开通讯录获取联系人电话
     */
    private void intentToContact() {
        // 跳转到联系人界面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, Utils.READ_REQUEST_CONTACTS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utils.READ_REQUEST_CONTACTS) {  //访问通讯录回调
            if (data != null) {
                Uri uri = data.getData();
                String phoneNum = null;
                String contactName = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = null;
                if (uri != null) {
                    cursor = contentResolver.query(uri,
                            new String[]{"display_name","data1"}, null, null, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        cursor.close();
                    }
                }
                //  把电话号码中的  -  符号 替换成空格
                if (phoneNum != null) {
                    phoneNum = phoneNum.replaceAll("-", " ");
//                    // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
//                    phoneNum= phoneNum.replaceAll(" ", "");
                }
                mEtPhone.setText(phoneNum);
            }
        } else if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
//                mIvImg.setImageBitmap(ImageUtils.getBitmap(picturePath));
                //压缩图片
                getResult(picturePath);
            }
        }
    }

    /**
     * 选择性别和是否存活
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()){
            case  R.id.rb_sex_man:
                mSex = 1;
                mEtRanking.setText("");
                break;

            case  R.id.rb_sex_woman:
                mSex = 2;
                mEtRanking.setText("");
                break;

            case  R.id.rb_live:
                mLive = 1;
                mLlDieDate.setVisibility(View.GONE);
                break;

            case  R.id.rb_die:
                mLive = 0;
                mLlDieDate.setVisibility(View.VISIBLE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserId = "";
        mUserInfo = null;
    }

    /**
     * 隐藏软键盘
     */
    private void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            if (imm != null && imm.isActive() && getCurrentFocus() != null) {
                //拿到view的token 不为空
                if (getCurrentFocus().getWindowToken() != null) {
                    //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
//        View view = getWindow().peekDecorView();
//        if (view != null) {
//            try {
//                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (inputmanger != null) {
//                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
