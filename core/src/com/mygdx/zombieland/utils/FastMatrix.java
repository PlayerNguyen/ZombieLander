package com.mygdx.zombieland.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FastMatrix<T> {

    private final Map<Key, T> internalMap;

    public FastMatrix() {
        this.internalMap = new ConcurrentHashMap<>();
    }

    public void set(int x, int y, T value) {
        this.internalMap.put(new Key(x, y), value);
    }

    public T get(int x, int y) {
        return this.internalMap.get(new Key(x, y));
    }

    public boolean contains(int x, int y) {
        return this.internalMap.containsKey(new Key(x, y));
    }

    public void clear() {
        this.internalMap.clear();
    }

    public Set<Map.Entry<Key, T>> entrySet() {
        return this.internalMap.entrySet();
    }

    public static class Key {

        public final int x;
        public final int y;

        public Key(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return x == key.x && y == key.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

    }
}
