package com.mygdx.zombieland.utils;

import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;

import java.util.Comparator;

public class CoordinateHelper {
    public static class Coordinate implements Comparable<Coordinate> {
        public float x;
        public float y;


        public Coordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Coordinate(int x, int y) {
            this.x = (float) x;
            this.y = (float) y;
        }

        public Coordinate(Vector2D v) {
            this.x = (float) v.x;
            this.y = (float) v.y;
        }

        public Coordinate(Location location) {
            this.x = location.x;
            this.y = location.y;
        }

        @Override
        public int compareTo(Coordinate o) {
            if (this.x == o.x & this.y == o.y) return 0;
            // Compare by length
            return (int) Math.sqrt(Math.pow(this.x - o.x, 2) + Math.pow(this.y - o.y, 2));
        }

        @Override
        public String toString() {
            return "Coordinate{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
