package com.addbean.effect;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import java.util.Random;

/**
 * Created by AddBean on 2016/10/20.
 */

public class TextDotView implements ITextDot {

    private TextDot mData;
    private Point mStartPoint;
    private Point mTargetPoint;
    private Point mCurrPoint;
    private int mRectBond = 160;//起始点daxiao;
    private int mAlpha = 0;
    private float mCurSize = 0;
    private float mGapSize = 0;

    public TextDotView(View v, TextDot mData) {
        this.mData = mData;
        mRectBond = v.getWidth() / 3;
        mStartPoint = getRandomPoint(v);
        mTargetPoint = mData.getmLoc();
        mGapSize = new Random().nextInt(mData.getmSize());
        mCurrPoint = new Point(mStartPoint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mData == null) return;
        int count = canvas.save();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(mData.getmColor());
        paint.setAlpha(mAlpha);
        canvas.drawCircle(mCurrPoint.x, mCurrPoint.y, mCurSize, paint);
        canvas.restoreToCount(count);
    }

    @Override
    public void evolveDot(float value) {
        if (mData.getmState() == 0) {
            onEnter(value);
        } else {
            onDismiss(value);
            if (value == 1.0f)
                mData.setmState(2);
        }
        if (mData.getmState() == 0 && value == 1.0f)
            mData.setmState(1);
    }

    /**
     * 入场动画;
     *
     * @param value
     */
    private void onEnter(float value) {
        float dx = mTargetPoint.x - mStartPoint.x;
        float dy = mTargetPoint.y - mStartPoint.y;
        mAlpha = (int) (value * 255);
        mCurSize = mGapSize * (1-value) + mData.getmSize() / 2.4f;
        mCurrPoint.set((int) (mStartPoint.x + dx * value), (int) (mStartPoint.y + dy * value));
    }

    /**
     * 出场动画；
     *
     * @param value
     */
    private void onDismiss(float value) {
        float dx = mTargetPoint.x - mStartPoint.x;
        float dy = mTargetPoint.y - mStartPoint.y;
        mAlpha = 255 - (int) (value * 255);
        mCurSize = mData.getmSize() / 2.4f*(1-value);
        mCurrPoint.set((int) (mTargetPoint.x - dx * value), (int) (mTargetPoint.y - dy * value));
    }

    public Point getRandomPoint(View v) {
        Random r = new Random();
        float baseR = Math.max(v.getWidth(), v.getHeight()) / 2;
        float R = r.nextInt((int) baseR);
        Double rd = r.nextFloat() * 2 * Math.PI;
        return new Point((int) (R * Math.cos(rd)) + v.getWidth() / 2, (int) (R * Math.sin(rd)) + v.getHeight() / 2);
    }

    public TextDot getmData() {
        return mData;
    }
}
