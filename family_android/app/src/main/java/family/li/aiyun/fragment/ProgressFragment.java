package family.li.aiyun.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import family.li.aiyun.R;

/**
 * Created by keyC on 2019/6/19.
 * 请求网络时等待框
 */

public class ProgressFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.progress_dialog_layout, null);
        ImageView mIvGif = mView.findViewById(R.id.iv_gif);
        Glide.with(this).load(R.drawable.loading).into(mIvGif);
        builder.setView(mView);
//        ImageView ivProgress = (ImageView) mView.findViewById(R.id.ivProgress);
//        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_progress_anim);
//        ivProgress.startAnimation(animation);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.33), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        ProgressFragment fragment = (ProgressFragment) manager.findFragmentByTag(tag);
        if (fragment != null) {
            fragment.dismissAllowingStateLoss();
        }
        super.show(manager, tag);
    }
}
