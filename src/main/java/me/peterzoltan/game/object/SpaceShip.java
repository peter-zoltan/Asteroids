package me.peterzoltan.game.object;

import me.peterzoltan.game.CollisionObject;
import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.Image.SCALE_DEFAULT;

public class SpaceShip extends GameObject implements CollisionObject, Drawable {

    Image image;

    public SpaceShip() {
        ImageIcon icon = new ImageIcon("src/main/resources/spaceship.png");
        image = icon.getImage().getScaledInstance(64, 64, SCALE_DEFAULT);
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(image, 100, 100, null);
    }

}

class InputListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_W:
        }
    }
}
