package family.li.aiyun.fragment;

import android.annotation.SuppressLint;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import family.li.aiyun.base.HttpRequestCallback;
import family.li.aiyun.bean.CityRecords;
import family.li.aiyun.bean.ProvinceRecords;
import family.li.aiyun.util.HttpManager;
import family.li.aiyun.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by keyC on 2019/6/14.
 * 选择籍贯
 */

public class NativePickFragment extends DialogFragment implements NumberPickerView.OnValueChangeListener  {

    private NumberPickerView mPickerViewP;
    private NumberPickerView mPickerViewC;
    private List<String> mProvinceLists = new ArrayList<>();
    private List<String> mCityLists = new ArrayList<>();
    private OnSelectedListener mListener;
    private String mTitle;

    public static NativePickFragment newInstance(String title, final OnSelectedListener mListener) {
        NativePickFragment fragment = new NativePickFragment();
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
        View view = inflater.inflate(family.li.aiyun.R.layout.view_native_picker, null);
        ((TextView) view.findViewById(family.li.aiyun.R.id.txt_title)).setText(mTitle);
        mPickerViewP = view.findViewById(family.li.aiyun.R.id.picker_province);
        mPickerViewC = view.findViewById(family.li.aiyun.R.id.picker_city);
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

            public void onClick(View v) {
                if (mListener != null && mProvinceLists.size() > 0) {
                    String province = mProvinceLists.get(mPickerViewP.getValue());
                    String city = "";
                    if (mCityLists != null && mCityLists.size() > 0) {
                        city = mCityLists.get(mPickerViewC.getValue());
                    }
                    mListener.onSelected(province, city);
                }
                // 销毁弹出框
                dismiss();
            }
        });

        //获取省份数据
        getProvince();

        mPickerViewP.setOnValueChangedListener(this);
        mPickerViewC.setOnValueChangedListener(this);
        return view;
    }

    /**
     * 获取省份数据
     */
    private void getProvince() {
        HashMap param = new HashMap();
        HttpManager.getInstance().post("index/District/getProvinces", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    mProvinceLists.clear();
                    String json = new Gson().toJson(data);
                    ProvinceRecords records = new Gson().fromJson(json, new TypeToken<ProvinceRecords>(){}.getType());
                    for (int i = 0; i < records.getRecords().size(); i++) {
                        mProvinceLists.add(records.getRecords().get(i).getProvince());
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
     * 获取城市数据
     */
    private void getCity(String pro_code, final String city) {
        HashMap param = new HashMap();
        param.put("pro_code", pro_code);
        HttpManager.getInstance().post("index/District/getCities", param, new HttpRequestCallback() {
            @Override
            public void requestSuccess(Object data, String msg) {
                if (data != null) {
                    mCityLists.clear();
                    String json = new Gson().toJson(data);
                    CityRecords records = new Gson().fromJson(json, new TypeToken<CityRecords>(){}.getType());
                    for (int i = 0; i < records.getRecords().size(); i++) {
                        mCityLists.add(records.getRecords().get(i).getCity());
                    }
                    if (mCityLists != null && mCityLists.size() > 0) {
                        setData(mPickerViewC, (String[]) (mCityLists.toArray(new String[mCityLists.size()])), city);
                    }
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

        String province = null;
        if (mProvinceLists.size() > 0) {
            province = mProvinceLists.get(mPickerViewP.getValue());
        }

        String city = null;
        if (mCityLists.size() > 0) {
            city = mCityLists.get(mPickerViewC.getValue());
        }

        setData(mPickerViewP, (String[]) (mProvinceLists.toArray(new String[mProvinceLists.size()])), province);

        if (changeP) {
            //获取城市
            getCity(province, city);
        }
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

        if (picker.getId() == family.li.aiyun.R.id.picker_province) {

            initTime(true);

        } else if (picker.getId() == family.li.aiyun.R.id.picker_city) {

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

        void onSelected(String province, String city);
    }
}
