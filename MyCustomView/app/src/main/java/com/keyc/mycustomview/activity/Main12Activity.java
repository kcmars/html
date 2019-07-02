package com.keyc.mycustomview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.FamilyMember;
import com.keyc.mycustomview.bean.KinMember;
import com.keyc.mycustomview.bean.Person;
import com.keyc.mycustomview.listener.OnFamilySelectListener;
import com.keyc.mycustomview.utils.Utils;
import com.keyc.mycustomview.view.NewFamilyTreeView;


public class Main12Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btnEnlarge;
    private Button btnShrinkDown;
    private NewFamilyTreeView ftvTree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main12);

        initView();
        setData();
    }

    private void initView() {
        btnEnlarge = (Button) findViewById(R.id.btn_enlarge);
        btnShrinkDown = (Button) findViewById(R.id.btn_shrink_down);
        ftvTree = (NewFamilyTreeView) findViewById(R.id.ftv_tree);
    }

    private void setData() {

        /**
         * 添加点击事件
         */
        ftvTree.setOnFamilySelectListener(new OnFamilySelectListener() {
            @Override
            public void onFamilySelect(Person person) {
                Toast.makeText(Main12Activity.this, person.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 读取json数据，并解析成对应的bean对象
         */
        String json = Utils.getJson("family.json", this);
        FamilyMember family = new Gson().fromJson(json, new TypeToken<FamilyMember>(){}.getType());

        /**
         * 添加数据
         */
        ftvTree.setFamilyMember(family);


        btnEnlarge.setOnClickListener(this);
        btnShrinkDown.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enlarge:
                ftvTree.doEnlarge();
                break;
            case R.id.btn_shrink_down:
                ftvTree.doShrinkDown();
                break;
        }
    }
}
