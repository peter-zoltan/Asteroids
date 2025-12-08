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

    public void draw(Graphics graphics) {
        graphics.drawImage(image, coordinate.x - radius, coordinate.y - radius, null);
    }

    @Override
    public void updatePosition() {
        setLocation(
            coordinate.x + (int) (Math.sin(Math.toRadians(orientation)) * 5),
            coordinate.y - (int) (Math.cos(Math.toRadians(orientation)) * 5)
        );
    }

    public void registerHit() {
        hitpoints--;
    }

    public boolean isDestroyed() {
        return hitpoints == 0;
    }

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
