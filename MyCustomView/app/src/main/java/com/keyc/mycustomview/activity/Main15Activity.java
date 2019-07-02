package com.keyc.mycustomview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.FamilyTree;
import com.keyc.mycustomview.bean.Kin;
import com.keyc.mycustomview.bean.NodeModel2;
import com.keyc.mycustomview.utils.FamilyUtils;
import com.keyc.mycustomview.utils.Utils;
import com.keyc.mycustomview.view.FamilyTreeView6;

import java.util.ArrayList;

public class Main15Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main15);

        //获取屏幕宽高
        WindowManager wm = this.getWindowManager();
        int mWidth = wm.getDefaultDisplay().getWidth();
        int mHeight = wm.getDefaultDisplay().getHeight();

        FamilyTreeView6 FamilyTreeView6 = (FamilyTreeView6) findViewById(R.id.familyTree);

        /**
         * 读取json数据，并解析成对应的bean对象
         */
        String json = Utils.getJson("model.json", this);
        NodeModel2 root = new Gson().fromJson(json, new TypeToken<NodeModel2>(){}.getType());


        FamilyUtils utils = new FamilyUtils();
        FamilyTree rootTree = utils.buchheim(root);
        Log.e("TAG", "onCreate: " + root );
        Log.e("TAG", "onCreate: " + rootTree );
        FamilyTreeView6.setFamilyTree(rootTree, mWidth, mHeight);
    }
}
