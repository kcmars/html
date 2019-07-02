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

import family.li.aiyun.R;
import family.li.aiyun.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by keyC on 2019/6/14.
 * 选择日期
 */

public class DatePickFragment extends DialogFragment implements NumberPickerView.OnValueChangeListener  {

    private NumberPickerView mPickerViewY;
    private NumberPickerView mPickerViewM;
    private NumberPickerView mPickerViewD;
    private SimpleDateFormat mDateFormat;
    private SimpleDateFormat mDateFormatBack;
    private List<String> mYearLists;
    private List<String> mMonthLists;
    private List<String> mDayLists;
    private OnSelectedTimeListener mListener;
    private String mTitle;

    public static DatePickFragment newInstance(String title, final OnSelectedTimeListener mListener) {
        DatePickFragment fragment = new DatePickFragment();
        fragment.setListener(mListener);
        fragment.setTitle(title);
        return fragment;
    }

    private void setTitle(String title) {
        this.mTitle = title;
    }

    private void setListener(OnSelectedTimeListener mListener) {
        this.mListener = mListener;
    }

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.view_date_picker, null);
        ((TextView) view.findViewById(R.id.txt_title)).setText(mTitle);
        mPickerViewY = view.findViewById(R.id.picker_year);
        mPickerViewM = view.findViewById(R.id.picker_month);
        mPickerViewD = view.findViewById(R.id.picker_date);
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
                if (mListener != null) {
                    String year = mYearLists.get(mPickerViewY.getValue());
                    String month = mMonthLists.get(mPickerViewM.getValue());
                    String day = mDayLists.get(mPickerViewD.getValue());

                    Calendar calendar = Calendar.getInstance();
                    try {
                        Date date = mDateFormatBack.parse(mYearLists.get(mPickerViewY.getValue()) + "年" + month);
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mListener.onTimeSelected(calendar.getTime());
                    mListener.onSelected(year, month, day);
                }
                // 销毁弹出框
                dismiss();
            }
        });

        mPickerViewY.setOnValueChangedListener(this);
        mPickerViewM.setOnValueChangedListener(this);
        mPickerViewD.setOnValueChangedListener(this);
        mDateFormat = new SimpleDateFormat("MM月dd日 E");
        mDateFormatBack = new SimpleDateFormat("yyyy年MM月dd日 E");
        Calendar calendar = Calendar.getInstance();
        initTime(calendar, true, true);

        return view;
    }

    /**
     * 初始化
     *
     * @param calendar
     * @param changeYear
     * @param changeMonth
     */
    private void initTime(Calendar calendar, boolean changeYear, boolean changeMonth) {

        GregorianCalendar now = (GregorianCalendar) GregorianCalendar.getInstance();

        String year = null;
        if (mYearLists != null) {
            year = mYearLists.get(mPickerViewY.getValue()) + "年";
        } else {
            year = String.valueOf(now.get(Calendar.YEAR)) + "年";
        }

        String month = null;
        if (mMonthLists != null) {
            month = mMonthLists.get(mPickerViewM.getValue()) + "月";
        } else {
            month = String.valueOf(now.get(Calendar.MONTH) + 1) + "月";
        }

        String day = null;
        if (mDayLists != null) {
            day = mDayLists.get(mPickerViewD.getValue()) + "日";
        } else {
            day = String.valueOf(now.get(Calendar.DAY_OF_MONTH)) + "日";
        }

        mDayLists = new ArrayList<>();
        if (Utils.getNumbers(month) == 1 || Utils.getNumbers(month) == 3 || Utils.getNumbers(month) == 5 || Utils.getNumbers(month) == 7 || Utils.getNumbers(month) == 8 || Utils.getNumbers(month) == 10 || Utils.getNumbers(month) == 12) {
            for (int i = 1; i <= 31; i++) {
                mDayLists.add(i + "日");
            }
        } else if (Utils.getNumbers(month) == 4 || Utils.getNumbers(month) == 6 || Utils.getNumbers(month) == 9 || Utils.getNumbers(month) == 11) {
            for (int i = 1; i <= 30; i++) {
                mDayLists.add(i + "日");
            }
        } else if (Utils.getNumbers(month) == 2) {
            if (Utils.getNumbers(year) % 400 == 0) {
                for (int i = 1; i <= 29; i++) {
                    mDayLists.add(i + "日");
                }
            } else {
                for (int i = 1; i <= 28; i++) {
                    mDayLists.add(i + "日");
                }
            }
        }

        setData(mPickerViewD, (String[]) (mDayLists.toArray(new String[mDayLists.size()])), day);

        if (changeMonth) {
            mMonthLists = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                mMonthLists.add(i + "月");
            }
            setData(mPickerViewM, (String[]) (mMonthLists.toArray(new String[mMonthLists.size()])), month);
        }
        if (changeYear) {
            mYearLists = new ArrayList<>();
            for (int i = now.get(Calendar.YEAR); i > -1000; i--) {
                if (i == 0) {
                    continue;
                }
                if (i > 0) {
                    mYearLists.add(i + "年");
                } else {
                    mYearLists.add("公元前" + Math.abs(i) + "年");
                }
            }
            setData(mPickerViewY, (String[]) (mYearLists.toArray(new String[mYearLists.size()])), year);
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

        if (picker.getId() == R.id.picker_year) {
            String currentValue = mYearLists.get(newVal);
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = mDateFormatBack.parse(mYearLists.get(newVal) + "年" + currentValue);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            initTime(calendar, false, true);

        } else if (picker.getId() == R.id.picker_month) {

            int currentValue = Utils.getNumbers(mMonthLists.get(newVal));
            Calendar calendar = Calendar.getInstance();
            initTime(calendar, false, false);

        } else if (picker.getId() == R.id.picker_date) {

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

    public interface OnSelectedTimeListener {
        void onTimeSelected(Date date);

        void onSelected(String year, String month, String day);
    }
}
