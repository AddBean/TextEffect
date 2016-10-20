package com.addbean.effect;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by AddBean on 2016/10/20.
 */

public class TextEffectView extends View {
    private String mText;
    private Bitmap mLowBitmap;
    private int mMinHeight = 20;//低分辨率的高度；
    private List<TextDotView> mDots = new ArrayList<>();
    private List<Point> mDotsPrePos = new ArrayList<>();
    private float mScaleRate = 10;//放大倍数；
    private RectF mTragetRect;

    public TextEffectView(Context context) {
        super(context);
    }

    public TextEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextEffectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text) {
        this.mText = text;
        mDots.clear();
        mDotsPrePos.clear();
        mLowBitmap = createLowResolutionBitmapByText();
        mTragetRect = new RectF(0, 0, mLowBitmap.getWidth() * mScaleRate, mLowBitmap.getHeight() * mScaleRate);
        setCoordByBitmap(mLowBitmap);
        invalidate();
    }

    /**
     * 创建一个低分辨率的文字位图，从而像素化文字；
     *
     * @return
     */
    private Bitmap createLowResolutionBitmapByText() {
        if (TextUtils.isEmpty(mText)) return null;
        int width = mText.length() * mMinHeight;
        Bitmap bmp = Bitmap.createBitmap(width, mMinHeight, Bitmap.Config.ARGB_8888);
        Canvas canves = new Canvas(bmp);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setTextSize(mMinHeight * 0.8f);
        canves.drawText(mText, 0, mMinHeight, p);
        return bmp;
    }

    /**
     * 取得低分辨率下的bitmap黑点坐标；
     *
     * @param bitmap
     */
    private void setCoordByBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = bitmap.getPixel(col, row);// ARGB
                if (pixel != 0) {
                    mDotsPrePos.add(new Point(col, row));
                }
            }
        }
        setToTargetCoord(mDotsPrePos);
    }

    private void setToTargetCoord(List<Point> mDotsPrePos) {
        for (Point p : mDotsPrePos) {
            Point point = new Point((int) (p.x * mScaleRate), (int) (p.y * mScaleRate));
            Random r = new Random();
            TextDot dot = new TextDot((int) mScaleRate, Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)), point, 1);
            mDots.add(new TextDotView(this,dot));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int count = canvas.save();
        for (TextDotView view : mDots) {
            view.onDraw(canvas);
        }
        canvas.restoreToCount(count);
    }

    public void startAnim(String text) {
        setText(text);
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                evolveDots(value);
                invalidate();
            }
        });
        anim.setDuration(4000);
        anim.start();
    }

    /**
     * 更新dot坐标；
     */
    private void evolveDots(float value) {
        for (TextDotView view : mDots) {
            view.evolveDot(value);
        }
    }
}
