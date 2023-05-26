package com.mygdx.zombieland;

import java.awt.*;

final public class WindowLocation {
    private static WindowLocation instance;
    private int X;
    private int Y;

    private WindowLocation(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int FRAME_WIDTH = 1066;
        this.X = (screenSize.width - FRAME_WIDTH) / 2;
        int FRAME_HEIGHT = 630;
        this.Y = (screenSize.height - FRAME_HEIGHT) / 2;
    }

    public static WindowLocation getInstance(){
        if(instance== null){
            instance = new WindowLocation();
        }
        return instance;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }
}
