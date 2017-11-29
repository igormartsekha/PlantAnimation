package com.example.imartsekha.plantanimation.recycler.adapter;

import android.support.annotation.ColorInt;
import android.view.View;

import java.util.List;

/**
 * Created by imartsekha on 11/27/17.
 */

public class BubbleCircle {
    @ColorInt int color;
    int score;
    String text;
    String bubleId;
    List<BubbleDirection> bubbleDirections;
    View overlayView;

    private BubbleCircle(Builder builder) {
        color = builder.color;
        score = builder.score;
        text = builder.text;
        bubleId = builder.bubleId;
        bubbleDirections = builder.bubbleDirections;
    }


    public static class BubbleDirection {
        private int startViewItemType;
        private int finishViewItemType;

        public BubbleDirection(int startViewItemType, int finishViewItemType) {
            this.startViewItemType = startViewItemType;
            this.finishViewItemType = finishViewItemType;
        }

        public int getStartViewItemType() {
            return startViewItemType;
        }

        public int getFinishViewItemType() {
            return finishViewItemType;
        }
    }

    public int getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public String getText() {
        return text;
    }

    public String getBubleId() {
        return bubleId;
    }

    public List<BubbleDirection> getBubbleDirections() {
        return bubbleDirections;
    }

    public View getOverlayView() {
        return overlayView;
    }

    public static final class Builder {
        private int color;
        private int score;
        private String text;
        private String bubleId;
        private List<BubbleDirection> bubbleDirections;

        public Builder() {
        }

        public Builder color(int val) {
            color = val;
            return this;
        }

        public Builder score(int val) {
            score = val;
            return this;
        }

        public Builder text(String val) {
            text = val;
            return this;
        }

        public Builder bubleId(String val) {
            bubleId = val;
            return this;
        }

        public Builder bubbleDirections(List<BubbleDirection> val) {
            bubbleDirections = val;
            return this;
        }

        public BubbleCircle build() {
            return new BubbleCircle(this);
        }
    }

    public void setOverlayView(View overlayView) {
        this.overlayView = overlayView;
    }
}
