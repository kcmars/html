package family.li.aiyun.activity;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.sdk.android.push.AndroidPopupActivity;

import java.util.Map;

/**
 * Created by keyC on 2019/6/16.
 * 推送辅助弹窗
 */

public class PopupPushActivity extends AndroidPopupActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * 实现通知打开回调方法，获取通知相关信息
     * @param title     标题
     * @param summary   内容
     * @param extMap    额外参数
     */
    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {
        Log.i("TAG", "onSysNoticeOpened, title: " + title + ", content: " + summary + ", extMap: " + extMap);
    }
}