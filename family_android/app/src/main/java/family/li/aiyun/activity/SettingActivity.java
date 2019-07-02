package family.li.aiyun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

import java.util.HashMap;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private LinearLayout mLlAbout;
    private LinearLayout mLlSecrecySetting;
    private LinearLayout mLlChangePassword;
    private TextView mTvLogout;

    //修改密码弹窗
    private FrameLayout mFlPassword;
    private EditText mEtPassword1;
    private EditText mEtPassword2;
    private TextView mTvCancel;
    private TextView mTvOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mLlAbout = (LinearLayout) findViewById(R.id.ll_about);
        mLlSecrecySetting = (LinearLayout) findViewById(R.id.ll_secrecy_setting);
        mLlChangePassword = (LinearLayout) findViewById(R.id.ll_change_password);
        mTvLogout = (TextView) findViewById(R.id.tv_logout);
        mFlPassword = (FrameLayout) findViewById(R.id.fl_password);
        mEtPassword1 = (EditText) findViewById(R.id.et_password1);
        mEtPassword2 = (EditText) findViewById(R.id.et_password2);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mTvOk = (TextView) findViewById(R.id.tv_ok);


        mIvBack.setOnClickListener(this);
        mLlAbout.setOnClickListener(this);
        mLlSecrecySetting.setOnClickListener(this);
        mLlChangePassword.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mTvOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        hintKeyBoard();
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:  //返回上一页
                finish();
                break;

            case R.id.ll_about:  //关于
                intent.setClass(SettingActivity.this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_secrecy_setting:  //保密设置
                intent.setClass(SettingActivity.this, SetSecrecyActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_change_password:  //修改密码
                mFlPassword.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_logout:  //退出登录
                logout();
                break;

            case R.id.tv_cancel:  //取消修改
                mEtPassword1.setText("");
                mEtPassword2.setText("");
                mFlPassword.setVisibility(View.GONE);
                break;

            case R.id.tv_ok:  //确定修改
                changePassword();
                break;
        }
    }

    /**
     * 修改密码
     */
    private void changePassword() {
        String oldPassword = mEtPassword1.getText().toString();
        if (TextUtils.isEmpty(oldPassword)) {
            ToastUtil.showToast("请输入旧密码");
            return;
        }
        String newPassword = mEtPassword2.getText().toString();
        if (TextUtils.isEmpty(newPassword)) {
            ToastUtil.showToast("请输入旧密码");
            return;
        }
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("old", oldPassword);
        param.put("new", newPassword);
        HttpManager.getInstance().post("index/Setting/resetPassword", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(msg);
                        mFlPassword.setVisibility(View.GONE);
                        mEtPassword1.setText("");
                        mEtPassword2.setText("");
                    }
                }, 500);
            }

            @Override
            public void requestFail(String msg) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(msg);
                        mEtPassword1.setText("");
                        mEtPassword2.setText("");
                        mEtPassword1.requestFocus();
                    }
                }, 500);
            }

            @Override
            public void requestError(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestStart() {
                if (getDialogFragment() != null) {
                    getDialogFragment().show(getSupportFragmentManager(), "change_password_progress");
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
     * 退出登录
     * 清除登录信息，回到登录界面
     */
    private void logout() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        HttpManager.getInstance().post("index/login/logout", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                Utils.TOKEN = null;
                Utils.USER_ID = "-1"; //用户USER_ID
                Utils.USER_NAME = ""; //用户姓名
                Utils.USER_HEAD_IMG = ""; //用户头像
                Utils.ADMIN = 0; //是否属于管理员（0 否， 1 是）
                Utils.HIDE_INFO = 0; //是否隐藏用户更多信息（0 隐藏， 1 显示）
                Utils.clearPreference(SettingActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);
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
                    getDialogFragment().show(getSupportFragmentManager(), "logout_progress");
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
     * 隐藏软键盘
     */
    private void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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
}
