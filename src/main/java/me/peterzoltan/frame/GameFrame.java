package me.peterzoltan.frame;

import me.peterzoltan.game.MyCanvas;
import me.peterzoltan.game.object.SpaceShip;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class GameFrame extends JFrame implements KeyListener {

    MyCanvas canvas;
    SpaceShip spaceShip;
    private final Set<Integer> pressedKeys = new HashSet<>();
    public static int tick = 16;

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
        new Timer(tick, e -> {
            int x = spaceShip.getLocation().x;
            int y = spaceShip.getLocation().y;
            int xOffset = 0;
            int yOffset = 0;
            for (Integer keyCode : pressedKeys) {
                switch (keyCode) {
                    case KeyEvent.VK_W -> yOffset -= 20;
                    case KeyEvent.VK_S -> yOffset += 20;
                    case KeyEvent.VK_A -> xOffset -= 20;
                    case KeyEvent.VK_D -> xOffset += 20;
                }
            }
            spaceShip.setLocation(x + xOffset, y + yOffset);
            canvas.repaint();
        }).start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }
}
