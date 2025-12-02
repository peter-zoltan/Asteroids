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

    public Planet(int x, int y) {
        try {
            image = ImageIO.read(new File("src/main/resources/planet-earth.png"));
        } catch (IOException e) {
            System.out.println("Error loading planet");
        }
        setLocation(x, y);
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(image, coordinate.x - image.getWidth() / 2, coordinate.y - image.getHeight() / 2, null);
        graphics.drawOval(coordinate.x - image.getWidth() / 2, coordinate.y - image.getHeight() / 2, 256, 256);
        graphics.drawRect(coordinate.x - 2, coordinate.y - 2, 5, 5);
    }

}
