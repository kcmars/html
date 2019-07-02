package family.li.aiyun.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.R;
import family.li.aiyun.adapter.CountryAdapter;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.CountryCode;
import family.li.aiyun.bean.CountryRecords;
import family.li.aiyun.listener.OnCountryItemClickListener;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.RecyclerViewUtil;
import family.li.aiyun.util.ToastUtil;
import family.li.aiyun.view.CountryRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CountryCodeActivity extends AppActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private CountryAdapter mAdapter = null;
    private List<CountryCode> mDataList = new ArrayList<>();
    private EditText mEtSearch;
    private CountryRecyclerView mCountryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_code);
        initView();
        initData();

    }

    private void initView(){
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEtSearch = (EditText) findViewById(R.id.ed_search);
        mCountryRecyclerView = (CountryRecyclerView) findViewById(R.id.country_view);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim())){
                    mCountryRecyclerView.initData(mDataList);
                    mAdapter.notifyDataSetChanged(mDataList);
                }else{
                    mAdapter.notifyDataSetChanged(mCountryRecyclerView.updateData(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mIvBack.setOnClickListener(this);
    }

    private void initData(){
        HashMap param = new HashMap();
        HttpManager.getInstance().post("index/Config/intlTelCode", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    String json = new Gson().toJson(data);
                    CountryRecords records = new Gson().fromJson(json, new TypeToken<CountryRecords>(){}.getType());
                    if (records != null) {
                        List<CountryCode> list = records.getRecords();
                        mAdapter = new CountryAdapter(mDataList, new OnCountryItemClickListener() {
                            @Override
                            public void onItemClick(CountryCode countryCode) {
                                Intent intent = new Intent();
                                intent.putExtra("country_code", countryCode);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                        RecyclerViewUtil.initNoDecoration(CountryCodeActivity.this, mCountryRecyclerView.getRecycler(), mAdapter);
                        mDataList = mCountryRecyclerView.sortData(list);
                        mCountryRecyclerView.initData(mDataList);
                        mAdapter.notifyDataSetChanged(mDataList);
                    }
                } else {
                    ToastUtil.showToast(msg);
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

    @Override
    public void onClick(View v) {
        hintKeyBoard();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    @SuppressLint("NewApi")
    private void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm != null && imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
//        View view = getWindow().peekDecorView();
//        if (view != null) {
//            try {
//                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (inputmanger != null) {
//                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
