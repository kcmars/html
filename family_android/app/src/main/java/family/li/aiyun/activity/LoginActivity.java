package family.li.aiyun.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.CountryCode;
import family.li.aiyun.bean.UserToken;
import family.li.aiyun.util.AppManager;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

import java.util.HashMap;
import java.util.TimerTask;

/**
 * Created by keyC on 2019/6/12.
 * 登录账户
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mLlCountry;
    private TextView mTvCountry;
    private TextView mTvCountryCode;
    private EditText mEtPhone;
    private EditText mEtPassword;
    private EditText mEtCode;
    private ImageView mIvLookPassword;
    private TextView mTvForgetPassword;
    private Button mBtnLogin;
    private Button mBtnGoRegister;
    private boolean showPassword = false;

    private TextView mTvChangeLoginMode;
    private LinearLayout mLlPasswrodLogin;
    private LinearLayout mLlCodeLogin;
    private boolean isPassword = true;

    private Button mBtnGetSmsCode;
    private ImageView mIvImgCode;

    private CountryCode mCountryCode; //国家地区

    private long mBackPressedTime = -1;  //双击退出

    private int mLastMillis = 60000;
    private static final int MSG_WHAT = 1001;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT:
                    if (mLastMillis > 0) {
                        setUsable(false);
                        mLastMillis -= 1000;
                        if (mBtnGetSmsCode != null) {
                            mHandler.sendEmptyMessageDelayed(MSG_WHAT, 1000);
                        }
                    } else {
                        setUsable(true);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLlCountry = (LinearLayout) findViewById(R.id.ll_country);
        mTvCountry = (TextView) findViewById(R.id.tv_country);
        mTvCountryCode = (TextView) findViewById(R.id.tv_country_code);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtCode = (EditText) findViewById(R.id.et_sms_code);
        mIvLookPassword = (ImageView) findViewById(R.id.iv_look_password);
        mTvChangeLoginMode = (TextView) findViewById(R.id.tv_change_login_mode);
        mTvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnGoRegister = (Button) findViewById(R.id.btn_go_register);
        mBtnGetSmsCode = (Button) findViewById(R.id.btn_get_sms_code);
        mLlPasswrodLogin = (LinearLayout) findViewById(R.id.ll_password_login);
        mLlCodeLogin = (LinearLayout) findViewById(R.id.ll_code_login);
        mIvImgCode = (ImageView) findViewById(R.id.iv_img_code);

        mLlCountry.setOnClickListener(this);
        mIvLookPassword.setOnClickListener(this);
        mTvChangeLoginMode.setOnClickListener(this);
        mTvForgetPassword.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mBtnGoRegister.setOnClickListener(this);
        mBtnGetSmsCode.setOnClickListener(this);

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
                if (s.length() == 13) {
                    mBtnGetSmsCode.setEnabled(true);
                } else {
                    mBtnGetSmsCode.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    mBtnLogin.setEnabled(true);
                } else {
                    mBtnLogin.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onClick(View v) {
        hintKeyBoard();
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_country: //选择地区国家
                intent.setClass(LoginActivity.this, CountryCodeActivity.class);
                startActivityForResult(intent, Utils.SELECT_COUNTRY);
                break;

            case R.id.iv_look_password: //查看密码
                if (showPassword) {
                    showPassword = false;
                    mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());  //密码不可见
                    mIvLookPassword.setImageResource(R.mipmap.icon_look_gray);
                } else {
                    showPassword = true;
                    mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  //密码可见
                    mIvLookPassword.setImageResource(R.mipmap.icon_look_open);
                }
                break;

            case R.id.tv_change_login_mode:  //切换密码验证码登录
                if (isPassword) {
                    isPassword = false;
                    mLlPasswrodLogin.setVisibility(View.GONE);
                    mLlCodeLogin.setVisibility(View.VISIBLE);
                    mTvChangeLoginMode.setText("用密码登录");
                } else {
                    isPassword = true;
                    mLlPasswrodLogin.setVisibility(View.VISIBLE);
                    mLlCodeLogin.setVisibility(View.GONE);
                    mTvChangeLoginMode.setText("用验证码登录");
                }
                break;

            case R.id.btn_get_sms_code:  //获取短信验证码
                getSmsCode();
                break;

            case R.id.iv_img_code:  //获取图形验证码
                getImgCode();
                break;

            case R.id.tv_forget_password:  //忘记密码
                intent.setClass(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_login:  //登录
                login();
                break;

            case R.id.btn_go_register:  //前往注册
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 检查手机号是否已注册
     */
    private void checkPhone() {
        String phone = Utils.replaceBlank(mEtPhone.getText().toString());
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号");
            return;
        }
        String area_code = (String) mTvCountryCode.getText();

        HashMap param = new HashMap();
        param.put("phone", phone);
        param.put("area_code", area_code);
        HttpManager.getInstance().post("index/login/isRegisted", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                mBtnGetSmsCode.setEnabled(true);
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
     * 获取验证码
     */
    private void getSmsCode() {
        String phone = Utils.replaceBlank(mEtPhone.getText().toString());
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号");
            return;
        }
        String area_code = Utils.getNumbers(mTvCountryCode.getText().toString()) + "";

        HashMap param = new HashMap();
        param.put("phone", phone);
        param.put("area_code", area_code);
        param.put("event", "login");
        param.put("captcha", "1");
        HttpManager.getInstance().post("index/login/getSmsCode", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                mHandler.sendEmptyMessage(MSG_WHAT);
                ToastUtil.showToast(msg);
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
     * 获取图形验证码
     */
    private void getImgCode() {
        HashMap param = new HashMap();
        HttpManager.getInstance().post("index/login/getImgCode", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
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
     * 设置是否可用
     * @param usable
     */
    private void setUsable(boolean usable) {
        if (mBtnGetSmsCode != null) {
            if (usable) {
                //可用
                if (!mBtnGetSmsCode.isClickable()) {
                    mBtnGetSmsCode.setClickable(true);
                    mBtnGetSmsCode.setBackground(getResources().getDrawable(R.drawable.btn_sms_code_bg));
                    mBtnGetSmsCode.setText("重新发送");
                    mLastMillis = 60000;
                }
            } else {
                //不可用
                if (mBtnGetSmsCode.isClickable()) {
                    mBtnGetSmsCode.setClickable(false);
                    mBtnGetSmsCode.setBackground(getResources().getDrawable(R.drawable.btn_gray_bg));
                }
                String content = mLastMillis / 1000 + "秒";
                mBtnGetSmsCode.setText(content);
            }
        }
    }

    /**
     * 登录
     */
    private void login() {
        String phone = Utils.replaceBlank(mEtPhone.getText().toString());
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号");
            return;
        }
        String area_code = Utils.getNumbers(mTvCountryCode.getText().toString()) + "";

        HashMap param = new HashMap();
        param.put("phone", phone);
        param.put("area_code", area_code);
        if (isPassword) {
            String pwd = mEtPassword.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                ToastUtil.showToast("请输入密码");
                return;
            }
            param.put("pwd", pwd);
        } else {
            String sms_code = mEtCode.getText().toString();
            if (TextUtils.isEmpty(sms_code)) {
                ToastUtil.showToast("请输入短信验证码");
                return;
            }
            param.put("sms_code", sms_code);
        }
        HttpManager.getInstance().post(isPassword ? "index/login/loginByPwd" : "index/login/loginBySms", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    UserToken userToken = new Gson().fromJson(json, new TypeToken<UserToken>(){}.getType());
                    Utils.TOKEN = Uri.encode(userToken.getToken());
                    Utils.savePreference(LoginActivity.this, "token", userToken.getToken());
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(LoginActivity.this, MainActivity.class));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }, 300);
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
                    getDialogFragment().show(getSupportFragmentManager(), "login_progress");
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Utils.SELECT_COUNTRY) {
                mCountryCode = (CountryCode) data.getSerializableExtra("country_code");
                if (mCountryCode != null) {
                    mTvCountry.setText(mCountryCode.getChinese());
                    mTvCountryCode.setText("+" + mCountryCode.getCode());
                }
            }
        }
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
