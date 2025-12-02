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

public class SpaceShip extends GameObject implements Drawable, Movable {

    BufferedImage image;
    Set<Integer> pressedKeys;
    public List<Projectile> projectiles = new ArrayList<>();
    long lastShot = 0;

    public SpaceShip(Set<Integer> pressedKeys) {
        try {
            image = ImageIO.read(new File("src/main/resources/spaceship.png"));
        } catch (IOException e) {
            System.out.println("Error loading spaceship");
        }
        setLocation(100, 100);
        this.pressedKeys = pressedKeys;
    }

    public void draw(Graphics graphics) {
        BufferedImage rotated = rotate(image, orientation);
        graphics.drawImage(rotated, coordinate.x - image.getWidth() / 2, coordinate.y - image.getHeight() / 2, null);
        graphics.drawOval(coordinate.x - image.getWidth() / 2, coordinate.y - image.getHeight() / 2, 64, 64);
        graphics.drawRect(coordinate.x - 2, coordinate.y - 2, 5, 5);
    }

    public void setOrientation(int o) {
        orientation = o;
        if (orientation < 0) {
            orientation = 360 + orientation;
        } else if (orientation > 360) {
            orientation = orientation - 360;
        }
    }

    @Override
    public void updatePosition() {
        int xOffset = 0;
        int yOffset = 0;
        int orientationOffset = 0;
        for (Integer keyCode : pressedKeys) {
            switch (keyCode) {
                case KeyEvent.VK_W -> {
                    xOffset += (int) (Math.sin(Math.toRadians(orientation)) * 12);
                    yOffset -= (int) (Math.cos(Math.toRadians(orientation)) * 12);
                }
                case KeyEvent.VK_S -> {
                    xOffset -= (int) (Math.sin(Math.toRadians(orientation)) * 12);
                    yOffset += (int) (Math.cos(Math.toRadians(orientation)) * 12);
                }
                case KeyEvent.VK_A -> orientationOffset -= 10;
                case KeyEvent.VK_D -> orientationOffset += 10;
            }
        }
        setLocation(coordinate.x + xOffset, coordinate.y + yOffset);
        setOrientation(orientation + orientationOffset);

        for (Projectile projectile : projectiles) {
            projectile.updatePosition();
        }
    }

    public void updateProjectiles() {
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

}
