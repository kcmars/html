package family.li.aiyun.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by keyC on 2019/6/14.
 * 滚轮选择器
 */

public class CustomPickFragment extends DialogFragment {

    private NumberPickerView mPickerView;
    private OnSelectedListener listener;
    private String[] displays;

    public static CustomPickFragment newInstance(String[] displays, final OnSelectedListener mListener) {
        CustomPickFragment fragment = new CustomPickFragment();
        fragment.setOnClickListener(mListener);
        fragment.setDisplays(displays);
        return fragment;
    }

    private void setDisplays(String[] displays) {
        this.displays = displays;
    }

    private void setOnClickListener(OnSelectedListener onClickListener) {
        this.listener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(family.li.aiyun.R.layout.view_custom_picker, null);
        mPickerView = view.findViewById(family.li.aiyun.R.id.picker_view);
        final TextView mCancel = view.findViewById(family.li.aiyun.R.id.txt_cancel);
        // 取消按钮
        mCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        final TextView mBtnConfirm = view.findViewById(family.li.aiyun.R.id.txt_confirm);
        // 确定按钮
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSelected(mPickerView.getValue(), displays[mPickerView.getValue()]);
                }
                dismiss();
            }
        });
        setData(mPickerView, displays, displays[0]);
        return view;
    }

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
    }

    public interface OnSelectedListener {
        void onSelected(int index, String display);
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
}
