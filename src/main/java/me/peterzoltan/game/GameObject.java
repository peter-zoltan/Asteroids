package me.peterzoltan.game;

import java.awt.Point;

public class GameObject {

    /**
     * Coordinate of the GameObject on the canvas.
     */
    protected Point coordinate = new Point();

    /**
     * Orientation of the GameObject in degrees, 0 being upwards, increasing clockwise.
     */
    protected int orientation;

    /**
     * Radius of the GameObject, used for calculating whether two collide.
     */
    protected int radius;

    public Point getLocation() {
        return coordinate;
    }

    /**
     * Sets the location of the GameObject to the given coordinates.
     * @param x the x coordinate on the canvas.
     * @param y the y coordinate on the canvas.
     */
    public void setLocation(int x, int y) {
        coordinate.setLocation(x, y);
    }

    /**
     * Gets the radius of the GameObject.
     * @return the radius of the GameObject.
     */
    public int getRadius() {
        return radius;
    }

}
