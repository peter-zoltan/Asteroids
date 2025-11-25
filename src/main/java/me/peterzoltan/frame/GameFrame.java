package me.peterzoltan.frame;

import me.peterzoltan.game.MyCanvas;
import me.peterzoltan.game.object.SpaceShip;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameFrame extends JFrame implements KeyListener {

    MyCanvas canvas;
    SpaceShip spaceShip;

    public GameFrame(String title) {
        super(title);
        setSize(800, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void init() {
        spaceShip = new SpaceShip();
        canvas = new MyCanvas(getWidth(), getHeight());
        canvas.setFocusable(true);
        canvas.addKeyListener(this);
        add(canvas);
        canvas.addDrawable(spaceShip);
        new Timer(8, e -> {
            canvas.repaint();
        }).start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int x = spaceShip.getLocation().x;
        int y = spaceShip.getLocation().y;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> spaceShip.setLocation(x, y - 20);
            case KeyEvent.VK_S -> spaceShip.setLocation(x, y + 20);
            case KeyEvent.VK_A -> spaceShip.setLocation(x - 20, y);
            case KeyEvent.VK_D -> spaceShip.setLocation(x + 20, y);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
