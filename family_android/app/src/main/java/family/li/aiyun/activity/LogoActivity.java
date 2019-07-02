package family.li.aiyun.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import family.li.aiyun.R;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

/**
 * Created by keyC on 2019/6/12.
 * logo
 */

public class LogoActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 点击桌面图标不重新启动app，直接进入
         */
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_logo);

        Utils.TOKEN = Utils.getPreference(LogoActivity.this, "token");

        //请求读取手机状态权限
        getPhoneStatus();

    }

    /**
     * 请求读取手机状态权限
     */
    private void getPhoneStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE )
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE },
                    Utils.READ_PHONE_STATE);
        } else {
            //进入主页
            startMain();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == Utils.READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                startMain();
            } else {
                // Permission Denied
                ToastUtil.showToast("未允许系统读取权限！请到系统设置中去打开");
                startMain();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPhoneStatus();
    }

    /**
     * 进入主页
     */
    private void startMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.TOKEN != null) {
                    Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }, 1000);
    }
}
