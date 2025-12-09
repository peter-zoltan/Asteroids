package me.peterzoltan.game.object;

import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Planet extends GameObject implements Drawable {

    BufferedImage image;
    int hitpoints;

    /**
     * Loads the image that is used to draw the Planet, inherits the location of the middle of the Canvas.
     * Sets hitpoints to what has been passed from the chosen Difficulty.
     * @param hitpoints hitpoints of the Planet;
     * @param x half of the Canvas's width;
     * @param y half of the Canvas's heighth;
     */
    public Planet(int hitpoints, int x, int y) {
        try {
            image = ImageIO.read(new File("src/main/resources/planet-earth.png"));
        } catch (IOException e) {
            System.out.println("Error loading planet");
        }
        setLocation(x, y);
        radius = image.getWidth() / 2;
        this.hitpoints = hitpoints;
    }

    /**
     * Draws image to the Graphics object passed as a parameter.
     * Radius is subtracted from the coordinates so that coordinate is at the center of the image.
     * @param graphics the Graphics object to which it will draw onto.
     */
    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(image, coordinate.x - radius, coordinate.y - radius, null);
    }

    /**
     * Register's a hit to the Planet, lowering it's hitpoints according to the damage suffered.
     * @param damage the damage suffered.
     */
    public void registerHit(int damage) {
        hitpoints -= damage;
    }

    /**
     * Returns whether the Planet's hitpoints have reached or fallen below 0, essentially whether the game is over.
     * @return whether the Planet's hitpoints have reached or fallen below 0.
     */
    public boolean isDestroyed() {
        return hitpoints <= 0;
    }

}
