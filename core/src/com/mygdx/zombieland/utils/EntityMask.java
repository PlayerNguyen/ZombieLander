package com.mygdx.zombieland.utils;

public class EntityMask {
    private final int top;
    private final int right;
    private final int bottom;
    private final int left;

    public EntityMask(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    @Override
    public String toString() {
        return "EntityMask{" +
                "top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", left=" + left +
                '}';
    }
}
