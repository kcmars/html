package family.li.aiyun.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.R;
import family.li.aiyun.adapter.LogcatRecycleViewAdapter;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.LogcatInfo;
import family.li.aiyun.bean.LogcatRecords;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;

import family.li.aiyun.LiApplication;

public class LogcatActivity extends AppActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mTvNotMore;

    private ArrayList<LogcatInfo> mLogcatInfo = new ArrayList<>();
    private LogcatRecycleViewAdapter mAdapter;
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcat);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.sr_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.logcat_list);
        mTvNotMore = (TextView) findViewById(R.id.tv_not_more);

        mIvBack.setOnClickListener(this);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 1;
                getLogcatData();
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                getLogcatData();
            }
        });
        mSmartRefreshLayout.autoRefresh();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:  //返回上一页
                finish();
                break;
        }
    }

    /**
     * 获取消息列表
     */
    private void getLogcatData() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("page", mPage + "");
        param.put("page_size", "20");
        HttpManager.getInstance().post("index/UserLog/records", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    LogcatRecords records = new Gson().fromJson(json, new TypeToken<LogcatRecords>(){}.getType());
                    mTvNotMore.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (mPage == 1) {
                        mLogcatInfo.clear();
                    }
                    mLogcatInfo.addAll(records.getRecords());
                    if (mAdapter == null) {
                        mAdapter = new LogcatRecycleViewAdapter(mLogcatInfo);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(LiApplication.getContext()));
                        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                //设定底部边距为1px
                                outRect.set(0, 0, 0, 10);
                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged(mLogcatInfo);
                    }
                } else  {
                    if (mPage == 1) {
                        mTvNotMore.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        mLogcatInfo.clear();
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged(mLogcatInfo);
                        }
                    } else {
                        ToastUtil.showToast(msg);
                        mPage--;
                    }
                }
            }

            @Override
            public void requestFail(String msg) {
                if (mPage == 1) {
                    mTvNotMore.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    mLogcatInfo.clear();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged(mLogcatInfo);
                    }
                } else {
                    ToastUtil.showToast(msg);
                    mPage--;
                }
            }

            @Override
            public void requestError(String msg) {
                mSmartRefreshLayout.finishRefresh();
                mSmartRefreshLayout.finishLoadmore();
                if (mPage == 1) {
                    mTvNotMore.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    mLogcatInfo.clear();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged(mLogcatInfo);
                    }
                } else {
                    ToastUtil.showToast(msg);
                    mPage--;
                }
            }

            @Override
            public void requestStart() {

            }

            @Override
            public void requestComplete() {
                mSmartRefreshLayout.finishRefresh();
                mSmartRefreshLayout.finishLoadmore();
            }
        });
    }
}
