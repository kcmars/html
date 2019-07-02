package family.li.aiyun.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import family.li.aiyun.fragment.ProgressFragment;

/**
 * Created by keyC on 2019/6/19.
 * BaseActivity
 */

@SuppressLint("Registered")
public class BaseActivity extends AppActivity {

    public ProgressFragment mDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogFragment = new ProgressFragment();
    }

    public ProgressFragment getDialogFragment() {
        return mDialogFragment;
    }
}
