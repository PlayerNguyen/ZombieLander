package com.mygdx.zombieland;

import java.util.ArrayList;

public class MapRenderer<T> {
    private final ArrayList<ArrayList<T>> map;

    public MapRenderer(int width, int height, T initialValue) {
        this.map = new ArrayList<>(width);
        for (int x = 0; x < width; x++) {
            this.map.add(new ArrayList<T>(height));
            for (int y = 0; y < height; y++) {
                this.map.get(x).add(initialValue);
            }
        }
    }

    public ArrayList<ArrayList<T>> getMap() {
        return map;
    }

    public T get(int x, int y) {
        return this.map.get(x).get(y);
    }

}
