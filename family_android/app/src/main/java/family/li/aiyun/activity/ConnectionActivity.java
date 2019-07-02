package family.li.aiyun.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.FamilyMember;
import family.li.aiyun.bean.KinMember;
import family.li.aiyun.bean.KinMemberRecords;
import family.li.aiyun.bean.NewKinMember;
import family.li.aiyun.bean.NewKinMemberRecords;
import family.li.aiyun.bean.NewKinMemberRecords2;
import family.li.aiyun.bean.Person;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.JavaScriptInterface;
import family.li.aiyun.util.SystemUtils;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import static family.li.aiyun.LiApplication.getContext;

public class ConnectionActivity extends AppActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private TextView mIvExport;
    private WebView mWebView;

    private String mUserId1 = "";
    private String mUserId2 = "";

    private long timer = -1; //防止无限点击截屏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        if (getIntent().getStringExtra("user_id_1") != null) {
            mUserId1 = getIntent().getStringExtra("user_id_1");
        }
        if (getIntent().getStringExtra("user_id_2") != null) {
            mUserId2 = getIntent().getStringExtra("user_id_2");
        }

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvExport = (TextView) findViewById(R.id.tv_export);
        //步骤1. 定义Webview组件
        mWebView = (WebView) findViewById(R.id.web_view);

        mIvBack.setOnClickListener(this);
        mIvExport.setOnClickListener(this);

        //初始化webView
        initWebView();

        //获取两个人之间的关系链条
        getConnectionData();

    }

    /**
     * 初始化webView
     */
    @SuppressLint({"NewApi", "SetJavaScriptEnabled"})
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setTextZoom(100); //禁止缩放字体大小
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setUseWideViewPort(true);      //设置缩放后不会变形
        webSettings.setBuiltInZoomControls(true);     //设置可以缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        mWebView.setWebContentsDebuggingEnabled(true); //开启调试模式

        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");

        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

//        /**
//         * 读取json数据，并解析成对应的bean对象
//         */
//        String json = Utils.getJson("test.json", getContext());
//        //加载网页
//        loadWebView(json);
    }

    /**
     * 加载网页
     */
    @SuppressLint("AddJavascriptInterface")
    private void loadWebView(String json) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", json);
        bundle.putString("user_id", Utils.USER_ID + "");
        bundle.putString("url", Utils.WEB_TREE);
        mWebView.addJavascriptInterface(new JavaScriptInterface(getContext(), bundle), "LeeInterface");

        //步骤2. 选择加载方式(加载apk包中的html页面)
