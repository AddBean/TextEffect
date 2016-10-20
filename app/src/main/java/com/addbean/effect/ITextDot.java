package com.addbean.effect;

import android.graphics.Canvas;

/**
 * Created by AddBean on 2016/10/20.
 */

public interface ITextDot {
    void onDraw(Canvas canvas);
    void evolveDot(float value);
}
