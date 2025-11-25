package me.peterzoltan.game.object;

import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.MovableGameObject;

import javax.swing.*;
import java.awt.*;

import static java.awt.Image.SCALE_SMOOTH;

public class SpaceShip extends MovableGameObject implements Drawable {

    Image image;
    int orientation;

    public SpaceShip() {
        ImageIcon icon = new ImageIcon("src/main/resources/spaceship.png");
        image = icon.getImage().getScaledInstance(64, 64, SCALE_SMOOTH);
        setLocation(100, 100);
    }

    public Image getImage() {
        return image;
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(image, coordinate.x, coordinate.y, null);
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

}
