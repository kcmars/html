package family.li.aiyun.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.LiApplication;
import family.li.aiyun.R;
import family.li.aiyun.adapter.SearchListRecycleViewAdapter;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.CountryCode;
import family.li.aiyun.bean.KinSearchMember;
import family.li.aiyun.bean.Person;
import family.li.aiyun.bean.RegisterPhoneInfo;
import family.li.aiyun.bean.SearchMemberRecords;
import family.li.aiyun.bean.UserInfo;
import family.li.aiyun.fragment.BaseFragment;
import family.li.aiyun.fragment.ClanFragment;
import family.li.aiyun.fragment.IntroduceFragment;
import family.li.aiyun.fragment.MeFragment;
import family.li.aiyun.fragment.NewsFragment;
import family.li.aiyun.listener.OnSearchItemClickListener;
import family.li.aiyun.util.AppManager;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.SystemUtils;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import family.li.aiyun.wxapi.WXEntryActivity;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static family.li.aiyun.LiApplication.getContext;
import static family.li.aiyun.LiApplication.iwxapi;
import static family.li.aiyun.LiApplication.mTencent;

public class MainActivity extends BaseActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private TabLayout mTabLayout;

    //首先需要先实例好n个全局Fragment
    private BaseFragment currentFragment = new BaseFragment();
    private ClanFragment mClanFragment;             //宗族
    private IntroduceFragment mIntroduceFragment;   //介绍
    private NewsFragment mNewsFragment;             //消息
    private MeFragment mMeFragment;                 //消息

    //点击三个点点的弹层
    private FrameLayout mFlMore;
    private TextView mTvSelf;
    private TextView mTvAdd;
    private TextView mTvExport;

    //点击搜索的弹层
    private FrameLayout mFlSearch;
    private EditText mEtText;
    private TextView mTvCancel;
    private SmartRefreshLayout mSrLayout;
    private RecyclerView mRlSearchList;

    private List<KinSearchMember> mSearchList = new ArrayList<>();
    private SearchListRecycleViewAdapter mAdapter;
    private TextView mTvNotMore;

    //点击人物的弹层
    private FrameLayout mFlDetail;
    private LinearLayout mLlInvitation;
    private LinearLayout mLlInter;
    private LinearLayout mLlShare;
    private LinearLayout mLlChain;
    private LinearLayout mLlLook;
    private LinearLayout mLlCenter;
    private LinearLayout mLlAdd;
    private LinearLayout mLlEdit;
    private ImageView mIvDetailHead;

    //点击添加人物的弹层
    private FrameLayout mFlAdd;
    private LinearLayout mLlAddFather;
    private LinearLayout mLlAddMother;
    private LinearLayout mLlAddSpouse1;
    private LinearLayout mLlAddSpouse2;
    private LinearLayout mLlAddSisters;
    private LinearLayout mLlAddSon;
    private LinearLayout mLlAddGirl;
    private ImageView mIvHead;
    private TextView mTvName;

    //点击编辑人物的弹层
    private FrameLayout mFlEdit;
    private TextView mTvOff;
    private TextView mTvOk;
    private TextView mTvEdit;
    private TextView mTvDelete;
    private TextView mTvCloseEdit;
    private LinearLayout mLlHint;

    //点击邀请成员的弹层
    private FrameLayout mFlInvitation;
    private LinearLayout mLlCountry;
    private TextView mTvInvitationText;
    private TextView mTvCountry;
    private TextView mTvCountryCode;
    private EditText mEtPhone;
    private Button mBtnCancelInvitation;
    private Button mBtnInvitation;
    private TextView mTvContact;   //打开通讯录

    private CountryCode mCountryCode; //国家地区

    //点击邀请成员--分享的弹层
    private FrameLayout mFlShare;
    private TextView mTvShareText;
    private ImageView mIvQQ;
    private ImageView mIvWX;
    private TextView mTvCancelShare;

    //如果自己的信息未完善弹窗提示
    private FrameLayout mFlPerfect;
    private TextView mTvCancelPerfect;
    private TextView mTvPerfect;

    private RegisterPhoneInfo mShareInfo; //邀请注册的分享内容

    private long mBackPressedTime = -1;  //双击退出
    public UserInfo mUserInfo;  //用户信息详情
    private int mPage = 1;  //分页
    private Person mBePerson; //选中的人员的信息

    private long timer = -1; //防止无限点击截屏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mClanFragment = (ClanFragment) getSupportFragmentManager().findFragmentByTag("mClanFragment");
            mIntroduceFragment = (IntroduceFragment) getSupportFragmentManager().findFragmentByTag("mIntroduceFragment");
            mNewsFragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag("mNewsFragment");
            mMeFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag("mMeFragment");
        }
        if (mClanFragment == null) {
            mClanFragment = ClanFragment.newInstance();
        }
        if (mIntroduceFragment == null) {
            mIntroduceFragment = IntroduceFragment.newInstance();
        }
        if (mNewsFragment == null) {
            mNewsFragment = NewsFragment.newInstance();
        }
        if (mMeFragment == null) {
            mMeFragment = MeFragment.newInstance();
        }
        setContentView(R.layout.activity_main);

        //初始化view
        initViews();

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        TabLayout.Tab tab1 = mTabLayout.newTab().setCustomView(R.layout.teb_layout_item1);
        TabLayout.Tab tab2 = mTabLayout.newTab().setCustomView(R.layout.teb_layout_item2);
        TabLayout.Tab tab3 = mTabLayout.newTab().setCustomView(R.layout.teb_layout_item3);
        TabLayout.Tab tab4 = mTabLayout.newTab().setCustomView(R.layout.teb_layout_item4);

        mTabLayout.addTab(tab1);
        mTabLayout.addTab(tab2);
        mTabLayout.addTab(tab3);
        mTabLayout.addTab(tab4);
        tab1.select();
        switchFragment(mClanFragment, "mClanFragment").commit();
        mTabLayout.addOnTabSelectedListener(this);

        mEtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    getSearchData();
                } else {
                    mPage = 1;
                    mRlSearchList.setVisibility(View.GONE);
                    mTvNotMore.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //获取用户详细信息
        getUserDetail();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取用户详细信息
        getUserDetail();
    }

    /**
     * 接受web点击事件
     */
    public void nodeClick(String string) {
        if (!TextUtils.isEmpty(string)) {
            mBePerson = new Gson().fromJson(string, new TypeToken<Person>(){}.getType());
            if (mBePerson != null) {
                if ((mBePerson.getId() + "").equals(Utils.USER_ID) && ("").equals(mBePerson.getName())) {
                    showPerfectLayer();
                } else {
                    showDetailLayer();
                }
            }
        }
    }

    /**
     * 获取用户详细信息
     */
    private void getUserDetail() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("user_id", Utils.USER_ID);     // -1代表获取当前用户信息
        HttpManager.getInstance().post("index/User/getDetail", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    mUserInfo = new Gson().fromJson(json, new TypeToken<UserInfo>(){}.getType());
                    if (mUserInfo != null) {
                        Utils.HIDE_INFO = mUserInfo.getHide_info();
                        Utils.ADMIN = mUserInfo.getIs_admin();
                        if (mUserInfo.getUser_profile() != null) {
                            Utils.USER_ID = mUserInfo.getUser_profile().getUser_id() + "";
                            Utils.USER_NAME = mUserInfo.getUser_profile().getName();
                            Utils.USER_HEAD_IMG = mUserInfo.getUser_profile().getHead_img();
                        }
                        if (mClanFragment != null) {
                            mClanFragment.upDateTree();
                        }
                        if (mMeFragment != null) {
                            mMeFragment.setUserInfo(mUserInfo);
                        }
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
     * 获取用户详情，fragment使用
     */
    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }

    /**
     * 获取搜索匹配数据
     */
    private void getSearchData() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("keyword", mEtText.getText().toString());
        param.put("page", mPage + "");
        param.put("page_size", "10");
        HttpManager.getInstance().post("index/User/search", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    SearchMemberRecords records  = new Gson().fromJson(json, new TypeToken<SearchMemberRecords>(){}.getType());
                    if (mPage == 1) {
                        mSearchList.clear();
                        if (records.getRecords().size() > 0) {
                            mRlSearchList.setVisibility(View.VISIBLE);
                            mTvNotMore.setVisibility(View.GONE);
                        } else {
                            mRlSearchList.setVisibility(View.GONE);
                            mTvNotMore.setVisibility(View.VISIBLE);
                        }
                    }
                    mSearchList.addAll(records.getRecords());
                    if (mAdapter == null) {
                        mAdapter = new SearchListRecycleViewAdapter(mSearchList, new OnSearchItemClickListener() {

                            @Override
                            public void onItemClick(KinSearchMember member) {
//                                mBePerson = new Person();
//                                mBePerson.setId(member.getUser_id());
//                                mBePerson.setName(member.getName());
                                lookDetail(member.getUser_id());
//                                addOrEdit(-2, false, 0, "个人资料");
                            }
                        });
                        mRlSearchList.setLayoutManager(new LinearLayoutManager(LiApplication.getContext()));
                        mRlSearchList.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                //设定底部边距为1px
                                outRect.set(0, 0, 0, 10);
                            }
                        });
                        mRlSearchList.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged(mSearchList);
                    }
                }
            }

            @Override
            public void requestFail(String msg) {
                ToastUtil.showToast(msg);
                if (mPage == 1) {
                    mSearchList.clear();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged(mSearchList);
                    }
                } else {
                    mPage--;
                }
            }

            @Override
            public void requestError(String msg) {
                mSrLayout.finishRefresh();
                mSrLayout.finishLoadmore();
                ToastUtil.showToast(msg);
                if (mPage == 1) {
                    mSearchList.clear();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged(mSearchList);
                    }
                } else {
                    mPage--;
                }
            }

            @Override
            public void requestStart() {

            }

            @Override
            public void requestComplete() {
                mSrLayout.finishRefresh();
                mSrLayout.finishLoadmore();
            }
        });
    }

    @Override
    public void onClick(View v) {
        hintKeyBoard();
        Intent intent = new Intent();
        mFlMore.setVisibility(View.GONE);
        mFlDetail.setVisibility(View.GONE);
        mFlAdd.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.fl_more:
                break;

            case R.id.tv_self:   //定位自己
                break;

            case R.id.tv_add:    //添加成员
                addOrEdit(0, true, 0, "添加成员");
                break;

            case R.id.tv_export:   //导出
                export();
                break;

            case R.id.tv_cancel:  //取消搜索
                mPage = 1;
                mEtText.setText("");
                mTvNotMore.setVisibility(View.GONE);
                mFlSearch.setVisibility(View.GONE);
                break;

            case R.id.fl_detail:  //
                break;

            case R.id.ll_invitation:  // 邀请name加入
                showInvitation();
                break;

            case R.id.ll_inter:  // 互动
                break;

            case R.id.ll_share:  // 分享
                intent.setClass(MainActivity.this, WXEntryActivity.class);
                intent.putExtra("name", mUserInfo.getUser_profile().getName());
                intent.putExtra("phone", mUserInfo.getPhone());
                startActivity(intent);
                break;

            case R.id.ll_chain:  // 关系链条
                if (mBePerson != null) {
                    intent.setClass(MainActivity.this, ConnectionActivity.class);
                    intent.putExtra("user_id_1", Utils.USER_ID + "");
                    intent.putExtra("user_id_2", mBePerson.getId() +"");
                    startActivity(intent);
                } else {
                    ToastUtil.showToast("选中人员信息不存在！");
                }
                break;

            case R.id.ll_look:  // 查看资料
                addOrEdit(-2, false, mBePerson.getSex(), "个人资料");
                break;

            case R.id.ll_center:  // 设为中心
                break;

            case R.id.ll_add:  // 添加关系
                showAddLayer();
                break;

            case R.id.ll_edit:  // 编辑删除
                showEditLayer();
                break;

            case R.id.fl_add:  //
                break;

            case R.id.ll_add_father:  // 添加父亲
                addOrEdit(1, true, 1, "添加父亲");
                break;

            case R.id.ll_add_mother:  // 添加母亲
                addOrEdit(2, true, 2, "添加母亲");
                break;

            case R.id.ll_add_spouse1:  // 添加配偶（男）
                addOrEdit(3, true, 1, "添加丈夫");
                break;

            case R.id.ll_add_spouse2:  // 添加配偶（女）
                addOrEdit(3, true, 2, "添加妻子");
                break;

            case R.id.ll_add_sisters:  // 添加兄弟
                addOrEdit(5, true, 0, "添加兄弟姐妹");
                break;

            case R.id.ll_add_son:  // 添加儿子
                addOrEdit(6, true, 1, "添加儿子");
                break;

            case R.id.ll_add_girl:  // 添加女儿
                addOrEdit(7, true, 2, "添加女儿");
                break;

            case R.id.tv_off:  //  关闭删除提示框
                mLlHint.setVisibility(View.GONE);
                break;

            case R.id.tv_ok:  //  确认删除
                delete();
                break;

            case R.id.tv_edit:  // 编辑
                mFlEdit.setVisibility(View.GONE);
                mLlHint.setVisibility(View.GONE);
                addOrEdit(-2, false, mBePerson.getSex(), "个人资料");
                break;

            case R.id.tv_delete:  // 删除
                mLlHint.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_close_edit:  // 取消编辑
                mFlEdit.setVisibility(View.GONE);
                mLlHint.setVisibility(View.GONE);
                break;

            case R.id.ll_country:  // 选择国家地区
                intent.setClass(MainActivity.this, CountryCodeActivity.class);
                startActivityForResult(intent, Utils.SELECT_COUNTRY);
                break;

            case R.id.tv_contact:  // 选择通讯录号码
                getContact();
                break;

            case R.id.btn_cancel_invitation:  // 取消邀请
                mEtPhone.setText("");
                mFlInvitation.setVisibility(View.GONE);
                break;

            case R.id.btn_invitation:  // 确定邀请
                isRegistedPhone();
                break;

            case R.id.iv_qq:  // 分享qq
                shareQQ();
                break;

            case R.id.iv_wx:  // 分享微信
                shareWX();
                break;

            case R.id.tv_cancel_share:  // 取消分享
                mFlShare.setVisibility(View.GONE);
                break;

            case R.id.tv_cancel_perfect:  // 取消完善自己的信息
                mFlPerfect.setVisibility(View.GONE);
                break;

            case R.id.tv_perfect:  // 完善自己的信息
                mFlPerfect.setVisibility(View.GONE);
                addOrEdit(-1, false, 0, "个人资料");
                break;

        }
    }

    /**
     * 分享到qq
     */
    private void shareQQ() {
        if (mShareInfo != null) {
            final Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, mShareInfo.getTitle());
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  mShareInfo.getDescription());
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareInfo.getImg_url());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  mShareInfo.getUrl());
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "李氏族谱");
            mTencent.shareToQQ(MainActivity.this, params, new MyIUiListener());
        } else {
            ToastUtil.showToast("获取分享内容失败");
        }
    }

    /**
     * 分享到微信
     */
    private void shareWX() {
        if (mShareInfo != null) {
            WXWebpageObject webpageObject = new WXWebpageObject();
            webpageObject.webpageUrl = mShareInfo.getUrl();
            WXMediaMessage msg = new WXMediaMessage(webpageObject);
            msg.title = mShareInfo.getTitle();
            msg.description = mShareInfo.getDescription();
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            msg.thumbData = bmpToByteArray(thumb, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("Req");
            req.message = msg;
            req.scene = WXSceneSession;
            iwxapi.sendReq(req);
        } else {
            ToastUtil.showToast("获取分享内容失败");
        }
    }

    /**
     * 导出（截屏保存图片到相册）
     */
    private void export() {
        if (timer == -1) {
            timer = System.currentTimeMillis();
            if (SystemUtils.shotScreen(MainActivity.this)) {
                ToastUtil.showToast("截屏保存成功！请到相册（族谱）中查看");
            } else {
                ToastUtil.showToast("截屏保存失败！版本过低，无法截屏");
            }
            new Handler().postDelayed(new TimerTask() {
                @Override
                public void run() {
                    timer = -1;
                }
            }, 5000);
        } else {
            ToastUtil.showToast("操作频繁！请稍后再试");
        }

    }

    /**
     * 显示自己一个人的时候，没有名字信息时，提示去完善信息弹窗
     */
    private void showPerfectLayer() {
        mFlPerfect.setVisibility(View.VISIBLE);
    }

    /**
     * 显示搜索弹层
     */
    public void showSearchLayer() {
        mFlSearch.setVisibility(View.VISIBLE);
    }

    /**
     * 显示更多弹层
     */
    public void showMoreLayer() {
        mFlMore.setVisibility(View.VISIBLE);
    }

    /**
     * 显示详情弹层
     */
    public void showDetailLayer() {
        if (mBePerson != null) {
            mFlDetail.setVisibility(View.VISIBLE);
            if (mClanFragment != null) {
                if (mClanFragment.isNear) { //近亲
                    if (Utils.USER_ID.equals(mBePerson.getId() + "")) {
                        mLlInter.setVisibility(View.GONE);
                        mLlShare.setVisibility(View.VISIBLE);
                        mLlChain.setVisibility(View.GONE);
                        mLlInvitation.setVisibility(View.GONE);
                        mLlLook.setVisibility(View.VISIBLE);
                        mLlCenter.setVisibility(View.VISIBLE);
                        mLlAdd.setVisibility(mBePerson.getNot_li() == 1 ? View.GONE : View.VISIBLE);
                        mLlEdit.setVisibility(View.VISIBLE);
                    } else {
                        mLlShare.setVisibility(View.GONE);
                        mLlInter.setVisibility(View.GONE);
                        mLlChain.setVisibility(View.VISIBLE);
                        mLlInvitation.setVisibility(View.VISIBLE);
                        mLlLook.setVisibility(View.VISIBLE);
                        mLlCenter.setVisibility(View.VISIBLE);
                        mLlAdd.setVisibility(mBePerson.getNot_li() == 1 ? View.GONE : View.VISIBLE);
                        mLlEdit.setVisibility(View.VISIBLE);
                    }
                } else {  //族亲
                    mLlInvitation.setVisibility(View.GONE);
                    mLlInter.setVisibility(View.GONE);
                    mLlShare.setVisibility(View.GONE);
                    mLlChain.setVisibility(View.VISIBLE);
                    mLlLook.setVisibility(View.VISIBLE);
                    mLlCenter.setVisibility(View.VISIBLE);
                    mLlAdd.setVisibility(View.GONE);
                    mLlEdit.setVisibility(View.GONE);
                }
                Glide.with(getContext())
                        .load(mBePerson.getHead())
                        .placeholder(mBePerson.getSex() == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                        .error(mBePerson.getSex() == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                        .fallback(mBePerson.getSex() == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(mIvDetailHead);
            }
        } else {
            ToastUtil.showToast("选中人员信息不存在！");
            mFlDetail.setVisibility(View.GONE);
        }
    }

    /**
     * 显示添加弹层
     */
    public void showAddLayer() {
        mFlAdd.setVisibility(View.VISIBLE);
        if (mBePerson != null) {
            mTvName.setText(mBePerson.getName());
            Glide.with(getContext())
                    .load(mBePerson.getHead())
                    .placeholder(mBePerson.getSex() == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                    .error(mBePerson.getSex() == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                    .fallback(mBePerson.getSex() == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(mIvHead);
            if (mBePerson.getSex() == 1) {
                mLlAddSpouse1.setVisibility(View.GONE);
                mLlAddSpouse2.setVisibility(View.VISIBLE);
                mLlAddFather.setVisibility(View.VISIBLE);
                mLlAddMother.setVisibility(View.VISIBLE);
                mLlAddSisters.setVisibility(View.VISIBLE);
                mLlAddSon.setVisibility(View.VISIBLE);
                mLlAddGirl.setVisibility(View.VISIBLE);
            } else {
                mLlAddSpouse2.setVisibility(View.GONE);
                mLlAddSpouse1.setVisibility(View.VISIBLE);
                mLlAddFather.setVisibility(View.VISIBLE);
                mLlAddMother.setVisibility(View.GONE);
                mLlAddSisters.setVisibility(View.VISIBLE);
                mLlAddSon.setVisibility(View.GONE);
                mLlAddGirl.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示编辑弹层
     */
    @SuppressLint("SetTextI18n")
    public void showEditLayer() {
        if (mBePerson != null) {
            mFlEdit.setVisibility(View.VISIBLE);
            mLlHint.setVisibility(View.GONE);
            mTvEdit.setText("编辑-" + mBePerson.getName());
            mTvDelete.setText("删除-" + mBePerson.getName());
        } else {
            ToastUtil.showToast("选中人员信息不存在！");
            mFlEdit.setVisibility(View.GONE);
            mLlHint.setVisibility(View.GONE);
        }
    }

    /**
     * 显示邀请弹层
     */
    @SuppressLint("SetTextI18n")
    public void showInvitation() {
        if (mBePerson != null) {
            mFlInvitation.setVisibility(View.VISIBLE);
            mTvInvitationText.setText("邀请-" + mBePerson.getTag() + "-" + mBePerson.getName());
        } else {
            ToastUtil.showToast("选中人员信息不存在！");
            mFlInvitation.setVisibility(View.GONE);
        }
    }

    /**
     * 显示分享弹层
     */
    @SuppressLint("SetTextI18n")
    public void showShare() {
        if (mBePerson != null) {
            mFlShare.setVisibility(View.VISIBLE);
            mTvShareText.setText("邀请-" + mBePerson.getTag() + "-" + mBePerson.getName());
        } else {
            ToastUtil.showToast("选中人员信息不存在！");
            mFlShare.setVisibility(View.GONE);
        }
    }

    /**
     * 删除成员信息
     */
    private void delete() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("delete_user_id", mBePerson.getId());
        param.put("delete_user_name", mBePerson.getName());
        HttpManager.getInstance().post("index/User/deleteRelative", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                mLlHint.setVisibility(View.GONE);
                mFlEdit.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mClanFragment != null) {
                            mClanFragment.upDateTree();
                        }
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
                    getDialogFragment().show(getSupportFragmentManager(), "delete_progress");
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
     * 查看user_id的信息
     */
    private void lookDetail(int user_id) {
        Intent intent = new Intent(MainActivity.this, PersonalLookActivity.class);
        intent.putExtra("user_id", user_id + "");
        startActivity(intent);
    }

    /**
     * 添加，编辑，查看成员信息
     * @param relation  操作状态或添加关系 -2 查看user_id的资料， -1 查看自己的资料， 0 添加无指定关系人员，
     *                                  1 添加父亲， 2 添加母亲， 3 添加配偶， 5 添加兄弟， 6 添加儿子， 7 添加女儿
     * @param b  是否添加新人 true 添加新人， false 编辑资料
     */
    private void addOrEdit(int relation, boolean b, int sex, String title) {
        if (mClanFragment != null && !mClanFragment.isNear) {
            lookDetail(mBePerson.getId());
        } else {
            Intent intent = new Intent();
            /**
             * 如果登录用户是管理员，才可进行编辑，否则只能查看
             */
            if (Utils.ADMIN == 0) {
                intent.setClass(MainActivity.this, PersonalEditActivity.class);
                intent.putExtra("user_relation", relation);
                intent.putExtra("user_id", mBePerson.getId() + "");
                intent.putExtra("first_edit", TextUtils.isEmpty(mBePerson.getName()) ? 1 : 0);
                intent.putExtra("is_add", b);
                if (sex != 0) {
                    intent.putExtra("user_sex", sex);
                }
                intent.putExtra("title", title);
            } else {
                intent.setClass(MainActivity.this, PersonalLookActivity.class);
                intent.putExtra("user_relation", relation);
                intent.putExtra("user_id", mBePerson.getId() + "");
                intent.putExtra("first_edit", TextUtils.isEmpty(mBePerson.getName()) ? 1 : 0);
                intent.putExtra("is_add", b);
                if (sex != 0) {
                    intent.putExtra("user_sex", sex);
                }
                intent.putExtra("title", title);
            }
            startActivityForResult(intent, Utils.PERSONAL_DATA);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Utils.PERSONAL_DATA && data != null) {
                if (data.getBooleanExtra("isChange", false)) {
                    getUserDetail();
                }
            } else if (requestCode == Utils.SELECT_COUNTRY) {
                mCountryCode = (CountryCode) data.getSerializableExtra("country_code");
                if (mCountryCode != null) {
                    mTvCountry.setText(mCountryCode.getChinese());
                    mTvCountryCode.setText("+" + mCountryCode.getCode());
                }
            }
        }
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
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                switchFragment(mClanFragment, "mClanFragment").commit();
                break;
            case 1:
                switchFragment(mIntroduceFragment, "mIntroduceFragment").commit();
                break;
            case 2:
                switchFragment(mNewsFragment, "mNewsFragment").commit();
                break;
            case 3:
                switchFragment(mMeFragment, "mMeFragment").commit();
                break;
            default:break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * 初始化view
     */
    private void initViews() {
        //点击三个点点的弹层
        mFlMore = (FrameLayout) findViewById(R.id.fl_more);
        mTvSelf = (TextView) findViewById(R.id.tv_self);
        mTvAdd = (TextView) findViewById(R.id.tv_add);
        mTvExport = (TextView) findViewById(R.id.tv_export);

        //点击搜索的弹层
        mFlSearch = (FrameLayout) findViewById(R.id.fl_search);
        mEtText = (EditText) findViewById(R.id.et_search);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mSrLayout = (SmartRefreshLayout) findViewById(R.id.sr_layout);
        mRlSearchList = (RecyclerView) findViewById(R.id.rl_search_list);
        mTvNotMore = (TextView) findViewById(R.id.tv_not_more);

        mFlMore.setOnClickListener(this);
        mTvSelf.setOnClickListener(this);
        mTvAdd.setOnClickListener(this);
        mTvExport.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);

        mSrLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 1;
                getSearchData();
            }
        });
        mSrLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                getSearchData();
            }
        });

        //点击人物的弹层
        mFlDetail = (FrameLayout) findViewById(R.id.fl_detail);
        mLlInvitation = (LinearLayout) findViewById(R.id.ll_invitation);
        mLlInter = (LinearLayout) findViewById(R.id.ll_inter);
        mLlShare = (LinearLayout) findViewById(R.id.ll_share);
        mLlChain = (LinearLayout) findViewById(R.id.ll_chain);
        mLlLook = (LinearLayout) findViewById(R.id.ll_look);
        mLlCenter = (LinearLayout) findViewById(R.id.ll_center);
        mLlAdd = (LinearLayout) findViewById(R.id.ll_add);
        mLlEdit = (LinearLayout) findViewById(R.id.ll_edit);
        mIvDetailHead = (ImageView) findViewById(R.id.iv_detail_head);

        mFlDetail.setOnClickListener(this);
        mLlInvitation.setOnClickListener(this);
        mLlInter.setOnClickListener(this);
        mLlShare.setOnClickListener(this);
        mLlChain.setOnClickListener(this);
        mLlLook.setOnClickListener(this);
        mLlCenter.setOnClickListener(this);
        mLlAdd.setOnClickListener(this);
        mLlEdit.setOnClickListener(this);

        //点击添加人物的弹层
        mFlAdd = (FrameLayout) findViewById(R.id.fl_add);
        mLlAddFather = (LinearLayout) findViewById(R.id.ll_add_father);
        mLlAddMother = (LinearLayout) findViewById(R.id.ll_add_mother);
        mLlAddSpouse1 = (LinearLayout) findViewById(R.id.ll_add_spouse1);
        mLlAddSpouse2 = (LinearLayout) findViewById(R.id.ll_add_spouse2);
        mLlAddSisters = (LinearLayout) findViewById(R.id.ll_add_sisters);
        mLlAddSon = (LinearLayout) findViewById(R.id.ll_add_son);
        mLlAddGirl = (LinearLayout) findViewById(R.id.ll_add_girl);
        mIvHead = (ImageView) findViewById(R.id.iv_head);
        mTvName = (TextView) findViewById(R.id.tv_name);

        mFlAdd.setOnClickListener(this);
        mLlAddFather.setOnClickListener(this);
        mLlAddMother.setOnClickListener(this);
        mLlAddSpouse1.setOnClickListener(this);
        mLlAddSpouse2.setOnClickListener(this);
        mLlAddSisters.setOnClickListener(this);
        mLlAddSon.setOnClickListener(this);
        mLlAddGirl.setOnClickListener(this);

        //点击编辑人物的弹层
        mFlEdit = (FrameLayout) findViewById(R.id.fl_edit);
        mTvOff = (TextView) findViewById(R.id.tv_off);
        mTvOk = (TextView) findViewById(R.id.tv_ok);
        mTvEdit = (TextView) findViewById(R.id.tv_edit);
        mTvDelete = (TextView) findViewById(R.id.tv_delete);
        mTvCloseEdit = (TextView) findViewById(R.id.tv_close_edit);
        mLlHint = (LinearLayout) findViewById(R.id.ll_hint);

        mTvOff.setOnClickListener(this);
        mTvOk.setOnClickListener(this);
        mTvEdit.setOnClickListener(this);
        mTvDelete.setOnClickListener(this);
        mTvCloseEdit.setOnClickListener(this);

        //点击邀请成员的弹层
        mFlInvitation = (FrameLayout) findViewById(R.id.fl_invitation);
        mLlCountry = (LinearLayout) findViewById(R.id.ll_country);
        mTvInvitationText = (TextView) findViewById(R.id.tv_invitation_text);
        mTvCountry = (TextView) findViewById(R.id.tv_country);
        mTvCountryCode = (TextView) findViewById(R.id.tv_country_code);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mBtnCancelInvitation = (Button) findViewById(R.id.btn_cancel_invitation);
        mBtnInvitation = (Button) findViewById(R.id.btn_invitation);
        mTvContact = (TextView) findViewById(R.id.tv_contact);

        mLlCountry.setOnClickListener(this);
        mBtnCancelInvitation.setOnClickListener(this);
        mBtnInvitation.setOnClickListener(this);
        mTvContact.setOnClickListener(this);

        //点击邀请成员--分享的弹层
        mFlShare = (FrameLayout) findViewById(R.id.fl_share);
        mTvShareText = (TextView) findViewById(R.id.tv_share_text);
        mIvQQ = (ImageView) findViewById(R.id.iv_qq);
        mIvWX = (ImageView) findViewById(R.id.iv_wx);
        mTvCancelShare = (TextView) findViewById(R.id.tv_cancel_share);

        mIvQQ.setOnClickListener(this);
        mIvWX.setOnClickListener(this);
        mTvCancelShare.setOnClickListener(this);

        //如果自己的信息未完善弹窗提示
        mFlPerfect = (FrameLayout) findViewById(R.id.fl_perfect);
        mTvCancelPerfect = (TextView) findViewById(R.id.tv_cancel_perfect);
        mTvPerfect = (TextView) findViewById(R.id.tv_perfect);

        mTvCancelPerfect.setOnClickListener(this);
        mTvPerfect.setOnClickListener(this);

    }

    /**
     * 判断邀请人的手机号是否注册
     */
    private void isRegistedPhone() {
        String phone = Utils.replaceBlank(mEtPhone.getText().toString());
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入被邀请人的手机号");
            return;
        }
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("area_code_name", mTvCountry.getText());
        param.put("area_code", Utils.getNumbers(mTvCountryCode.getText().toString()));
        param.put("phone", Utils.replaceBlank(mEtPhone.getText().toString()));
        param.put("invite_user_id", Utils.USER_ID);
        param.put("invited_id", mBePerson.getId());
        param.put("invited_name", mBePerson.getName());
        HttpManager.getInstance().post("index/Invite/isRegistedPhone", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    mShareInfo = new Gson().fromJson(json, new TypeToken<RegisterPhoneInfo>(){}.getType());
                    if (mShareInfo.getNeed_register() == 0) {
                        mEtPhone.setText("");
                        mFlInvitation.setVisibility(View.GONE);
                        ToastUtil.showToast(mShareInfo.getMessage());
                    } else {
                        mEtPhone.setText("");
                        mFlInvitation.setVisibility(View.GONE);
                        showShare();
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
                if (getDialogFragment() != null) {
                    getDialogFragment().show(getSupportFragmentManager(), "is_register_phone_progress");
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

    @Override
    public void onBackPressed() {
        if (mBackPressedTime == -1) {
            mBackPressedTime = System.currentTimeMillis();
            ToastUtil.showToast("再次点击退出");
            new Handler().postDelayed(new TimerTask() {
                @Override
                public void run() {
                    mBackPressedTime = -1;
                }
            }, 2000);
        } else if (System.currentTimeMillis() - mBackPressedTime < 2000) {
            AppManager.getAppManager().AppExit(this);
        }
    }

    /**
     * 权限检查
     * 打开通讯录获取联系人电话
     */
    private void getContact() {
        //**版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取**
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ContextCompat.checkSelfPermission() 方法 指定context和某个权限 返回PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                // 若不为GRANTED(即为DENIED)则要申请权限了
                // 申请权限 第一个为context 第二个可以指定多个请求的权限 第三个参数为请求码
                ActivityCompat.requestPermissions(MainActivity.this,
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

    /**
     * 隐藏软键盘
     */
    @SuppressLint("NewApi")
    private void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm != null && imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    private FragmentTransaction switchFragment(BaseFragment targetFragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fl_content, targetFragment, tag);
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        return transaction;
    }

    /**
     * qq分享回调实现
     */
    class MyIUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            // 操作成功
            Log.i("TAG", "share-qq-onComplete: " + o.toString());
        }
        @Override
        public void onError(UiError uiError) {
            // 分享异常
            Log.i("TAG", "share-qq-onComplete: " + uiError.errorMessage);
            Log.i("TAG", "share-qq-onComplete: " + uiError.errorDetail);
            Log.i("TAG", "share-qq-onComplete: " + uiError.errorCode);
        }
        @Override
        public void onCancel() {
            // 取消分享
            Log.i("TAG", "share-qq-onComplete: " );
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
