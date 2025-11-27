package me.peterzoltan.game.object;

import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;
import me.peterzoltan.game.Movable;

import static me.peterzoltan.util.ImageUtil.rotate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class SpaceShip extends GameObject implements Drawable, Movable {

    BufferedImage image;
    int orientation;
    Movable movement;

    public SpaceShip(Movable movement) {
        try {
            image = ImageIO.read(new File("src/main/resources/spaceship.png"));
        } catch (IOException e) {
            System.out.println("Error loading spaceship");
        }
        setLocation(100, 100);
        this.movement = movement;
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
    public void updatePosition() {
        movement.updatePosition();
    }

}
