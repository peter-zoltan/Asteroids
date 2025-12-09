package me.peterzoltan.game.object;

import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;
import me.peterzoltan.game.Movable;

import static me.peterzoltan.util.ImageUtil.rotate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Spaceship extends GameObject implements Drawable, Movable {

    BufferedImage image;
    Set<Integer> pressedKeys;
    public List<Projectile> projectiles = new ArrayList<>();
    long lastShot = 0;
    long lastHit = 0;
    long resetTime = 1000;
    int canvasWidth;
    int canvasHeight;

    /**
     * Loads the image that is used to draw the Spaceship, and sets it's variables.
     * @param pressedKeys the set that keeps record of the keys currently being pressed.
     * @param canvasWidth the width of the canvas the game is drawn on.
     * @param canvasHeight the height of the canvas the game is drawn on.
     */
    public Spaceship(Set<Integer> pressedKeys, int canvasWidth, int canvasHeight) {
        try {
            image = ImageIO.read(new File("src/main/resources/spaceship.png"));
        } catch (IOException e) {
            System.out.println("Error loading spaceship");
        }
        setLocation(100, 100);
        setOrientation(0);
        radius = image.getWidth() / 2;
        this.pressedKeys = pressedKeys;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    /**
     * Draws image rotated by orientation to the Graphics object passed as a parameter.
     * Radius is subtracted from the coordinates so that coordinate is at the center of the image.
     * @param graphics the Graphics object to which it will draw onto.
     */
    public void draw(Graphics graphics) {
        BufferedImage rotated = rotate(image, orientation);
        graphics.drawImage(rotated, coordinate.x - radius, coordinate.y - radius, null);
    }

    /**
     * Sets orientation to the value passed as a parameter. Overflows in both directions at 0 and 360 respectively.
     * @param o orientation will be set to this.
     */
    public void setOrientation(int o) {
        orientation = o;
        if (orientation < 0) {
            orientation = 360 + orientation;
        } else if (orientation > 360) {
            orientation = orientation - 360;
        }
    }

    /**
     * Sets the new location of the Spaceship, making sure that it stays within the bounds of the Canvas.
     * If it would go out of bounds along an axis, it keeps it's previous position along that one.
     * @param xOffset the amount x is to be changed by.
     * @param yOffset the amount y is to be changed by.
     */
    public void setLocationWithBounds(int xOffset, int yOffset) {
        int x = coordinate.x;
        int y = coordinate.y;
        int newX;
        int newY;
        if (x + xOffset > canvasWidth || x + xOffset < 0) {
            newX = x;
        } else {
            newX = x + xOffset;
        }
        if (y + yOffset > canvasHeight || y + yOffset < 0) {
            newY = y;
        } else {
            newY = y + yOffset;
        }
        setLocation(newX, newY);
    }

    /**
     * Updates the Spaceship's and it's Projectiles' position.
     * The arrow keys move it along it's own x and y axis (decided by orientation).
     * The Q and E keys respectively decrement and increment it's orientation.
     * Iterates over the Projectiles and calls their functions to handle the change in position.
     */
    @Override
    public void updatePosition() {
        int xOffset = 0;
        int yOffset = 0;
        int orientationOffset = 0;
        for (Integer keyCode : pressedKeys) {
            switch (keyCode) {
                case KeyEvent.VK_UP -> {
                    xOffset += (int) (Math.sin(Math.toRadians(orientation)) * 12);
                    yOffset -= (int) (Math.cos(Math.toRadians(orientation)) * 12);
                }
                case KeyEvent.VK_DOWN -> {
                    xOffset -= (int) (Math.sin(Math.toRadians(orientation)) * 12);
                    yOffset += (int) (Math.cos(Math.toRadians(orientation)) * 12);
                }
                case KeyEvent.VK_LEFT -> {
                    xOffset -= (int) (Math.sin(Math.toRadians(orientation + 90)) * 12);
                    yOffset += (int) (Math.cos(Math.toRadians(orientation + 90)) * 12);
                }
                case KeyEvent.VK_RIGHT -> {
                    xOffset += (int) (Math.sin(Math.toRadians(orientation + 90)) * 12);
                    yOffset -= (int) (Math.cos(Math.toRadians(orientation + 90)) * 12);
                }
                case KeyEvent.VK_Q -> orientationOffset -= 10;
                case KeyEvent.VK_E -> orientationOffset += 10;
            }
        }
        setLocationWithBounds(xOffset, yOffset);
        setOrientation(orientation + orientationOffset);

        for (Projectile projectile : projectiles) {
            projectile.updatePosition();
        }
    }

    /**
     * If enough time has passed since the last Projectile was fired and Spacebar is curently pressed, and the Spaceship
     * is capable of shooting (enough time has passed since last hit registered), it shoots a new Projectile, and
     * records the time of the shot.
     */
    public void updateProjectiles() {
        if (System.currentTimeMillis() - lastHit <= resetTime) {
            return;
        }
        if (pressedKeys.contains(KeyEvent.VK_SPACE) && System.currentTimeMillis() - lastShot > 128) {
            projectiles.add(
                new Projectile(
                    coordinate.x,
                    coordinate.y,
                    orientation
               )
            );
            lastShot = System.currentTimeMillis();
        }
    }

    /**
     * If the Spaceship can currently shoot Projectiles that ability is disabled for a given time.
     */
    public void registerHit() {
        if (System.currentTimeMillis() - lastHit > resetTime) {
            lastHit = System.currentTimeMillis();
        }
    }

}
