package com.example.imartsekha.plantanimation.recycler;

/**
 * Created by imartsekha on 11/27/17.
 */

public class Shape {
    private int x;
    private int y;
    private int width;
    private int height;
    private ShapeType type;

    public Shape(int x, int y, int width, int height, ShapeType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public enum ShapeType {
        CIRCLE,
        RECTANGLE
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ShapeType getType() {
        return type;
    }
}
