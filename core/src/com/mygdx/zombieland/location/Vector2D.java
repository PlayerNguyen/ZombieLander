package com.mygdx.zombieland.location;

public class Vector2D {

    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(Vector2D vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public Vector2D scalar(double amount) {
        this.x *= amount;
        this.y *= amount;
        return this;
    }

    public void normalize() {
        double length = Math.sqrt(x * x + y * y);
        if (length != 0.0) {
            double s = 1.0 / length;
            x *= s;
            y *= s;
        }
    }

    public Vector2D getInverseVector(Location root) {
        double length = root.distance(new Location((float) this.x, (float) this.y));
        return new Vector2D(-length * this.x, -length * this.y);
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
