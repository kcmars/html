package com.keyc.mycustomview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.Kin;
import com.keyc.mycustomview.bean.KinMember;
import com.keyc.mycustomview.bean.Person;
import com.keyc.mycustomview.listener.OnKinMemberViewsClickListener;
import com.keyc.mycustomview.listener.OnKinViewsClickListener;
import com.keyc.mycustomview.utils.Utils;
import com.keyc.mycustomview.view.FamilyTreeCustomView;
import com.nineoldandroids.view.ViewHelper;

public class Main5Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btnEnlarge;  //放大
    private Button btnShrinkDown; //缩小
    private FamilyTreeCustomView ftvTree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        btnEnlarge = (Button) findViewById(R.id.btn_enlarge);
        btnShrinkDown = (Button) findViewById(R.id.btn_shrink_down);
        ftvTree = (FamilyTreeCustomView) findViewById(R.id.ftv_tree);

        btnEnlarge.setOnClickListener(this);
        btnShrinkDown.setOnClickListener(this);

        /**
         * 添加点击事件
         */
        ftvTree.setOnViewsClick(new OnKinMemberViewsClickListener() {
            @Override
            public void onViewsClick(KinMember kinMember) {
                Toast.makeText(Main5Activity.this, kinMember.getCall() + "  " + kinMember.getName() + "（"+kinMember.getSex() + "）", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 读取json数据，并解析成对应的bean对象
         */
        String json = Utils.getJson("kinMember.json", this);
        KinMember kinMember = new Gson().fromJson(json, new TypeToken<KinMember>(){}.getType());

        /**
         * 添加数据
         */
        ftvTree.setData(kinMember);
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
