package family.li.aiyun.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.InstructionInfo;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;

import java.util.HashMap;

public class IntroductionActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvInfo = (TextView) findViewById(R.id.tv_info);

        mIvBack.setOnClickListener(this);

        //获取功能介绍内容
        getData();

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
     * 后去功能介绍内容
     */
    private void getData() {
        HashMap param = new HashMap();
        HttpManager.getInstance().post("index/Config/appInstruction", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                String json = new Gson().toJson(data);
                InstructionInfo info = new Gson().fromJson(json, new TypeToken<InstructionInfo>(){}.getType());;
                if (info != null) {
                    mTvInfo.setText(Html.fromHtml(info.getContent()));
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
}
