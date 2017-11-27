package com.example.imartsekha.plantanimation.recycler;

/**
 * Created by imartsekha on 11/27/17.
 */

public class CircleShape extends Shape{
        private int x;
        private int y;
        private int radius;

    public CircleShape(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getX() {
            return x;
        }

    public int getY() {
            return y;
        }

    public int getRadius() {
            return radius;
        }

}
