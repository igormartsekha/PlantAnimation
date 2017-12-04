package com.example.imartsekha.plantanimation.recycler.helper;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by imartsekha on 11/30/17.
 */

public class ScrollingHelper {
    private int orientation;
    private float currentPrevProgress = 0.0f;
    private float currentProgress = 0.0f;
    private DIRECTION currentDirection;

    private float currentPosition = 0.0f;

    public ScrollingHelper(int orientation) {
        this.orientation = orientation;
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        currentPrevProgress = currentProgress;
        float progress = 0.0f;

        if(orientation == OrientationHelper.VERTICAL) {
            progress = Math.abs((recyclerView.computeVerticalScrollOffset() / ((float) recyclerView.computeVerticalScrollExtent() / 100.0f)));
        } else {
            progress = Math.abs((recyclerView.computeHorizontalScrollOffset() / ((float) recyclerView.computeHorizontalScrollExtent() / 100.0f)));
        }

        if(progress%100 == 0.0f && currentProgress > 50.0f) {
            progress = 100.0f;
        } else {
            progress = progress%100;
        }
        currentProgress = progress;

        if (Math.abs(currentPrevProgress - currentProgress) > 50.0f) {
            currentPrevProgress = currentProgress;
        }

        detectDirection(recyclerView);
    }

    private void detectDirection(RecyclerView recyclerView) {
        if(currentPosition > recyclerView.computeHorizontalScrollOffset()) {
            currentDirection = (orientation == OrientationHelper.HORIZONTAL) ? DIRECTION.LEFT : DIRECTION.DOWN;
        } else {
            currentDirection = (orientation == OrientationHelper.HORIZONTAL) ? DIRECTION.RIGHT : DIRECTION.UP;
        }
        if(orientation == OrientationHelper.HORIZONTAL) {
            currentPosition = recyclerView.computeHorizontalScrollOffset();
        } else {
            currentPosition = recyclerView.computeVerticalScrollOffset();
        }
    }

    public boolean isReverse() {
        return (getPreviousProgress() >= getCurrentProgress()) && getCurrentDirection() == ((orientation == OrientationHelper.HORIZONTAL) ?  DIRECTION.LEFT : DIRECTION.UP);
    }

    public float getCurrentProgress() {
        return currentProgress;
    }

    private float getPreviousProgress() {
        return currentPrevProgress;
    }

    public DIRECTION getCurrentDirection() {
        return currentDirection;
    }

    public enum DIRECTION {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