//        mWebView.loadUrl("file:///android_asset/www/error.html");
        mWebView.loadUrl(Utils.WEB_TREE);
    }

    /**
     * 重装数据
     */
    private void setKinMember(int root_id, List<KinMember> list) {
        KinMemberRecords records = new KinMemberRecords();
        List<FamilyMember> mList = new ArrayList<>();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                /**
                 * 先找到祖先id，然后在往下面添加孩子
                 */
                if (root_id == list.get(i).getUser_id()) {
                    FamilyMember familyMember = new FamilyMember();
                    /**
                     * 装自己的信息
                     */
                    Person self = null;
                    if (list.get(i) != null) {
                        self = new Person();
                        self.setId(list.get(i).getUser_id());                    //user_id
                        self.setName(list.get(i).getName());                      //姓名
                        self.setSex(list.get(i).getSex());                        //性别
                        self.setHead(list.get(i).getHead_img());                   //头像
                        self.setTag(Utils.USER_ID.equals(list.get(i).getUser_id() + "") ? "自己" : "");    //标记
                    }
                    /**
                     * 装配偶的信息
                     */
                    Person spouse = setSpouse(list, list.get(i));
                    /**
                     * 装孩子的信息
                     */
                    List<FamilyMember> child = setChild(list, self);
                    familyMember.setSelf(self);
                    familyMember.setSpouse(spouse);
                    familyMember.setChildren(child);
                    mList.add(familyMember);
                }
            }
        }
        records.setData(mList);
        String jsonStr = new Gson().toJson(records);
        loadWebView(jsonStr);
    }

    /**
     * 装配偶的信息
     * @param list  数据源
     * @param self    自己的信息
     */
    private Person setSpouse(List<KinMember> list, KinMember self) {
        if (list != null && self != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && self != null && self.getCouple_id() == list.get(i).getUser_id()) {
                    Person spouse = new Person();
                    spouse.setId(list.get(i).getUser_id());       //user_id
                    spouse.setName(list.get(i).getName());        //姓名
                    spouse.setSex(list.get(i).getSex());          //性别
                    spouse.setHead(list.get(i).getHead_img());    //头像
//                    spouse.setTag("配偶");                                           //标记
                    spouse.setTag(Utils.USER_ID.equals(list.get(i).getCouple_id() + "") ? (spouse.getSex() == 1 ? "丈夫" : "妻子") : "");
                    return spouse;
                }
            }
        }
        return null;
    }

    /**
     * 循环装孩子对象
     * 根据自己的 user_id 和 其他数据的 father_id 对比，如果一样，就表示其他数据是自己的孩子，依次循环
     * @param list   数据源
     * @param father  父亲数据
     */
    private List<FamilyMember> setChild(List<KinMember> list, Person father) {
        List<FamilyMember> list1 = new ArrayList<>();
        if (father != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && list.get(i).getNot_li() == 0 && father.getId() == list.get(i).getFather_id()) {
                    FamilyMember familyMember = new FamilyMember();
                    /**
                     * 装自己的信息
                     */
                    Person self = null;
                    if (list.get(i) != null) {
                        self = new Person();
                        self.setId(list.get(i).getUser_id());       //user_id
                        self.setName(list.get(i).getName());        //姓名
                        self.setSex(list.get(i).getSex());          //性别
                        self.setHead(list.get(i).getHead_img());    //头像
                        self.setTag(Utils.USER_ID.equals(list.get(i).getUser_id() + "") ? "自己" : "");                                         //标记
                    }
                    /**
                     * 装配偶的信息
                     */
                    Person spouse = setSpouse(list, list.get(i));
                    /**
                     * 装孩子的信息
                     */
                    //递归
                    List<FamilyMember> child1 = setChild(list, self);

                    familyMember.setSelf(self);
                    familyMember.setSpouse(spouse);
                    familyMember.setChildren(child1);
                    list1.add(familyMember);
                }
            }
        }
        return list1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:    //返回上一页
                finish();
                break;

            case R.id.tv_export:   //导出
                export();
                break;
        }
    }

    /**
     * 获取两个人之间的关系链条
     */
    private void getConnectionData() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("user_id1", mUserId1 + "");
        param.put("user_id2", mUserId2 + "");
        HttpManager.getInstance().post("index/Tree/getRelationLineNew", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    NewKinMemberRecords2 records = new Gson().fromJson(json, new TypeToken<NewKinMemberRecords2>(){}.getType());
                    int root_id = records.getRoot_id();
                    List<KinMember> list = records.getTree_data();
                    setKinMember(root_id, list);
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

    /**
     * 导出（截屏保存图片到相册）
     */
    private void export() {
        if (timer == -1) {
            timer = System.currentTimeMillis();
            if (SystemUtils.shotScreen(ConnectionActivity.this)) {
                ToastUtil.showToast("截屏保存成功！请到相册（族谱）中查看");
            } else {
                ToastUtil.showToast("截屏保存失败！版本过低，无法截屏");
            }
            new Handler().postDelayed(new TimerTask() {
                @Override
                public void run() {
                    timer = -1;
                }
            }, 5000);
        } else {
            ToastUtil.showToast("操作频繁！请稍后再试");
        }

    }
}
