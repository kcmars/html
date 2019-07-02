package family.li.aiyun.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import family.li.aiyun.LiApplication;
import family.li.aiyun.R;
import family.li.aiyun.bean.NoticeInfo;
import family.li.aiyun.listener.OnNoticeInfoItemClickListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import java.util.List;

import static family.li.aiyun.LiApplication.getContext;

/**
 * Created by keyC on 2019/6/15.
 * 互动消息列表item
 */

public class NoticeInterRecycleViewAdapter extends RecyclerView.Adapter<NoticeInterRecycleViewAdapter.ViewHolder> {

    private List<NoticeInfo> mData;
    private OnNoticeInfoItemClickListener mListener;

    public NoticeInterRecycleViewAdapter(List<NoticeInfo> mData, OnNoticeInfoItemClickListener mListener) {
        this.mData = mData;
        this.mListener = mListener;
    }

    @Override
    public NoticeInterRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(family.li.aiyun.R.layout.item_notice_inter, parent, false);
        return new NoticeInterRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeInterRecycleViewAdapter.ViewHolder holder, int position) {
        final NoticeInfo noticeInfo = mData.get(position);
        holder.mTvTitle.setText(noticeInfo.getTitle());
        holder.mTvTime.setText(noticeInfo.getTitle());
        holder.mTvContent.setText(noticeInfo.getTitle());
        Glide.with(LiApplication.mContext)
                .load(R.mipmap.icon_man_gray_circle)
                .placeholder(R.mipmap.icon_man_gray_circle)
                .error(R.mipmap.icon_man_gray_circle)
                .fallback(R.mipmap.icon_man_gray_circle)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(holder.mIvHead);
        holder.mTvTitle.setOnClickListener(new View.OnClickListener() {
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
        public final ImageView mIvHead;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvTitle = view.findViewById(family.li.aiyun.R.id.tv_title);
            mTvTime = view.findViewById(family.li.aiyun.R.id.tv_time);
            mTvContent = view.findViewById(family.li.aiyun.R.id.tv_content);
            mIvHead = view.findViewById(family.li.aiyun.R.id.iv_head);
        }
    }
}
