package me.peterzoltan.game.object;

import me.peterzoltan.game.CollisionObject;
import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;

import javax.swing.*;
import java.awt.*;

public class SpaceShip extends GameObject implements CollisionObject, Drawable {

    public void draw(Graphics graphics) {
        ImageIcon icon = new ImageIcon("src/main/java/resources/spaceship.png");
        Image image = icon.getImage();
        graphics.drawImage(image, 500, 500, null);
    }

}
