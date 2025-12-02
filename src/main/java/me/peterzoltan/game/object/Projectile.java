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

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(image, coordinate.x - image.getWidth() / 2, coordinate.y - image.getHeight() / 2, null);
    }

    @Override
    public void updatePosition() {
        setLocation(
            coordinate.x + (int) (Math.sin(Math.toRadians(orientation)) * 30),
            coordinate.y - (int) (Math.cos(Math.toRadians(orientation)) * 30)
        );
    }
}
