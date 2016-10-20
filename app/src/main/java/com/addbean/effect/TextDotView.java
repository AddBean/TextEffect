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

    public TextDotView(View v, TextDot mData) {
        this.mData = mData;
        Random r = new Random();
        mStartPoint = new Point(r.nextInt(1000), r.nextInt(1000));
        mTargetPoint = mData.getmLoc();
        mCurrPoint = new Point(mStartPoint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mData == null) return;
        int count = canvas.save();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setAlpha((int) (mData.getmAlpha() * 255));
        paint.setColor(mData.getmColor());
        canvas.drawCircle(mCurrPoint.x, mCurrPoint.y, mData.getmSize() / 2.4f, paint);
        canvas.restoreToCount(count);
    }

    @Override
    public void evolveDot(float value) {
        float dx = mTargetPoint.x - mCurrPoint.x;
        float dy = mTargetPoint.y - mCurrPoint.y;
        mCurrPoint.set((int) (mCurrPoint.x + dx * value), (int) (mCurrPoint.y + dy * value));
    }

}
