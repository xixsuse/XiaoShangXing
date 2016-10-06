package com.xiaoshangxing.xiaoshang.Calendar.Decorator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

/**
 * Span to draw a dot centered under a section of text
 */
public class MyDotSpan implements LineBackgroundSpan {

    /**
     * Default radius used
     */
    public static final float DEFAULT_RADIUS = 3;

    private final float radius;
    private final int color;
    private boolean isAlpha;

    /**
     * Create a span to draw a dot using default radius and color
     *
     * @see #MyDotSpan(float, int)
     * @see #DEFAULT_RADIUS
     */
    public MyDotSpan() {
        this.radius = DEFAULT_RADIUS;
        this.color = 0;
    }

    /**
     * Create a span to draw a dot using a specified color
     *
     * @param color color of the dot
     * @see #MyDotSpan(float, int)
     * @see #DEFAULT_RADIUS
     */
    public MyDotSpan(int color) {
        this.radius = DEFAULT_RADIUS;
        this.color = color;
    }

    /**
     * Create a span to draw a dot using a specified radius
     *
     * @param radius radius for the dot
     * @see #MyDotSpan(float, int)
     */
    public MyDotSpan(float radius) {
        this.radius = radius;
        this.color = 0;
    }

    /**
     * Create a span to draw a dot using a specified radius and color
     *
     * @param radius radius for the dot
     * @param color  color of the dot
     */
    public MyDotSpan(float radius, int color, boolean isAlpha) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }
        if (isAlpha) {
            paint.setAlpha(40);
        }
        canvas.drawCircle((left + right) / 2, (bottom + top) / 2, radius, paint);
        paint.setColor(oldColor);
    }
}
