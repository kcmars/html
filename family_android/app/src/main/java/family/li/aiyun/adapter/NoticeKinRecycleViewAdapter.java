package family.li.aiyun.adapter;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import family.li.aiyun.LiApplication;
import family.li.aiyun.bean.NoticeInfo;
import family.li.aiyun.listener.OnNoticeInfoItemClickListener;
import family.li.aiyun.util.Utils;

import java.util.List;

/**
 * Created by keyC on 2019/6/15.
 * 宗族公告消息列表item
 */

public class NoticeKinRecycleViewAdapter extends RecyclerView.Adapter<NoticeKinRecycleViewAdapter.ViewHolder> {

    private List<NoticeInfo> mData;
    private OnNoticeInfoItemClickListener mListener;

    public NoticeKinRecycleViewAdapter(List<NoticeInfo> mData, OnNoticeInfoItemClickListener mListener) {
        this.mData = mData;
        this.mListener = mListener;
    }

    @Override
    public NoticeKinRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(family.li.aiyun.R.layout.item_notice_kin, parent, false);
        return new NoticeKinRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeKinRecycleViewAdapter.ViewHolder holder, int position) {
        final NoticeInfo noticeInfo = mData.get(position);
        holder.mTvTitle.setText(noticeInfo.getTitle());
        holder.mTvTime.setText(Utils.formatTime(noticeInfo.getAdd_time() + ""));
        holder.mTvContent.setText(noticeInfo.getDes());
//        if (noticeInfo.getStatus() == 1) {
//            holder.mViewStatus.setVisibility(View.VISIBLE);
//        } else {
//            holder.mViewStatus.setVisibility(View.GONE);
//        }
        List<String> imgList = noticeInfo.getCover();
        ClanImgRecycleViewAdapter adapter = new ClanImgRecycleViewAdapter(imgList);
        holder.mRlClanImgList.setLayoutManager(new GridLayoutManager(LiApplication.getContext(), 2));
        holder.mRlClanImgList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                //设定底部边距为1px
                outRect.set(0, 0, 0, 10);
            }
        });
        holder.mRlClanImgList.setAdapter(adapter);

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
        public final View mViewStatus;
        public final TextView mTvTitle;
        public final TextView mTvTime;
        public final TextView mTvContent;
        public final RecyclerView mRlClanImgList;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mViewStatus = view.findViewById(family.li.aiyun.R.id.view_status);
            mTvTitle = view.findViewById(family.li.aiyun.R.id.tv_title);
            mTvTime = view.findViewById(family.li.aiyun.R.id.tv_time);
            mTvContent = view.findViewById(family.li.aiyun.R.id.tv_content);
            mRlClanImgList = view.findViewById(family.li.aiyun.R.id.rl_clan_img_list);
        }
    }
}
