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

    public void draw(Graphics graphics) {
        BufferedImage rotated = rotate(image, orientation);
        graphics.drawImage(rotated, coordinate.x - radius, coordinate.y - radius, null);
    }

    public void setOrientation(int o) {
        orientation = o;
        if (orientation < 0) {
            orientation = 360 + orientation;
        } else if (orientation > 360) {
            orientation = orientation - 360;
        }
    }

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

    public void registerHit() {
        if (System.currentTimeMillis() - lastHit > resetTime) {
            lastHit = System.currentTimeMillis();
        }
    }

}
