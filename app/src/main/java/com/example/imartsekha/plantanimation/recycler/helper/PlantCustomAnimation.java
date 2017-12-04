package com.example.imartsekha.plantanimation.recycler.helper;

import android.view.View;

import com.example.imartsekha.plantanimation.recycler.Shape;

/**
 * Created by imartsekha on 12/4/17.
 */

public class PlantCustomAnimation {
    Shape startView;
    Shape finishView;
    boolean isReverse;

    private float currentX;
    private float currentY;
    private float currentWidth;
    private float currentHeight;

    public PlantCustomAnimation(Shape startView, Shape finishView, boolean isReverse) {
        this.startView = startView;
        this.finishView = finishView;
        this.isReverse = isReverse;
    }

    public void calculatePositionAnimation(float percentage) {
        float finishX = finishView.getX();
        float finishY = finishView.getY();
        float finishWidth = finishView.getWidth();
        float finishHeight = finishView.getHeight();


        float startX = startView.getX();
        float startY = startView.getY();
        float startWidth = startView.getWidth();
        float startHeight = startView.getHeight();

        currentX = startX;
        currentY = startY;

        currentWidth = startWidth;
        currentHeight = startHeight;


        if(percentage != 0) {
            float stepX = ((finishX - startX) / 100.0f) * (int)percentage;
            float stepY = ((finishY - startY) / 100.0f) * (int)percentage;

            float stepWidth = ((finishWidth - startWidth) / 100.0f) * (int)percentage;
            float stepHeight = ((finishHeight - startHeight) / 100.0f) * (int)percentage;


            if((int)percentage >= 100) {
                stepX = finishX-startX;
                stepY = finishY-startY;

                stepWidth = finishWidth - finishWidth;
                stepHeight = finishHeight - finishHeight;
            }

            if(isReverse) {
                currentX = finishX - stepX;
                currentY = finishY - stepY;

                currentWidth = finishWidth - stepWidth;
                currentHeight = finishHeight - stepHeight;
            } else {
                currentX = startX + stepX;
                currentY = startY + stepY;

                currentWidth = startWidth + stepWidth;
                currentHeight = startHeight + stepHeight;
            }
        }
    }

    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public float getCurrentWidth() {
        return currentWidth;
    }

    public float getCurrentHeight() {
        return currentHeight;
    }
}
