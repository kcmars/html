package com.keyc.mycustomview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.CountryCode;
import com.keyc.mycustomview.utils.CharacterParser;
import com.keyc.mycustomview.utils.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by keyC on 2019/6/12.
 * 仿通讯录
 */

public class ContactRecyclerView extends FrameLayout implements SideBarView.LetterTouchListener {

    private TextView mTvLetterShow;
    private LinearLayout mLlTopTitle;
    private TextView mTvLetter;
    private SideBarView mSideBarView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private int mLetterHeight = 0;
    private int mCurrentPosition = 0;
    private List<CountryCode> mData = new ArrayList();
    private CharacterParser mParser = CharacterParser.getInstance();

    public ContactRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ContactRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContactRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_layout, this);
        mTvLetterShow = (TextView) view.findViewById(R.id.tv_letter_show);
        mLlTopTitle = (LinearLayout) view.findViewById(R.id.ll_top_title);
        mTvLetter = (TextView) view.findViewById(R.id.tv_letter);
        mSideBarView = (SideBarView) view.findViewById(R.id.view_sidebar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mSideBarView.setLetterTouchListener(this);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mLlTopTitle != null) {
                    mLetterHeight = mLlTopTitle.getHeight();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //找到列表下一个可见的View
                View view1 = mLayoutManager.findViewByPosition(mCurrentPosition + 1);
                // 检查列表中的letter布局是否显示
                if (view1 != null && view1.getTop() <= mLetterHeight && ((TextView) view1.findViewById(R.id.tv_letter)).getVisibility() == View.VISIBLE){
                    //被顶掉的效果
                    mLlTopTitle.setY((float)(-(mLetterHeight - view1.getTop())));
                }else{
                    mLlTopTitle.setY(0f);
                }
                //判断是否需要更新悬浮条
                if (mCurrentPosition != mLayoutManager.findFirstVisibleItemPosition()){
                    mLlTopTitle.setY(0f);
                    updateLetter();
                }
            }
        });
    }

    @Override
    public void setLetterVisibility(int visibility) {
        if (mTvLetterShow != null) {
            mTvLetterShow.setVisibility(visibility);
        }
    }

    @Override
    public void setLetter(String letter) {
        if (!TextUtils.isEmpty(letter)){
            mTvLetterShow.setText(letter);
            int position = getPositionForSection(letter);
            if (position != -1) {
                updateLetter();
                mLetterHeight = mLlTopTitle.getHeight();
                mLayoutManager.scrollToPositionWithOffset(position,0); // 使当前位置处于最顶端
            }
        }
    }

    public RecyclerView getRecycler() {
        return mRecyclerView;
    }

    /**
     * 刷新 字母title
     */
    private void updateLetter(){
        mCurrentPosition = mLayoutManager.findFirstVisibleItemPosition();
        if (mData.size() > 0 && mCurrentPosition > -1 && mCurrentPosition < mData.size()){
            if (mTvLetter != null) {
                mTvLetter.setText(mData.get(mCurrentPosition).getLetter());
            }
        }
    }

    /**
     * 初始化数据
     */
    public void initData(List<CountryCode> data){
        mData.clear();
        mData.addAll(data);
        updateLetter();
    }

    /**
     * 数据排序
     */
    public List<CountryCode> sortData(List<CountryCode> data) {
        List<CountryCode> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            CountryCode sm = data.get(i);
            String pinyin = mParser.getSelling(sm.getChinese());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sm.setLetter(sortString);
            } else {
                sm.setLetter("#");
            }
            list.add(sm);
        }
        Collections.sort(list, new PinyinComparator());
        return list;
    }

    /**
     * 获取字母首次出现的位置
     */
    private int getPositionForSection(String letter) {
        if (mData != null && mData.size() > 0){
            for (int i = 0; i < mData.size(); i++) {
                String s = mData.get(i).getLetter();
                if (letter.equals(s)) {
                    return i;
                }
            }
            return -1;
        } else {
            return -1;
        }
    }

    /**
     * 根据输入的内容刷新数据
     */
    public List<CountryCode> updateData(String filterStr) {
        List<CountryCode> newData = new ArrayList<>();
        if (mData != null && mData.size() > 0){
            if ("".equals(filterStr)) {
                newData = mData;
            } else {
                for (int i = 0; i < mData.size(); i++) {
                    String name = mData.get(i).getChinese();
                    if (name.contains(filterStr) || mParser.getSelling(name).startsWith(filterStr)) {
                        newData.add(mData.get(i));
                    }
                }
            }
            mData.clear();
            mData.addAll(newData);
            updateLetter();
        }
        return mData;
    }
}
