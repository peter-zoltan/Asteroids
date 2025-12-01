package me.peterzoltan.frame;

import me.peterzoltan.game.Movable;
import me.peterzoltan.game.MyCanvas;
import me.peterzoltan.game.object.Asteroid;
import me.peterzoltan.game.object.Planet;
import me.peterzoltan.game.object.Projectile;
import me.peterzoltan.game.object.SpaceShip;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameFrame extends JFrame implements KeyListener {

    MyCanvas canvas;
    SpaceShip spaceShip;
    Planet planet;
    List<Asteroid> asteroids = new ArrayList<>();
    private final Set<Integer> pressedKeys = new HashSet<>();
    public static int tick = 16;

    public GameFrame(String title) {
        super(title);
        setSize(800, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void init() {

        planet = new Planet(getContentPane().getWidth() * 3 / 4, getContentPane().getHeight() / 2);
        spaceShip = new SpaceShip(pressedKeys);

        canvas = new MyCanvas(getContentPane().getWidth(), getContentPane().getHeight());
        add(canvas);
        canvas.setFocusable(true);
        canvas.addKeyListener(this);
        canvas.addDrawable(planet);
        canvas.addDrawable(spaceShip);

        new Timer(tick, e -> {
            for(Projectile projectile : spaceShip.projectiles) {
                canvas.addDrawable(projectile);
            }
            updatePositions();
            canvas.repaint();
        }).start();

    }

    public void updatePositions() {
        spaceShip.updateProjectiles();
        spaceShip.updatePosition();
        for (Asteroid asteroid : asteroids) {
            asteroid.updatePosition();
        }
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
