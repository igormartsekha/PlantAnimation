package com.example.imartsekha.plantanimation.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by imartsekha on 11/28/17.
 */

public class BubbleView extends View {
    int backgroundColor = Color.BLACK;

    Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    RectF rounRect = new RectF(0,0,0,0);


    public BubbleView(Context context) {
        this(context, null);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        circlePaint.setColor(this.backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getHeight() != getWidth()) {
            rounRect.set(0, 0, getWidth(), getHeight());
            canvas.drawRoundRect(rounRect, getWidth()/2, getWidth()/2, circlePaint);
        } else {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, circlePaint);
        }
    }
}
