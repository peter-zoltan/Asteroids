package me.peterzoltan.game;

import java.awt.Point;

public class GameObject {

    protected Point coordinate = new Point();

    protected int orientation;

    protected int radius;

    public Point getLocation() {
        return coordinate;
    }

    public void setLocation(int x, int y) {
        coordinate.setLocation(x, y);
    }

    public int getRadius() {
        return radius;
    }

}
