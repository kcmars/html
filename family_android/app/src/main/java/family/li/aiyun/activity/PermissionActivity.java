package family.li.aiyun.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.R;
import family.li.aiyun.adapter.PermissionAdapter;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.PermissionMember;
import family.li.aiyun.bean.PermissionRecords;
import family.li.aiyun.listener.OnPermissionItemClickListener;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import family.li.aiyun.LiApplication;

public class PermissionActivity extends AppActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private TextView mTvToExplain;
    private RecyclerView mRlNear;
    private TextView mTvKinText;
    private RecyclerView mRlClan;
    private PermissionAdapter mNearAdapter;
    private PermissionAdapter mClanAdapter;
    private List<PermissionMember> mNearMember = new ArrayList<>();
    private List<PermissionMember> mClanMember = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvKinText = (TextView) findViewById(R.id.tv_kin_text);
        mTvToExplain = (TextView) findViewById(R.id.tv_explain);
        mRlNear = (RecyclerView) findViewById(R.id.rl_near_list);
        mRlClan = (RecyclerView) findViewById(R.id.rl_clan_list);

        mIvBack.setOnClickListener(this);
        mTvToExplain.setOnClickListener(this);

        //获取权限人员名单
        getPermissionData();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_explain:
                intent.setClass(PermissionActivity.this, PermissionsExplainActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取权限人员名单
     */
    private void getPermissionData() {
        HashMap param = new HashMap();
        param.put("token", Utils.TOKEN);
        HttpManager.getInstance().post("index/Permission/supervisors", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    PermissionRecords records = new Gson().fromJson(json, new TypeToken<PermissionRecords>(){}.getType());

                    /**
                     * 近亲管理员，默认存在自己
                     */
                    mNearMember.clear();
                    PermissionMember member = new PermissionMember();
                    member.setName("自己");
                    member.setHead_img(Utils.USER_HEAD_IMG);
                    mNearMember.add(member);
                    mNearMember.addAll(records.getFamily_supervisors());
                    /**
                     * 族亲管理员
                     */
                    mClanMember.clear();
                    mClanMember.addAll(records.getClan_supervisors());
                    if (mClanMember.size() > 0) {
                        mTvKinText.setVisibility(View.VISIBLE);
                    } else {
                        mTvKinText.setVisibility(View.GONE);
                    }
                    if (mNearAdapter == null) {
                        mNearAdapter = new PermissionAdapter(mNearMember, new OnPermissionItemClickListener() {

                            @Override
                            public void onItemClick(PermissionMember member) {

                            }
                        });
                        mRlNear.setLayoutManager(new GridLayoutManager(LiApplication.getContext(), 4));
                        mRlNear.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                //设定底部边距为1px
                                outRect.set(0, 0, 0, 10);
                            }
                        });
                        mRlNear.setAdapter(mNearAdapter);
                    } else {
                        mNearAdapter.notifyDataSetChanged(mNearMember);
                    }

                    if (mClanAdapter == null) {
                        mClanAdapter = new PermissionAdapter(mClanMember, new OnPermissionItemClickListener() {

                            @Override
                            public void onItemClick(PermissionMember member) {

                            }
                        });
                        mRlClan.setLayoutManager(new GridLayoutManager(LiApplication.getContext(), 4));
                        mRlClan.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                //设定底部边距为1px
                                outRect.set(0, 0, 0, 10);
                            }
                        });
                        mRlClan.setAdapter(mClanAdapter);
                    } else {
                        mClanAdapter.notifyDataSetChanged(mClanMember);
                    }
                }
//                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//                //调用下面这行代码可以实现布局横向排列
//                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                //GridLayoutManager 实现网格布局
//                //StaggeredGridLayoutManager 实现瀑布流布局
//                //和ListView不同的地方在于，ListView的布局排列是由自身去管理
//                //而RecyclerView则将这个工作交给LayoutManager，它制定了一套可扩展的布局排列接口
//                //子类只要按照接口的规范来实现，就能定制出不同排列方式的布局了
//                recyclerView.setLayoutManager(layoutManager);
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
