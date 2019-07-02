package family.li.aiyun.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import family.li.aiyun.bean.NoticeInfo;
import family.li.aiyun.listener.OnNoticeInfoItemClickListener;
import family.li.aiyun.util.Utils;

import java.util.List;

/**
 * Created by keyC on 2019/6/15.
 * 系统消息列表item
 */

public class NoticeSystemRecycleViewAdapter extends RecyclerView.Adapter<NoticeSystemRecycleViewAdapter.ViewHolder> {

    private Context mContext;
    private List<NoticeInfo> mData;
    private OnNoticeInfoItemClickListener mListener;

    public NoticeSystemRecycleViewAdapter(Context mContext, List<NoticeInfo> mData, OnNoticeInfoItemClickListener mListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.mListener = mListener;
    }

    @Override
    public NoticeSystemRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(family.li.aiyun.R.layout.item_notice_system, parent, false);
        return new NoticeSystemRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeSystemRecycleViewAdapter.ViewHolder holder, int position) {
        final NoticeInfo noticeInfo = mData.get(position);
        holder.mTvTitle.setText(noticeInfo.getTitle());
        holder.mTvTime.setText(Utils.formatTime(noticeInfo.getAdd_time() + ""));
        holder.mTvContent.setText(noticeInfo.getContent());
        if (noticeInfo.getRead_status() == 0) {
            holder.mTvTitle.setTextColor(mContext.getResources().getColor(family.li.aiyun.R.color.colorText333333));
            holder.mTvTime.setTextColor(mContext.getResources().getColor(family.li.aiyun.R.color.colorText333333));
            holder.mTvContent.setTextColor(mContext.getResources().getColor(family.li.aiyun.R.color.colorText333333));
        } else {
            holder.mTvTitle.setTextColor(mContext.getResources().getColor(family.li.aiyun.R.color.colorGray999999));
            holder.mTvTime.setTextColor(mContext.getResources().getColor(family.li.aiyun.R.color.colorGray999999));
            holder.mTvContent.setTextColor(mContext.getResources().getColor(family.li.aiyun.R.color.colorGray999999));
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(noticeInfo);
                }
            }
        });
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
    public void notifyDataSetChanged(List<NoticeInfo> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTvTitle;
        public final TextView mTvTime;
        public final TextView mTvContent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvTitle = view.findViewById(family.li.aiyun.R.id.tv_title);
            mTvTime = view.findViewById(family.li.aiyun.R.id.tv_time);
            mTvContent = view.findViewById(family.li.aiyun.R.id.tv_content);
        }
    }
}
