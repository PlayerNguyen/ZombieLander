package com.mygdx.zombieland.path;

import com.mygdx.zombieland.location.Vector2D;

public class Path implements Comparable<Path> {
    private final Vector2D direction;
    private final double costToArrived;

    public Path(Vector2D direction, double costToArrived) {
        this.direction = direction;
        this.costToArrived = costToArrived;
    }

    public double getCostToArrived() {
        return costToArrived;
    }

    public Vector2D getDirection() {
        return direction;
    }


    @Override
    public int compareTo(Path o) {
        return Double.compare(this.costToArrived, o.costToArrived);
    }
}
