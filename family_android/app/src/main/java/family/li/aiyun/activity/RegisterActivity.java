package family.li.aiyun.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

import java.util.HashMap;

/**
 * Created by keyC on 2019/6/12.
 * 注册账户
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mLlCountry;
    private TextView mTvCountry;
    private TextView mTvCountryCode;
    private EditText mEtPhone;
    private EditText mEtSmsCode;
    private Button mBtnGetSmsCode;
    private ImageView mIvGetImgCode;
    private ImageView mIvLookPassword;
    private EditText mEtPassword;
    private Button mBtnRegister;
    private Button mBtnGoLogin;

    private boolean showPassword = false; //是否显示密码

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
        setContentView(R.layout.activity_register);

        mLlCountry = (LinearLayout) findViewById(R.id.ll_country);
        mTvCountry = (TextView) findViewById(R.id.tv_country);
        mTvCountryCode = (TextView) findViewById(R.id.tv_country_code);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtSmsCode = (EditText) findViewById(R.id.et_sms_code);
        mBtnGetSmsCode = (Button) findViewById(R.id.btn_get_sms_code);
        mIvGetImgCode = (ImageView) findViewById(R.id.iv_img_code);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mIvLookPassword = (ImageView) findViewById(R.id.iv_look_password);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnGoLogin = (Button) findViewById(R.id.btn_go_login);

        mLlCountry.setOnClickListener(this);
        mBtnGetSmsCode.setOnClickListener(this);
        mIvGetImgCode.setOnClickListener(this);
        mIvLookPassword.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mBtnGoLogin.setOnClickListener(this);

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
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_country:  //选择地区国家
                intent.setClass(RegisterActivity.this, CountryCodeActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_get_sms_code:  //获取短信验证码
                getSmsCode();
                break;

            case R.id.iv_img_code:  //获取图形验证码
                getImgCode();
                break;

            case R.id.iv_look_password:  //显示隐藏密码
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

            case R.id.btn_register:  //注册
                register();
                break;

            case R.id.btn_go_login:  //去登录
                finish();
                break;


        }
    }

    /**
     * 获取验证码
     */
    private void getSmsCode(){
        String phone = Utils.replaceBlank(mEtPhone.getText().toString());
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号");
            return;
        }
        String area_code = Utils.getNumbers(mTvCountryCode.getText().toString()) + "";

        HashMap param = new HashMap();
        param.put("phone", phone);
        param.put("area_code", area_code);
        param.put("event", "register");
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
        HttpManager.getInstance().post("index/login/getCaptcha", param, new HttpRequestCallback() {
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
     * 注册
     */
    private void register() {
        String phone = Utils.replaceBlank(mEtPhone.getText().toString());
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号");
            return;
        }
        String area_code = Utils.getNumbers(mTvCountryCode.getText().toString()) + "";

        String sms_code = mEtSmsCode.getText().toString().trim();
        if (TextUtils.isEmpty(sms_code)) {
            ToastUtil.showToast("请输入短信验证码");
            return;
        }

        String pwd = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(sms_code)) {
            ToastUtil.showToast("请输入注册密码");
            return;
        }
        HashMap param = new HashMap();
        param.put("phone", phone);
        param.put("area_code", area_code);
        param.put("sms_code", sms_code);
        param.put("pwd", pwd);
        HttpManager.getInstance().post("index/login/register", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                ToastUtil.showToast(msg);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
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
                    getDialogFragment().show(getSupportFragmentManager(), "register_progress");
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
}
