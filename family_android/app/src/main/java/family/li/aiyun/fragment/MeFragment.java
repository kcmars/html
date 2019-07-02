package family.li.aiyun.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import family.li.aiyun.R;
import family.li.aiyun.activity.LogcatActivity;
import family.li.aiyun.activity.MainActivity;
import family.li.aiyun.activity.PermissionActivity;
import family.li.aiyun.activity.PersonalEditActivity;
import family.li.aiyun.activity.SettingActivity;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.UserInfo;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static family.li.aiyun.LiApplication.mContext;

/**
 * Created by keyC on 2019/6/15.
 * 我的
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout mLlSelfInfo;
    private ImageView mIvHead;
    private TextView mTvName;
    private TextView mTvAccount;
    private LinearLayout mLlToPermission;
    private LinearLayout mLlToLogcat;
    private LinearLayout mLlToSetting;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mLlSelfInfo = (LinearLayout) view.findViewById(R.id.ll_self_info);
        mIvHead = (ImageView) view.findViewById(R.id.iv_head);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvAccount = (TextView) view.findViewById(R.id.tv_account);
        mLlToPermission = (LinearLayout) view.findViewById(R.id.ll_to_permission);
        mLlToLogcat = (LinearLayout) view.findViewById(R.id.ll_to_logcat);
        mLlToSetting = (LinearLayout) view.findViewById(R.id.ll_to_setting);

        mLlSelfInfo.setOnClickListener(this);
        mLlToPermission.setOnClickListener(this);
        mLlToLogcat.setOnClickListener(this);
        mLlToSetting.setOnClickListener(this);

        Glide.with(mContext)
                .load(R.mipmap.icon_man_orange_circle)
                .placeholder(R.mipmap.icon_man_gray_circle)
                .error(R.mipmap.icon_man_gray_circle)
                .fallback(R.mipmap.icon_man_gray_circle)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(mIvHead);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("TAG", "setUserInfo23232: ");
        setUserInfo(((MainActivity) getActivity()).getUserInfo());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setUserInfo(((MainActivity) getActivity()).getUserInfo());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setUserInfo(((MainActivity) getActivity()).getUserInfo());
        }
    }

    /**
     * 设置个人信息
     */
    @SuppressLint("SetTextI18n")
    public void setUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            //头像
            if (mIvHead != null) {
                int sex = 1;
                if (userInfo.getUser_relation() != null) {
                    sex = userInfo.getUser_relation().getSex();
                }
                if (userInfo.getUser_profile() != null && !TextUtils.isEmpty(userInfo.getUser_profile().getHead_img())) {
                    Glide.with(mContext)
                            .load(userInfo.getUser_profile().getHead_img())
                            .placeholder(sex == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                            .error(sex == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                            .fallback(sex == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                            .bitmapTransform(new CropCircleTransformation(getContext()))
                            .into(mIvHead);
                } else {
                    Glide.with(mContext)
                            .load(sex == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                            .placeholder(sex == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                            .error(sex == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                            .fallback(sex == 1 ? R.mipmap.icon_man_orange_circle : R.mipmap.icon_woman_orange_circle)
                            .bitmapTransform(new CropCircleTransformation(getContext()))
                            .into(mIvHead);
                }
            }
            //姓名
            if (mTvName != null && userInfo.getUser_profile() != null) {
                mTvName.setText(userInfo.getUser_profile().getName());
            }
            //账号
            if (mTvAccount != null) {
                mTvAccount.setText("账号：" + userInfo.getPhone_display());
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_self_info:   //个人信息
                intent.setClass(getContext(), PersonalEditActivity.class);
                intent.putExtra("title", "个人资料");
                intent.putExtra("user_id", Utils.USER_ID);
                intent.putExtra("is_add", false);
                try {
                    if (!TextUtils.isEmpty(Utils.USER_NAME)) {
                        intent.putExtra("user_sex", ((MainActivity) getActivity()).getUserInfo().getUser_profile().getSex());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("first_edit", TextUtils.isEmpty(Utils.USER_NAME) ? 1 : 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((MainActivity) getActivity()).startActivityForResult(intent, Utils.PERSONAL_DATA);
                break;

            case R.id.ll_to_permission:  //权限管理
                intent.setClass(getContext(), PermissionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.ll_to_logcat:    //日志信息
                intent.setClass(getContext(), LogcatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.ll_to_setting:   //设置
                intent.setClass(getContext(), SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
