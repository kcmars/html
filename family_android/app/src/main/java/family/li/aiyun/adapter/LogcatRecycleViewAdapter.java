package family.li.aiyun.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import family.li.aiyun.LiApplication;
import family.li.aiyun.R;
import family.li.aiyun.bean.LogcatInfo;
import family.li.aiyun.util.Utils;

import java.util.List;

import static family.li.aiyun.LiApplication.mContext;

/**
 * Created by keyC on 2019/6/15.
 * 日志列表item
 */

public class LogcatRecycleViewAdapter extends RecyclerView.Adapter<LogcatRecycleViewAdapter.ViewHolder> {

    private List<LogcatInfo> mData;

    public LogcatRecycleViewAdapter(List<LogcatInfo> mData) {
        this.mData = mData;
    }

    @Override
    public LogcatRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_logcat, parent, false);
        return new LogcatRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogcatRecycleViewAdapter.ViewHolder holder, int position) {
        final LogcatInfo logcatInfo = mData.get(position);
        String opt_text = "";
        if (logcatInfo.getOpt() == 1) {
            Glide.with(mContext).load(R.mipmap.icon_add_green).into(holder.mIvType);
            opt_text = " 添加了 ";
        } else if (logcatInfo.getOpt() == 2) {
            Glide.with(mContext).load(R.mipmap.icon_delete_red).into(holder.mIvType);
            opt_text = " 删除了 ";
        } else if (logcatInfo.getOpt() == 3) {
            Glide.with(mContext).load(R.mipmap.icon_update_green).into(holder.mIvType);
            opt_text = " 更新了 ";
        } else if (logcatInfo.getOpt() == 4) {
            Glide.with(mContext).load(R.mipmap.icon_add_green).into(holder.mIvType);
            opt_text = " 加入了家族 ";
        } else if (logcatInfo.getOpt() == 5) {
            Glide.with(mContext).load(R.mipmap.icon_add_green).into(holder.mIvType);
            opt_text = " 邀请了 ";
        }
        holder.mTvName1.setText(logcatInfo.getName());
        holder.mTvType.setText(opt_text);
        holder.mTvType.setTextColor(logcatInfo.getOpt() == 2 ? mContext.getResources().getColor(R.color.color_4) : mContext.getResources().getColor(R.color.colorText6ED300));
        holder.mTvName2.setText(logcatInfo.getContent());
        holder.mTvTime.setText(Utils.formatTime(logcatInfo.getAdd_time()));
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
    public void notifyDataSetChanged(List<LogcatInfo> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIvType;
        public final TextView mTvName1;
        public final TextView mTvType;
        public final TextView mTvName2;
        public final TextView mTvTime;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIvType = view.findViewById(R.id.iv_type);
            mTvName1 = view.findViewById(R.id.tv_name1);
            mTvType = view.findViewById(R.id.tv_type);
            mTvName2 = view.findViewById(R.id.tv_name2);
            mTvTime = view.findViewById(R.id.tv_time);
        }
    }
}
