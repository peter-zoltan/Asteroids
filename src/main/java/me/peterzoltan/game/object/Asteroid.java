package me.peterzoltan.game.object;

import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;
import me.peterzoltan.game.Movable;
import me.peterzoltan.util.Size;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Asteroid extends GameObject implements Drawable, Movable {

    int hitpoints;
    Size size;
    BufferedImage image;

    /**
     * Loads the image that is used to draw the Asteroid and sets hitpoints, both taking size into account.
     * @param x starting x coordinate.
     * @param y starting y coordinate.
     * @param orientation orientation, pointing towards the middle.
     * @param size size, either SMALL, MEDIUM OR LARGE.
     */
    public Asteroid(int x, int y, int orientation, Size size) {
        String filePath = "";
        this.size = size;
        switch(size) {
            case SMALL -> {
                hitpoints = 1;
                filePath = "src/main/resources/asteroid_small.png";
            }
            case MEDIUM -> {
                hitpoints = 2;
                filePath = "src/main/resources/asteroid_medium.png";
            }
            case LARGE -> {
                hitpoints = 3;
                filePath = "src/main/resources/asteroid_large.png";
            }
        }
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.out.println("Error loading asteroid size " + size);
        }
        setLocation(x, y);
        this.orientation = orientation;
        radius = image.getWidth() / 2;
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
     * Updates the Asteroid's position. Each Asteroid travels in a straight line at a set pace as decided by it's orientation.
     */
    @Override
    public void updatePosition() {
        setLocation(
            coordinate.x + (int) (Math.sin(Math.toRadians(orientation)) * 5),
            coordinate.y - (int) (Math.cos(Math.toRadians(orientation)) * 5)
        );
    }

    /**
     * Register's being hit by a Projectile, losing 1 hipoint.
     */
    public void registerHit() {
        hitpoints--;
    }

    /**
     * Returns whether it's hitpoints have reached 0.
     * @return whether it's hitpoints have reached 0.
     */
    public boolean isDestroyed() {
        return hitpoints == 0;
    }

    /**
     * Returns the damage it does on collision with the Planet according to it's size.
     * @return SMALL -> 1, Medium -> 2, LARGE -> 3.
     */
    public int getDamage() {
        switch (size) {
            case MEDIUM -> {
                return 2;
            }
            case LARGE -> {
                return 3;
            }
            default -> {
                return 1;
            }
        }
    }

}
