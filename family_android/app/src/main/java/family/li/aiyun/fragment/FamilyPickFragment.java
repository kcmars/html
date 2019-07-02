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
import family.li.aiyun.bean.CityRecords;
import family.li.aiyun.bean.FamilyGenerationRecords;
import family.li.aiyun.bean.ProvinceRecords;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;

/**
 * Created by keyC on 2019/6/14.
 * 选择房代
 */

public class FamilyPickFragment extends DialogFragment implements NumberPickerView.OnValueChangeListener  {

    private NumberPickerView mPickerViewF;
    private NumberPickerView mPickerViewG;
    private List<String> mFamilyLists = new ArrayList<>();
    private List<String> mGenerationLists = new ArrayList<>();
    private OnSelectedListener mListener;
    private String mTitle;

    public static FamilyPickFragment newInstance(String title, final OnSelectedListener mListener) {
        FamilyPickFragment fragment = new FamilyPickFragment();
        fragment.setListener(mListener);
        fragment.setTitle(title);
        return fragment;
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
        View view = inflater.inflate(R.layout.view_family_picker, null);
        ((TextView) view.findViewById(R.id.txt_title)).setText(mTitle);
        mPickerViewF = view.findViewById(R.id.picker_family);
        mPickerViewG = view.findViewById(R.id.picker_generation);
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
                if (mListener != null && mFamilyLists.size() > 0 && mGenerationLists.size() > 0) {
                    String family = mFamilyLists.get(mPickerViewF.getValue());
                    String generation = mGenerationLists.get(mPickerViewG.getValue());

                    mListener.onSelected(family, generation);
                }
                // 销毁弹出框
                dismiss();
            }
        });
        Log.i("TAG", "onCreateView: 111");

        //获取的房代数据
        getData();

        mPickerViewF.setOnValueChangedListener(this);
        mPickerViewG.setOnValueChangedListener(this);
        return view;
    }

    /**
     * 获取的房代数据
     */
    private void getData() {
        HashMap param = new HashMap();
        HttpManager.getInstance().post("index/Config/generations", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    mFamilyLists.clear();
                    mGenerationLists.clear();
                    String json = new Gson().toJson(data);
                    FamilyGenerationRecords records = new Gson().fromJson(json, new TypeToken<FamilyGenerationRecords>(){}.getType());
                    for (int i = 0; i < records.getFamily().size(); i++) {
                        mFamilyLists.add(records.getFamily().get(i).getName());
                    }
                    for (int i = 0; i < records.getGeneration().size(); i++) {
                        mGenerationLists.add(records.getGeneration().get(i).getName());
                    }
                    initTime(true);
                }
            }

            @Override
            public void requestFail(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestError(String msg) {
                ToastUtil.showToast(msg);
            }

            @Override
            public void requestStart() {

            }

            @Override
            public void requestComplete() {
            }
        });
    }

    /**
     * 初始化
     * @param changeP
     */
    private void initTime(boolean changeP) {

        String family = null;
        if (mFamilyLists.size() > 0) {
            family = mFamilyLists.get(mPickerViewF.getValue());
        }
        setData(mPickerViewF, (String[]) (mFamilyLists.toArray(new String[mFamilyLists.size()])), family);

        String generation = null;
        if (mGenerationLists.size() > 0) {
            generation = mGenerationLists.get(mPickerViewG.getValue());
        }

        setData(mPickerViewG, (String[]) (mGenerationLists.toArray(new String[mGenerationLists.size()])), generation);
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

        if (picker.getId() == R.id.picker_family) {

        } else if (picker.getId() == R.id.picker_generation) {

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

        void onSelected(String family, String generation);
    }
}
