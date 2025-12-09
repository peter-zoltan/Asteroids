package me.peterzoltan.game.object;

import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;
import me.peterzoltan.game.Movable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Projectile extends GameObject implements Drawable, Movable {

    BufferedImage image;

    /**
     * Loads the image that is used to draw the Projectile, inherits the location and orientation of the Spaceship firing it.
     * @param spaceShipX x coordinate of the Spaceship.
     * @param spaceShipY y coordinate of the Spaceship.
     * @param orientation orieantation of the Spaceship.
     */
    public Projectile(int spaceShipX, int spaceShipY, int orientation) {
        try {
            image = ImageIO.read(new File("src/main/resources/projectile.png"));
        } catch (IOException e) {
            System.out.println("Error loading projectile");
        }
        this.orientation = orientation;
        setLocation(spaceShipX, spaceShipY);
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
     * Updates the Projectile's position. Each Projectile travels in a straight line at a set pace as decided by it's orientation.
     */
    @Override
    public void updatePosition() {
        setLocation(
            coordinate.x + (int) (Math.sin(Math.toRadians(orientation)) * 30),
            coordinate.y - (int) (Math.cos(Math.toRadians(orientation)) * 30)
        );
    }
}
