package family.li.aiyun.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

import java.util.HashMap;

/**
 * Created by keyC on 2019/6/15.
 * 保密设置
 */
public class SetSecrecyActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_secrecy);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mSwitch = (Switch) findViewById(R.id.switch_id);

        if (Utils.HIDE_INFO == 1) {
            mSwitch.setChecked(true);
        } else {
            mSwitch.setChecked(false);
        }

        mIvBack.setOnClickListener(this);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setSecrecy(1);
                }else {
                    setSecrecy(0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 设置保密设置
     */
    private void setSecrecy(final int type) {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("hidden_info", type + "");
        HttpManager.getInstance().post("index/Setting/privacy", param, new HttpRequestCallback() {

            @Override
            public void requestSuccess(Object data, String msg) {
                Utils.HIDE_INFO = type;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(msg);
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
                    getDialogFragment().show(getSupportFragmentManager(), "set_privacy_progress");
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
}
