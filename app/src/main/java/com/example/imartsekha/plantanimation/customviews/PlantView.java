package com.example.imartsekha.plantanimation.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.imartsekha.plantanimation.R;

import java.util.ArrayList;

/**
 * Created by imartsekha on 11/27/17.
 */

public class PlantView extends View {
    private final int CIRCLE_COUNT = 4;
    private final int ANGLE_POINT_LINE_LEFT = 60;
    private final int ANGLE_POINT_LINE_RIGHT = 120;

    private int lineColor;
    private int lineThick;

    private int circleColor;
    private int circleRadius;
    private int scoreCircleColor;
    private int scoreCircleRadius;

    private int bottomTreeHeight;

    private int halfWidth;

    Paint linePaint;
    Paint circlePaint;
    Paint scoreCirclePaint;
    Rect mainRect;


    CircleInfo scoreCircleInfo;
    ArrayList<CircleInfo> circles = new ArrayList<>();


    public PlantView(Context context) {
        this(context, null);
    }

    public PlantView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlantView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlantView, defStyleAttr, 0);

        scoreCircleColor = a.getColor(R.styleable.PlantView_scoreCircleColor, Color.BLACK);
        scoreCircleRadius = a.getDimensionPixelSize(R.styleable.PlantView_scoreCircleRadius, 0);

        circleColor = a.getColor(R.styleable.PlantView_circleColor, Color.BLACK);
        circleRadius = a.getDimensionPixelSize(R.styleable.PlantView_circleRadius, 0);

        lineColor = a.getColor(R.styleable.PlantView_lineColor, Color.BLACK);
        bottomTreeHeight = a.getDimensionPixelSize(R.styleable.PlantView_bottomTreeHeight, 0);
        lineThick = a.getDimensionPixelSize(R.styleable.PlantView_lineThick, 0);


        // init Paint for lines
        this.linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineThick);

        // init Paint for circles
        this.circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleColor);

        // init Paint for score Circle
        this.scoreCirclePaint = new Paint();
        scoreCirclePaint.setStyle(Paint.Style.FILL);
        scoreCirclePaint.setColor(scoreCircleColor);

        // init mainRect where was all circles
        this.mainRect = new Rect();

        // Init info for circles
        this.scoreCircleInfo = new CircleInfo();
        for(int i=0; i < CIRCLE_COUNT; i++) {
            this.circles.add(new CircleInfo());
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


        halfWidth = getWidth()/2;


        int scoreCircleEndY = mainRect.top+2*scoreCircleRadius;
        this.mainRect.set(getPaddingLeft(), getPaddingTop(), getWidth()-getPaddingRight(), getHeight() -getPaddingBottom());
        setScoreCircleInfo(halfWidth, this.mainRect.top+scoreCircleRadius, scoreCircleRadius);

        int previusCircleEnd = scoreCircleEndY;
        for(int i=0; i < CIRCLE_COUNT; i++) {
            // draw first circle
            int startX = 0;
            int anglePosition = 0;
            if(i%2 == 0) {
                startX = this.mainRect.left + circleRadius;
                anglePosition = ANGLE_POINT_LINE_LEFT;
            } else {
                startX = this.mainRect.right - circleRadius;
                anglePosition = ANGLE_POINT_LINE_RIGHT;
            }

            int startY = previusCircleEnd+circleRadius;

            // calculate point on 300
            double lineStartX = startX + circleRadius * Math.cos(Math.toRadians(anglePosition));
            double lineStartY = startY + circleRadius * Math.sin(Math.toRadians(anglePosition));

//            canvas.drawLine((float) lineStartX, (float)lineStartY, bottomTreeEndPointX, bottomTreeEndPointY, linePaint);
            previusCircleEnd = previusCircleEnd+2*circleRadius;
            setCircleInfo(i, startX, startY, circleRadius);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw main container

        //draw bottom line
        int bottomTreeEndPointX = halfWidth;
        int bottomTreeEndPointY = this.mainRect.bottom-bottomTreeHeight;
        canvas.drawLine(bottomTreeEndPointX,bottomTreeEndPointY,bottomTreeEndPointX, this.mainRect.bottom,linePaint);

        // draw score circle
        int scoreCircleEndY = mainRect.top+2*scoreCircleRadius;
        canvas.drawCircle(scoreCircleInfo.getX(), scoreCircleInfo.getY(), scoreCircleInfo.getRadius(), scoreCirclePaint);
        canvas.drawLine(bottomTreeEndPointX,scoreCircleEndY, bottomTreeEndPointX, bottomTreeEndPointY, linePaint);

        for(int i=0; i < CIRCLE_COUNT; i++) {
            CircleInfo circleInfo = circles.get(i);
            canvas.drawCircle(circleInfo.getX(), circleInfo.getY(), circleInfo.getRadius(), circlePaint);
            int anglePosition = 0;
            if(i%2 == 0) {
                anglePosition = ANGLE_POINT_LINE_LEFT;
            } else {
                anglePosition = ANGLE_POINT_LINE_RIGHT;
            }

            double lineStartX = circleInfo.getX() + circleRadius * Math.cos(Math.toRadians(anglePosition));
            double lineStartY = circleInfo.getY() + circleRadius * Math.sin(Math.toRadians(anglePosition));

            canvas.drawLine((float) lineStartX, (float)lineStartY, bottomTreeEndPointX, bottomTreeEndPointY, linePaint);
        }
    }

    private void setScoreCircleInfo(int centerX, int centerY, int radius) {
        scoreCircleInfo.setX(centerX);
        scoreCircleInfo.setY(centerY);
        scoreCircleInfo.setRadius(radius);
    }

    private void setCircleInfo(int index, int centerX, int centerY, int radius) {
        if(index >= this.circles.size())
            throw new IllegalArgumentException();

        CircleInfo circleInfo = this.circles.get(index);
        circleInfo.setX(centerX);
        circleInfo.setY(centerY);
        circleInfo.setRadius(radius);
    }

    public CircleInfo getScoreCircleInfo() {
        return scoreCircleInfo.clone();
    }

    public CircleInfo getCircleInfo(int index) {
        if(index >= this.circles.size())
            throw new IllegalArgumentException();

        return this.circles.get(index).clone();
    }


    public static class CircleInfo implements Cloneable {
        private int x;
        private int y;
        private int radius;

        public CircleInfo() {
            super();
        }
        public CircleInfo(CircleInfo circleInfo) {
            super();
            this.x = circleInfo.getX();
            this.y = circleInfo.getY();
            this.radius = circleInfo.getRadius();
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        @Override
        protected CircleInfo clone() {
            return new CircleInfo(this);
        }
    }



}
