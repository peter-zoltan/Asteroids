package me.peterzoltan.game.object;

import me.peterzoltan.game.CollisionObject;
import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.MovableGameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.Image.SCALE_DEFAULT;

public class SpaceShip extends MovableGameObject implements CollisionObject, Drawable, KeyListener {

    Image image;
    int orientation;

    public SpaceShip() {
        ImageIcon icon = new ImageIcon("src/main/resources/spaceship.png");
        image = icon.getImage().getScaledInstance(64, 64, SCALE_DEFAULT);
        coordinate = new Point(100,100);
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(image, (int)coordinate.getX(), (int)coordinate.getY(), null);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_D: ax += 10; break;
            case KeyEvent.VK_S, KeyEvent.VK_A: ax -= 10; break;
            default: break;
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

}

/*class InputListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_W: // how to access the spaceship's veolcity?
                break;
        }
    }
}*/
