package com.mygdx.zombieland;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class MapRenderer<T> {
    private final List<List<Set<T>>> map;

    public MapRenderer(int width, int height) {
        this.map = new ArrayList<>(width);
        for (int x = 0; x < width; x++) {
            this.map.add(new CopyOnWriteArrayList<Set<T>>());
            for (int y = 0; y < height; y++) {
                CopyOnWriteArraySet<T> ts = new CopyOnWriteArraySet<>();
                this.map.get(x).add(ts);
            }
        }
    }

    public List<List<Set<T>>> getMap() {
        return map;
    }

    public Set<T> get(int x, int y) {
        return this.map.get(x).get(y);
    }

}
