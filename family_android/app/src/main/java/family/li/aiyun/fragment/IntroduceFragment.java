package family.li.aiyun.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import family.li.aiyun.R;
import family.li.aiyun.activity.WebActivity;
import family.li.aiyun.util.Utils;

/**
 * Created by keyC on 2019/6/15.
 * 介绍
 */

public class IntroduceFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTvText;
    private TextView mTvText1;
    private TextView mTvText2;

    public IntroduceFragment() {
        // Required empty public constructor
    }

    public static IntroduceFragment newInstance() {
        IntroduceFragment fragment = new IntroduceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_introduce, container, false);
        mTvText1 = (TextView) view.findViewById(R.id.tv_text1);
        mTvText2 = (TextView) view.findViewById(R.id.tv_text2);
        mTvText = (TextView) view.findViewById(R.id.tv_text);
        String text = "<font color='#000000'><big><big>&emsp 李</big></big></font>姓族谱始于宋代，盛于明、清，还有一些 是近现代的，基本上都是私修，而且有初修、续 修、再修等版本。修谱的主要目的是“尊祖收祖”， 并对宗族成员进行“尊尊亲亲之道”的伦理教育， 也就是尊敬祖先，加强宗族团结，明确怎样为人 处事。";
        mTvText.setText(Html.fromHtml(text));

        mTvText1.setOnClickListener(this);
        mTvText2.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), WebActivity.class);
        switch (v.getId()) {
            case R.id.tv_text1:
                intent.putExtra("url", Utils.URL_L1);
                break;

            case R.id.tv_text2:
                intent.putExtra("url", Utils.URL_L2);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
