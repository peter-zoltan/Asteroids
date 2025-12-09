package me.peterzoltan.game;

import java.awt.*;

public interface Drawable {

    /**
     * Describes how the object is drawn.
     * @param graphics the Graphics object to which it will draw onto.
     */
    public void draw(Graphics graphics);

}
