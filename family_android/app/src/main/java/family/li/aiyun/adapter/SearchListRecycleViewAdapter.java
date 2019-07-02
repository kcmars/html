package family.li.aiyun.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import family.li.aiyun.R;
import family.li.aiyun.bean.KinSearchMember;
import family.li.aiyun.listener.OnSearchItemClickListener;

import java.util.List;

import family.li.aiyun.LiApplication;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static family.li.aiyun.LiApplication.getContext;

/**
 * Created by keyC on 2019/6/15.
 * 互动消息列表item
 */

public class SearchListRecycleViewAdapter extends RecyclerView.Adapter<SearchListRecycleViewAdapter.ViewHolder> {

    private List<KinSearchMember> mData;
    private OnSearchItemClickListener mListener;

    public SearchListRecycleViewAdapter(List<KinSearchMember> mData, OnSearchItemClickListener mListener) {
        this.mData = mData;
        this.mListener = mListener;
    }

    @Override
    public SearchListRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);
        return new SearchListRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListRecycleViewAdapter.ViewHolder holder, int position) {
        final KinSearchMember member = mData.get(position);
        holder.mTvName.setText(member.getName());
        if (member.getIs_self() == 1) {
            holder.mTvTag.setText("自己");
        } else {
            holder.mTvTag.setText("");
        }
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(member.getFather_name())) {
            builder.append("父亲").append(member.getFather_name());
        }
        builder.append("    ");
        if (!TextUtils.isEmpty(member.getMother_name())) {
            builder.append("母亲").append(member.getMother_name());
        }
        holder.mTvInfo.setText(builder);
        Glide.with(LiApplication.mContext)
                .load(member.getHead_img())
                .placeholder(R.mipmap.icon_man_gray_circle)
                .error(R.mipmap.icon_man_gray_circle)
                .fallback(R.mipmap.icon_man_gray_circle)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(holder.mIvHead);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(member);
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
    public void notifyDataSetChanged(List<KinSearchMember> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTvName;
        public final TextView mTvTag;
        public final TextView mTvInfo;
        public final ImageView mIvHead;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvName = view.findViewById(R.id.tv_name);
            mTvTag = view.findViewById(R.id.tv_tag);
            mTvInfo = view.findViewById(R.id.tv_info);
            mIvHead = view.findViewById(R.id.iv_head);
        }
    }
}
