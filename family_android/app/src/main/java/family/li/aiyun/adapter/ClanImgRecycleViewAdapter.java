package family.li.aiyun.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import family.li.aiyun.LiApplication;

/**
 * Created by keyC on 2019/6/15.
 * 日志列表item
 */

public class ClanImgRecycleViewAdapter extends RecyclerView.Adapter<ClanImgRecycleViewAdapter.ViewHolder> {

    private List<String> mData;

    public ClanImgRecycleViewAdapter(List<String> mData) {
        this.mData = mData;
    }

    @Override
    public ClanImgRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(family.li.aiyun.R.layout.item_caln_img, parent, false);
        return new ClanImgRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClanImgRecycleViewAdapter.ViewHolder holder, int position) {
        final String url = mData.get(position);
        Glide.with(LiApplication.getContext())
                .load(url)
                .into(holder.mIvImg);
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
    public void notifyDataSetChanged(List<String> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIvImg;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIvImg = view.findViewById(family.li.aiyun.R.id.iv_img);
        }
    }
}
