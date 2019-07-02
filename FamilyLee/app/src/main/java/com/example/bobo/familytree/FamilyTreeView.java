package com.example.bobo.familytree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class FamilyTreeView extends FrameLayout {

    private FamilyTree mFamilyTree;
    private Paint mPaint;

    public FamilyTreeView(@NonNull Context context) {
        super(context);
        init();
    }

    public FamilyTreeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FamilyTreeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2.f);
        mPaint.setColor(Color.parseColor("#999999"));
        mPaint.setAntiAlias(true);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFamilyTree != null) {
            this.draw(mFamilyTree, 0.f, mPaint, canvas);
        }
    }

    public FamilyTree getFamilyTree() {
        return mFamilyTree;
    }

    public void setFamilyTree(FamilyTree mFamilyTree) {
        this.mFamilyTree = mFamilyTree;
        invalidate();
    }

    private void draw(FamilyTree rootTree, float depth, Paint paint, Canvas canvas) {
        canvas.drawCircle((float) (rootTree.getX() * 30 * 1.5 + 115f), (float) (depth * 30f * 1.5 + 115f), 15.f, paint);
        canvas.drawText(rootTree.getTree().getName(),(float) (rootTree.getX() * 30 * 1.5 + 115f), (float) (depth * 30f * 1.5 + 115f),paint);
        Log.e("TAG", "draw: " + rootTree);
        for (FamilyTree childTree : rootTree.getChildren()) {
            canvas.drawLine((float) (rootTree.getX() * 30 * 1.5 + 115f), (float) (depth * 30f * 1.5 + 115f), (float) (childTree.getX() * 30 * 1.5 + 115f), (float) ((depth + 1) * 30f * 1.5 + 115f), paint);
            this.draw(childTree, depth + 1, paint, canvas);
        }
    }
}
