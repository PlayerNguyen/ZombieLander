package com.mygdx.zombieland.map;

import java.util.Arrays;

public class Map {

    private static Map instance;
    private int arr [][];

    private Map() {
        arr=  new int[1000][1000];
    }

    public static synchronized Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }

    public static void setInstance(Map instance) {
        Map.instance = instance;
    }

    public int[][] getArr() {
        return arr;
    }

    public void setArr(int i, int j, int val) {
        this.arr[i][j] = val;
    }

    @Override
    public String toString() {
        return "Map{" +
                "arr=" + Arrays.deepToString(arr) +
                '}';
    }
}

