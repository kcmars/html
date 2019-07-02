package family.li.aiyun.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import family.li.aiyun.R;

public class PermissionsExplainActivity extends AppActivity implements View.OnClickListener {

    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions_explain);

        mIvBack = (ImageView) findViewById(R.id.iv_back);

        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:  //返回上一页
                finish();
                break;
        }
    }
}
