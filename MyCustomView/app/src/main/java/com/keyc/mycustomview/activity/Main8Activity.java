package com.keyc.mycustomview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.adapter.ContactSortAdapter;
import com.keyc.mycustomview.bean.CountryCode;
import com.keyc.mycustomview.listener.OnCountryItemClickListener;
import com.keyc.mycustomview.utils.RecyclerViewUtil;
import com.keyc.mycustomview.utils.Utils;
import com.keyc.mycustomview.view.ContactRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Main8Activity extends AppCompatActivity {

    private ContactSortAdapter mAdapter = null;
    private List<CountryCode> mDataList = new ArrayList<>();
    private EditText mEtSearch;
    private ContactRecyclerView mContactRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        initView();
        initData();

    }

    private void initView(){
        mEtSearch = (EditText) findViewById(R.id.ed_search);
        mContactRecyclerView = (ContactRecyclerView) findViewById(R.id.contact_view);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim())){
                    mContactRecyclerView.initData(mDataList);
                    mAdapter.notifyDataSetChanged(mDataList);
                }else{
                    mAdapter.notifyDataSetChanged(mContactRecyclerView.updateData(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initData(){

        /**
         * 读取json数据，并解析成对应的bean对象
         */
        String json = Utils.getJson("countryCode.json", this);
        List<CountryCode> countryCodeList = new Gson().fromJson(json, new TypeToken<List<CountryCode>>(){}.getType());


//        String[] arrayData = {"a", "bd", "ced", "de", "as", "东皇太一","宫本武藏","王昭君","李元芳","刘禅","后裔","许爱明","无名","流海"
//                ,"亚瑟","吕布", "秋雅", "夏洛", "公孙离", "张良", "孙尚香", "我", "你", "啊", "哈哈", "嘿"
//                ,"无名","流海","亚瑟","吕布", "夏洛", "公孙离", "张良", "孙尚香","无名","流海","亚瑟","吕布", "刘备", "夏洛", "公孙离", "张良", "孙尚香"
//                ,"无名","流海","亚瑟","吕布", "秋雅", "夏洛", "公孙离", "张良", "孙尚香","无名","流海","亚瑟","吕布",
//                "秋雅", "夏洛", "公孙离", "张良", "孙尚香"};
//        List<String> data = new ArrayList<>();
//        for (int i = 0; i < arrayData.length; i++){
//            data.add(arrayData[i]);
//        }
        mAdapter = new ContactSortAdapter(mDataList, new OnCountryItemClickListener() {
            @Override
            public void onItemClick(CountryCode sortModel) {
                Toast.makeText(Main8Activity.this, sortModel.getChinese(), Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerViewUtil.initNoDecoration(this, mContactRecyclerView.getRecycler(), mAdapter);
        mDataList = mContactRecyclerView.sortData(countryCodeList);
        mContactRecyclerView.initData(mDataList);
        mAdapter.notifyDataSetChanged(mDataList);
    }
}
