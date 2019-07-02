package com.keyc.mycustomview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keyc.mycustomview.R;
import com.keyc.mycustomview.bean.CountryCode;
import com.keyc.mycustomview.listener.OnCountryItemClickListener;

import java.util.List;

/**
 * Created by keyC on 2019/6/13.
 */

public class ContactSortAdapter extends RecyclerView.Adapter<ContactSortAdapter.ViewHolder> {

    private List<CountryCode> mData;
    private OnCountryItemClickListener mListener;

    public ContactSortAdapter(List<CountryCode> mData, OnCountryItemClickListener mListener) {
        this.mData = mData;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sort, parent, false);
        return new ContactSortAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactSortAdapter.ViewHolder holder, int position) {
        final CountryCode sortModel = mData.get(position);
        holder.mTvName.setText(sortModel.getChinese());
        if (!compareSection(position)){
            holder.mLlTopTitle.setVisibility(View.VISIBLE);
            holder.mTvLetter.setText(sortModel.getLetter());
            holder.mLineView.setVisibility(View.GONE);
        }else{
            holder.mLlTopTitle.setVisibility(View.GONE);
            holder.mLineView.setVisibility(View.VISIBLE);
        }
        holder.mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(sortModel);
                }
            }
        });
    }

    // 获取当前位置的首字母(int表示ascii码)
    public String getSectionForPosition(int position) {
        return mData.get(position).getLetter();
    }

    // 获取字母首次出现的位置
    public int getPositionForSection(int section) {
        for (int i = 0; i < mData.size(); i++) {
            String s = mData.get(i).getLetter();
            int firstChar = Integer.valueOf(s.toUpperCase());
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private boolean compareSection(int position) {
        if (position == 0) {
             return false;
        } else {
            String current = getSectionForPosition(position);
            String previous = getSectionForPosition(position - 1);
            return current.equals(previous);
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return  0;
    }

    /**
     * 更新视图
     * @param data
     */
    public void notifyDataSetChanged(List<CountryCode> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImg;
        public final TextView mTvName;
        public final View mLineView;
        public final LinearLayout mLlTopTitle;
        public final TextView mTvLetter;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImg = view.findViewById(R.id.img);
            mTvName = view.findViewById(R.id.name);
            mLineView = view.findViewById(R.id.line_view);
            mLlTopTitle = view.findViewById(R.id.ll_top_title);
            mTvLetter = view.findViewById(R.id.tv_letter);
        }
    }
}
