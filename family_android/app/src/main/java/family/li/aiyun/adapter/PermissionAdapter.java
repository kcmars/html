package family.li.aiyun.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import family.li.aiyun.R;
import family.li.aiyun.bean.PermissionMember;
import family.li.aiyun.listener.OnPermissionItemClickListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import java.util.List;

import static family.li.aiyun.LiApplication.getContext;
import static family.li.aiyun.LiApplication.mContext;

/**
 * Created by keyC on 2019/6/18.
 * 权限类表item
 */

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.ViewHolder> {
    
    private List<PermissionMember> mData;
    private OnPermissionItemClickListener mListener;

    public PermissionAdapter(List<PermissionMember> mData, OnPermissionItemClickListener mListener) {
        this.mData = mData;
        this.mListener = mListener;
    }

    @Override
    public PermissionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_permission, parent, false);
        return new PermissionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionAdapter.ViewHolder holder, int position) {
        final PermissionMember member = mData.get(position);
        holder.mTvName.setText(member.getName());
        Glide.with(mContext)
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
    public void notifyDataSetChanged(List<PermissionMember> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTvName;
        public final ImageView mIvHead;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvName = view.findViewById(R.id.tv_name);
            mIvHead = view.findViewById(R.id.iv_head);
        }
    }
}
