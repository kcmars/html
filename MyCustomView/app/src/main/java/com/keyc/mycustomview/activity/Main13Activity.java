package com.keyc.mycustomview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyc.mycustomview.R;
import com.keyc.mycustomview.view.FixedGridLayout;

public class Main13Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main13);

        FixedGridLayout grid = (FixedGridLayout) findViewById(R.id.grid);
        grid.setCellWidth(150);
        grid.setCellHeight(180);
    }
}
