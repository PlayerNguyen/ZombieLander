package com.mygdx.zombieland.utils;

import com.mygdx.zombieland.location.Location;

public class Rectangle {
    private final Location location;
    int width;
    int height;

    public boolean isCollided(Rectangle other) {
        float x1 = this.location.x - (float) this.width / 2;
        float y1 = this.location.y - (float) this.height / 2;
        float x2 = other.location.x - (float) other.width / 2;
        float y2 = other.location.y - (float) other.height / 2;

        if (x1 + this.width < x2 || x2 + other.width < x1) {
            return false;
        }
        return !(y1 + this.height < y2) && !(y2 + other.height < y1);
    }

    public Rectangle(Location location, int width, int height) {
        this.location = location;
        this.width = width;
        this.height = height;
    }
}
