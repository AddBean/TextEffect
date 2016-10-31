package com.addbean.effect;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AddBean on 2016/10/20.
 */

public class TextEffectView extends View {
    private String mText;
    private Bitmap mLowBitmap;
    private int mMinHeight = 12;//低分辨率的高度；
    private volatile List<TextDotView> mDots = new ArrayList<>();
    private List<TempModel> mDotsPrePos = new ArrayList<>();
    private float mScaleRate = 10;//放大倍数；
    private RectF mTragetRect;
    private boolean mIsShowColor;

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
        String[] mTextS = mText.split("\n");
        String maxStr = "";

        for (String s : mTextS) {
            if (s.length() > maxStr.length())
                maxStr = s;
        }
        int width = 0;
        for (int i = 0; i < maxStr.length(); i++) {
            Pattern p1 = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p1.matcher(String.valueOf(maxStr.charAt(i)));
            if (m.matches()) {
                width += mMinHeight;
            } else {
                width += mMinHeight / 2;
            }
        }
        Bitmap bmp = Bitmap.createBitmap(width, mTextS.length * mMinHeight, Bitmap.Config.ARGB_8888);
        Canvas canves = new Canvas(bmp);
        TextPaint p = new TextPaint();
        p.setColor(Color.BLACK);
        p.setTextSize(mMinHeight);
        float textBaseY = mMinHeight / 2 + (Math.abs(p.ascent()) - p.descent()) / 2;
        int i = 0;
        for (String s : mTextS) {
            canves.drawText(s, 0, i * mMinHeight + textBaseY, p);
            i++;
        }
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
                    mDotsPrePos.add(new TempModel(new Point(col, row), pixel));
                }
            }
        }
        setToTargetCoord(mDotsPrePos);
    }

    private void setToTargetCoord(List<TempModel> mDotsPrePos) {
        for (TempModel p : mDotsPrePos) {
            Point point = new Point((int) (p.point.x * mScaleRate + getWidth() / 2 - mTragetRect.width() / 2), (int) (p.point.y * mScaleRate + getHeight() / 2 - mTragetRect.height() / 2));
            Random r = new Random();
            TextDot dot = null;
            if (mIsShowColor)
                dot = new TextDot((int) mScaleRate, p.color, point, 1, 0);
            else
                dot = new TextDot((int) mScaleRate, Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)), point, 1, 0);
            TextDotView dotView = new TextDotView(this, dot);
            mDots.add(dotView);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int count = canvas.save();
        for (Iterator it = mDots.iterator(); it.hasNext(); ) {
            TextDotView view = ((TextDotView) it.next());
            if (view.getmData().getmState() == 2) {
                Log.e("evolveDot ", "删除");
                it.remove();
            } else {
                view.onDraw(canvas);
            }
        }
        canvas.restoreToCount(count);
    }

    ValueAnimator mAnim = null;

    public void startShow(String text, int durTime, float scaleRate, int resole, boolean isShowColor) {
        mScaleRate = scaleRate;
        mMinHeight = resole;
        mIsShowColor = isShowColor;
        setText(text);
        if (mAnim != null) mAnim.cancel();
        mAnim = ValueAnimator.ofFloat(0f, 1f);
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                evolveDots(value);
                invalidate();
                if (value == 1f && mOnDotLifeListener != null)
                    mOnDotLifeListener.onAnimFinish();
            }
        });
        //设置插值器
        mAnim.setInterpolator(new DecelerateInterpolator());
        mAnim.setDuration(durTime);
        mAnim.start();
    }

    /**
     * 更新dot坐标；
     */
    private void evolveDots(float value) {
        for (TextDotView view : mDots) {
            view.evolveDot(value);
        }
    }

    public class TempModel {
        public Point point;
        public int color;

        public TempModel(Point point, int color) {
            this.point = point;
            this.color = color;
        }
    }

    private OnDotLifeListener mOnDotLifeListener;

    public void setOnDotLifeListener(OnDotLifeListener mOnDotLifeListener) {
        this.mOnDotLifeListener = mOnDotLifeListener;
    }

    public interface OnDotLifeListener {
        void onAnimFinish();
    }

}
