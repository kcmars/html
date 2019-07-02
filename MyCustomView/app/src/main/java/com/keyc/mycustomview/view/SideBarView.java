package com.keyc.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.keyc.mycustomview.R;

public class SideBarView extends View {

    private Paint mPaint;
    private int mWidth, mHeight;
    private String[] c = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z", "#"};

    private int textSize;
    private int index;
    private int choose = -1;
    private boolean mFlag;
//    private LetterChangeListener mChangeListener;
    private LetterTouchListener mTouchListener;

    public SideBarView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public SideBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public SideBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                14, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        if (mFlag) {
//			canvas.drawColor(Color.parseColor("#E5E5E5"));
        }
        float strHeight = (float) (mHeight / c.length);
        for (int i = 0; i < c.length; i++) {
            float x = mWidth / 2 - mPaint.measureText(c[i]) / 2;
            float y = strHeight * i + strHeight;
            if (choose == i) {
                mPaint.setColor(getResources().getColor(R.color.color_3));
            } else {
                mPaint.setColor(getResources().getColor(R.color.color_2));
            }
            canvas.drawText(c[i], x, y, mPaint);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        float y = event.getY();
        index = (int) (y / mHeight * c.length);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchListener.setLetterVisibility(View.VISIBLE);
                mTouchListener.setLetter(c[index]);
                choose = index;
                mFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (index > -1 && index < c.length) {
                    mTouchListener.setLetter(c[index]);
                    choose = index;
                    mFlag = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchListener.setLetterVisibility(View.GONE);
                choose = -1;
                mFlag = false;
                break;
        }
        return true;
    }

    public void setLetterTouchListener(LetterTouchListener listener) {
        mTouchListener = listener;
    }

    public interface LetterTouchListener {
        void setLetterVisibility(int visibility);
        void setLetter(String letter);
    }

}
