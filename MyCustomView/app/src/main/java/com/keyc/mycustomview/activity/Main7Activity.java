package com.keyc.mycustomview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keyc.mycustomview.R;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.TRUE;

public class Main7Activity extends AppCompatActivity {
    private RelativeLayout mRl;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        mRl = (RelativeLayout) findViewById(R.id.rl_layout);
        BaseView baseView = new BaseView(this);
//        View view = LayoutInflater.from(this).inflate(R.layout.test,null);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.addRule(CENTER_IN_PARENT,TRUE);
        addView(mRl);
        //mRl.addView(view,params);
    }
    public void addView(ViewGroup baseView){
        count++;
        View view = LayoutInflater.from(this).inflate(R.layout.test,null);
        TextView fq = (TextView)view.findViewById(R.id.fq_id);
        TextView mq = (TextView)view.findViewById(R.id.mq_id);
        fq.setText("fq"+count);
        mq.setText("mq"+count);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT,TRUE);
        baseView.addView(view,params);
        if (count == 2){
            View view1 = LayoutInflater.from(this).inflate(R.layout.test,null);
            TextView fq1 = (TextView)view1.findViewById(R.id.fq_id);
            TextView mq1 = (TextView)view1.findViewById(R.id.mq_id);
            fq1.setText("xd"+count);
            mq1.setText("xd-m"+count);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.addRule(CENTER_IN_PARENT,TRUE);
            baseView.addView(view1,params1);
        }
        if (count == 3){
            View view1 = LayoutInflater.from(this).inflate(R.layout.test,null);
            TextView fq1 = (TextView)view1.findViewById(R.id.fq_id);
            TextView mq1 = (TextView)view1.findViewById(R.id.mq_id);
            fq1.setText("xd"+count);
            mq1.setText("xd-m"+count);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.addRule(CENTER_IN_PARENT,TRUE);
            baseView.addView(view1,params1);
            View view2 = LayoutInflater.from(this).inflate(R.layout.test,null);
            TextView fq2 = (TextView)view2.findViewById(R.id.fq_id);
            TextView mq2 = (TextView)view2.findViewById(R.id.mq_id);
            fq2.setText("xd2"+count);
            mq2.setText("xd2-m"+count);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.addRule(CENTER_IN_PARENT,TRUE);
            baseView.addView(view2,params2);
        }
        if (count < 10)
        {
            LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.children_id);
            addView(linearLayout);
        }
    }
}
