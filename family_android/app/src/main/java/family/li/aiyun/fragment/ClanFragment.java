package family.li.aiyun.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.R;
import family.li.aiyun.activity.MainActivity;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.FamilyMember;
import family.li.aiyun.bean.KinMemberRecords;
import family.li.aiyun.bean.NewKinMember;
import family.li.aiyun.bean.NewKinMemberRecords;
import family.li.aiyun.bean.Person;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.JavaScriptInterface;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by keyC on 2019/6/15.
 * 宗族
 */

public class ClanFragment extends BaseFragment implements View.OnClickListener {

    private Button mBtnNear;
    private Button mBtnKin;
    private LinearLayout mLLSearch;
    private ImageView mIvMore;
    private WebView mWebView;

    public boolean isNear = true; //是否加载近亲树图

    public ClanFragment() {
        // Required empty public constructor
    }

    public static ClanFragment newInstance() {
        ClanFragment fragment = new ClanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clan, container, false);
        mBtnNear = (Button) view.findViewById(R.id.btn_near);
        mBtnKin = (Button) view.findViewById(R.id.btn_kin);
        mLLSearch = (LinearLayout) view.findViewById(R.id.ll_search);
        mIvMore = (ImageView) view.findViewById(R.id.iv_more);
        //步骤1. 定义Webview组件
        mWebView = (WebView) view.findViewById(R.id.web_view);

        mBtnNear.setOnClickListener(this);
        mBtnKin.setOnClickListener(this);
        mLLSearch.setOnClickListener(this);
        mIvMore.setOnClickListener(this);

        //初始化webView
        initWebView();

        //获取近亲树形结构
        getNearTreeData();


        return view;
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
    private void loadWebView(int current_id, String json) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", json);
        bundle.putString("user_id", current_id + "");
        bundle.putString("url", Utils.WEB_TREE);
        mWebView.addJavascriptInterface(new JavaScriptInterface(getContext(), bundle), "LeeInterface");

