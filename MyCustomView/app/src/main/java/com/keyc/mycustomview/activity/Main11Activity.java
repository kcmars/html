package com.keyc.mycustomview.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyc.mycustomview.R;

public class Main11Activity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        TabLayout.Tab tab1 = mTabLayout.newTab().setCustomView(R.layout.teb_layout_item1);
        TabLayout.Tab tab2 = mTabLayout.newTab().setCustomView(R.layout.teb_layout_item2);
        TabLayout.Tab tab3 = mTabLayout.newTab().setCustomView(R.layout.teb_layout_item3);
        TabLayout.Tab tab4 = mTabLayout.newTab().setCustomView(R.layout.teb_layout_item4);

        mTabLayout.addTab(tab1);
        mTabLayout.addTab(tab2);
        mTabLayout.addTab(tab3);
        mTabLayout.addTab(tab4);
        mTabLayout.addOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {

        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
