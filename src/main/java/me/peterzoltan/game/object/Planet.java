package me.peterzoltan.game.object;

import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Planet extends GameObject implements Drawable {

    BufferedImage image;
    int health;

    public Planet(int health, int x, int y) {
        try {
            image = ImageIO.read(new File("src/main/resources/planet-earth.png"));
        } catch (IOException e) {
            System.out.println("Error loading planet");
        }
        setLocation(x, y);
        radius = image.getWidth() / 2;
        this.health = health;
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(image, coordinate.x - radius, coordinate.y - radius, null);
    }

    public void registerHit(int damage) {
        health -= damage;
    }

    public boolean isDestroyed() {
        return health < 0;
    }

}
