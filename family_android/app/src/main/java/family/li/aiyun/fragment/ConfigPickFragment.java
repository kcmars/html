package family.li.aiyun.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import family.li.aiyun.R;
import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.ConfigInfo;
import family.li.aiyun.bean.FamilyGenerationRecords;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;

/**
 * Created by keyC on 2019/6/14.
 * 选择学历、排行等配置信息
 */

public class ConfigPickFragment extends DialogFragment implements NumberPickerView.OnValueChangeListener  {

    private NumberPickerView mPicker;
    private List<String> mLists = new ArrayList<>();
    private OnSelectedListener mListener;
    private String mTitle;

    public static ConfigPickFragment newInstance(String title, List<ConfigInfo> data, final OnSelectedListener mListener) {
        ConfigPickFragment fragment = new ConfigPickFragment();
        fragment.setListener(mListener);
        fragment.setTitle(title);
        fragment.setData(data);
        return fragment;
    }
    private void setData(List<ConfigInfo> data) {
        List<String> list = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                list.add(data.get(i).getName());
            }
        }
        this.mLists = list;
    }

    private void setTitle(String title) {
        this.mTitle = title;
    }

    private void setListener(OnSelectedListener mListener) {
        this.mListener = mListener;
    }

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.view_config_picker, null);
        ((TextView) view.findViewById(R.id.txt_title)).setText(mTitle);
        mPicker = view.findViewById(R.id.picker);
        final TextView mCancel = view.findViewById(R.id.txt_cancel);
        // 取消按钮
        mCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        final TextView mBtnConfirm = view.findViewById(R.id.txt_confirm);
        // 确定按钮
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (mListener != null && mLists.size() > 0) {
                    String text = mLists.get(mPicker.getValue());
                    mListener.onSelected(text, mPicker.getValue());
                }
                // 销毁弹出框
                dismiss();
            }
        });

        mPicker.setOnValueChangedListener(this);

        initTime();

        return view;
    }

    /**
     * 初始化
     */
    private void initTime() {

        String picker = null;
        if (mLists.size() > 0) {
            picker = mLists.get(mPicker.getValue());
        }
        setData(mPicker, (String[]) (mLists.toArray(new String[mLists.size()])), picker);
    }

    /**
     * 设置视图
     *
     * @param picker
     * @param display
     * @param currentValue
     */
    private void setData(NumberPickerView picker, String[] display, String currentValue) {
        int minValue = picker.getMinValue();
        int oldMaxValue = picker.getMaxValue();
        int oldSpan = oldMaxValue - minValue + 1;
        int newMaxValue = display.length - 1;
        int newSpan = newMaxValue - minValue + 1;
        if (newSpan > oldSpan) {
            picker.setDisplayedValues(display);
            picker.setMaxValue(newMaxValue);
        } else {
            picker.setMaxValue(newMaxValue);
            picker.setDisplayedValues(display);
        }
        if (currentValue != null) {
            for (int i = 0; i < display.length; i++) {
                if (display[i].equals(currentValue)) {
                    picker.setValue(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {

        if (picker.getId() == R.id.picker) {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            window.setAttributes(wlp);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    public interface OnSelectedListener {

        void onSelected(String text, int index);
    }
}