        //步骤2. 选择加载方式(加载apk包中的html页面)
//        mWebView.loadUrl("file:///android_asset/www/error.html");
        mWebView.loadUrl(Utils.WEB_TREE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:   //搜索
                try {
                    ((MainActivity) getActivity()).showSearchLayer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_near:   //近亲
                isNear = true;
                //获取近亲树形结构
                getNearTreeData();
                mBtnNear.setEnabled(false);
                mBtnKin.setEnabled(true);
                break;

            case R.id.btn_kin:    //族亲
                //获取族亲树形结构
                isNear = false;
                getKinTreeData();
                mBtnNear.setEnabled(true);
                mBtnKin.setEnabled(false);
                break;

            case R.id.iv_more:    //更多功能
                try {
                    ((MainActivity) getActivity()).showMoreLayer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 重装数据
     */
    private void setKinMember(int root_id, int current_id, List<NewKinMember> list) {
        KinMemberRecords records = new KinMemberRecords();
        List<FamilyMember> mList = new ArrayList<>();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                /**
                 * 先找到祖先id，然后在往下面添加孩子
                 */
                if (root_id == list.get(i).getId()) {
                    FamilyMember familyMember = new FamilyMember();
                    /**
                     * 装自己的信息
                     */
                    Person self = null;
                    if (list.get(i).getUser_profile() != null) {
                        self = new Person();
                        self.setId(list.get(i).getUser_profile().getUser_id());                    //user_id
                        self.setName(list.get(i).getUser_profile().getName());                      //姓名
                        self.setSex(list.get(i).getUser_relation().getSex());                        //性别
                        self.setHead(list.get(i).getUser_profile().getHead_img());                   //头像
                        self.setTag(Utils.USER_ID.equals(list.get(i).getId() + "") ? "自己" : "");    //标记
                        self.setNot_li(list.get(i).getUser_relation().getNot_li());                  //是否李家人
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
        loadWebView(current_id, jsonStr);
    }

    /**
     * 装配偶的信息
     * @param list  数据源
     * @param self    自己的信息
     */
    private Person setSpouse(List<NewKinMember> list, NewKinMember self) {
        if (list != null && self != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUser_profile() != null && self.getUser_relation() != null && self.getUser_relation().getCouple_id() == list.get(i).getId()) {
                    Person spouse = new Person();
                    spouse.setId(list.get(i).getUser_profile().getUser_id());       //user_id
                    spouse.setName(list.get(i).getUser_profile().getName());        //姓名
                    spouse.setSex(list.get(i).getUser_relation().getSex());          //性别
                    spouse.setHead(list.get(i).getUser_profile().getHead_img());    //头像
//                    spouse.setTag("配偶");                                           //标记
                    spouse.setTag(Utils.USER_ID.equals(list.get(i).getUser_relation().getCouple_id() + "") ? (spouse.getSex() == 1 ? "丈夫" : "妻子") : "");
                    spouse.setNot_li(list.get(i).getUser_relation().getNot_li());
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
    private List<FamilyMember> setChild(List<NewKinMember> list, Person father) {
        List<FamilyMember> list1 = new ArrayList<>();
        if (father != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUser_relation() != null && list.get(i).getUser_relation().getNot_li() == 0 && father.getId() == list.get(i).getUser_relation().getFather_id()) {
                    FamilyMember familyMember = new FamilyMember();
                    /**
                     * 装自己的信息
                     */
                    Person self = null;
                    if (list.get(i).getUser_profile() != null) {
                        self = new Person();
                        self.setId(list.get(i).getUser_profile().getUser_id());       //user_id
                        self.setName(list.get(i).getUser_profile().getName());        //姓名
                        self.setSex(list.get(i).getUser_relation().getSex());          //性别
                        self.setHead(list.get(i).getUser_profile().getHead_img());    //头像
                        self.setTag(Utils.USER_ID.equals(list.get(i).getId() + "") ? "自己" : "");   //标记
                        self.setNot_li(list.get(i).getUser_relation().getNot_li());
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

    /**
     * 获取近亲树形结构
     */
    private void getNearTreeData() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("i_need_tree_please", "1");
        HttpManager.getInstance().post("index/Tree/getCloseRelative", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    NewKinMemberRecords records = new Gson().fromJson(json, new TypeToken<NewKinMemberRecords>(){}.getType());
                    int root_id = records.getRoot_id();
                    int current_id = records.getCurrent_user_id();
                    List<NewKinMember> list = records.getTree_data();
                    setKinMember(root_id, current_id, list);
//                    KinMember kinMember = new Gson().fromJson(json, new TypeToken<KinMember>(){}.getType());
//                    KinMemberRecords records = new KinMemberRecords();
//                    List<KinMember> list = new ArrayList<>();
//                    list.add(kinMember);
//                    records.setData(list);
//
//                    String jsonStr = new Gson().toJson(records);
//                    loadWebView(jsonStr);
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
                try {
                    if (((MainActivity)getActivity()) != null) {
                        if (((MainActivity)getActivity()).getDialogFragment() != null) {
                            ((MainActivity)getActivity()).getDialogFragment().show(((MainActivity)getActivity()).getSupportFragmentManager(), "get_near_tree_progress");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestComplete() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (((MainActivity)getActivity()) != null) {
                                if (((MainActivity)getActivity()).getDialogFragment() != null) {
                                    ((MainActivity)getActivity()).getDialogFragment().dismiss();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
            }
        });
    }

    /**
     * 获取族亲树形结构
     */
    private void getKinTreeData() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        HttpManager.getInstance().post("index/Tree/getTribeRelative", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    NewKinMemberRecords records = new Gson().fromJson(json, new TypeToken<NewKinMemberRecords>(){}.getType());
                    int root_id = records.getRoot_id();
                    int current_id = records.getCurrent_user_id();
                    List<NewKinMember> list = records.getTree_data();
                    setKinMember(root_id, current_id, list);
//                    KinMember kinMember = new Gson().fromJson(json, new TypeToken<KinMember>(){}.getType());
//                    KinMemberRecords records = new KinMemberRecords();
//                    List<KinMember> list = new ArrayList<>();
//                    list.add(kinMember);
//                    records.setData(list);
//
//                    String jsonStr = new Gson().toJson(records);
//                    loadWebView(jsonStr);
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
                if (((MainActivity)getActivity()) != null) {
                    if (((MainActivity)getActivity()).getDialogFragment() != null) {
                        ((MainActivity)getActivity()).getDialogFragment().show(((MainActivity)getActivity()).getSupportFragmentManager(), "get_kin_tree_progress");
                    }
                }
            }

            @Override
            public void requestComplete() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (((MainActivity)getActivity()) != null) {
                            if (((MainActivity)getActivity()).getDialogFragment() != null) {
                                ((MainActivity)getActivity()).getDialogFragment().dismiss();
                            }
                        }
                    }
                }, 300);
            }
        });
    }

    /**
     * 更新视图
     */
    public void upDateTree() {
        if (isNear) {
            getNearTreeData();
        } else {
            getKinTreeData();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.PERSONAL_DATA) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
