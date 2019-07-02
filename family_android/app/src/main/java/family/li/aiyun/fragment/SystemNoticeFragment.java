package family.li.aiyun.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import family.li.aiyun.R;
import family.li.aiyun.activity.WebActivity;
import family.li.aiyun.adapter.NoticeSystemRecycleViewAdapter;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.NoticeInfo;
import family.li.aiyun.bean.NoticeRecords;
import family.li.aiyun.listener.OnNoticeInfoItemClickListener;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by keyC on 2019/6/15.
 * 系统消息
 */

public class SystemNoticeFragment extends BaseFragment {

    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mTvNotMore;

    private ArrayList<NoticeInfo> mNoticeInfo = new ArrayList<>();
    private NoticeSystemRecycleViewAdapter mAdapter;
    private int mPage = 1;

    public SystemNoticeFragment() {
        // Required empty public constructor
    }

    public static SystemNoticeFragment newInstance() {
        SystemNoticeFragment fragment = new SystemNoticeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_system_notice, container, false);
        mSmartRefreshLayout = view.findViewById(R.id.sr_layout);
        mRecyclerView = view.findViewById(R.id.system_notice_list);
        mTvNotMore = view.findViewById(R.id.tv_not_more);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 1;
                getNoticeData();
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                getNoticeData();
            }
        });
        mSmartRefreshLayout.autoRefresh();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mSmartRefreshLayout != null) {
            mSmartRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(mSmartRefreshLayout!=null) {
                mSmartRefreshLayout.autoRefresh();
            }
        }
    }

    /**
     * 获取消息列表
     */
    private void getNoticeData() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        param.put("page", mPage + "");
        param.put("page_size", "10");
        HttpManager.getInstance().post("index/Notice/system", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    NoticeRecords records  = new Gson().fromJson(json, new TypeToken<NoticeRecords>(){}.getType());
                    mTvNotMore.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (mPage == 1) {
                        mNoticeInfo.clear();
                    }
                    mNoticeInfo.addAll(records.getRecords());
                    if (mAdapter == null) {
                        mAdapter = new NoticeSystemRecycleViewAdapter(getContext(), mNoticeInfo, new OnNoticeInfoItemClickListener() {

                            @Override
                            public void onItemClick(NoticeInfo noticeInfo) {
                                if (noticeInfo != null && !TextUtils.isEmpty(noticeInfo.getUrl())) {
                                    Intent intent = new Intent(getActivity(), WebActivity.class);
                                    intent.putExtra("url", noticeInfo.getUrl() + "&token=" + Uri.encode(Utils.TOKEN));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                        });
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                        mAdapter.notifyDataSetChanged(mNoticeInfo);
                    }
                } else  {
                    if (mPage == 1) {
                        mTvNotMore.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        mNoticeInfo.clear();
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged(mNoticeInfo);
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
                    mNoticeInfo.clear();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged(mNoticeInfo);
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
                    mNoticeInfo.clear();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged(mNoticeInfo);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
