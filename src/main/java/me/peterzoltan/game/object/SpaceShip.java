package me.peterzoltan.game.object;

import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.MovableGameObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.Image.SCALE_SMOOTH;

public class SpaceShip extends MovableGameObject implements Drawable {

    BufferedImage image;
    int orientation;

    public SpaceShip() {
        /*image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        ImageIcon icon = new ImageIcon("src/main/resources/spaceship.png");
        image.createGraphics().drawImage(icon.getImage().getScaledInstance(64, 64, SCALE_SMOOTH), 0, 0, null);*/
        try {
            image = ImageIO.read(new File("src/main/resources/spaceship.png"));
        } catch (IOException e) {
        }
        setLocation(100, 100);
    }

    public Image getImage() {
        return image;
    }

    public void draw(Graphics graphics) {
        BufferedImage rotated = rotate(image, orientation);
        graphics.drawImage(rotated, coordinate.x, coordinate.y, null);
    }

    public int getOrientation() {
        return orientation;
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
    public void updatePosition() {}

    public static BufferedImage rotate(BufferedImage img, double degrees)
    {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        Graphics2D g2 = newImage.createGraphics();

        g2.rotate(Math.toRadians(degrees), (double) width / 2, (double) height / 2);
        g2.drawImage(img, null, 0, 0);

        return newImage;
    }

}
