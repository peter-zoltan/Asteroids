package me.peterzoltan.frame;

import me.peterzoltan.game.Movable;
import me.peterzoltan.game.MyCanvas;
import me.peterzoltan.game.object.Asteroid;
import me.peterzoltan.game.object.Planet;
import me.peterzoltan.game.object.Projectile;
import me.peterzoltan.game.object.SpaceShip;
import me.peterzoltan.util.Size;

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
    long lastAsteroid = 0;
    private final Set<Integer> pressedKeys = new HashSet<>();
    public static int tick = 16;
    int[] weights;

    public GameFrame(String title, int[] weights) {
        super(title);
        if (weights.length != 3) {
            throw new IllegalArgumentException("Weights must have 3 elements");
        }
        this.weights = weights;
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void init() {

        planet = new Planet(getContentPane().getWidth() / 2, getContentPane().getHeight() / 2);
        spaceShip = new SpaceShip(pressedKeys);
        System.out.println(getContentPane().getWidth());
        System.out.println(getContentPane().getHeight());

        canvas = new MyCanvas(getContentPane().getWidth(), getContentPane().getHeight());
        add(canvas);
        canvas.setFocusable(true);
        canvas.addKeyListener(this);
        canvas.addDrawable(planet);
        canvas.addDrawable(spaceShip);

        new Timer(tick, e -> {
            addAsteroid();
            for(Projectile projectile : spaceShip.projectiles) {
                if (!canvas.getDrawables().contains(projectile)) {
                    canvas.addDrawable(projectile);
                }
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

    public void addAsteroid() {
        if (System.currentTimeMillis() - lastAsteroid > 1000) {

            Size size;
            int weightSum = weights[0] + weights[1] + weights[2];
            int randomWeight = Math.toIntExact(Math.round(Math.random() * weightSum));
            if (randomWeight < weights[0]) {
                size = Size.SMALL;
            } else if (randomWeight < weights[0] + weights[1]) {
                size = Size.MEDIUM;
            } else {
                size = Size.LARGE;
            }

            int width = getContentPane().getWidth();
            int height = getContentPane().getHeight();
            int x = Math.random() > 0.5 ? 0 : getContentPane().getWidth();
            int y = (int) (Math.random() * getContentPane().getHeight());

            int orientation;
            if (x == 0) {
                if (y < height / 2) {
                    orientation = 180 - (int) Math.toDegrees(Math.atan((double) (width / 2) / ((double) (height / 2) - y)));
                } else {
                    orientation = - (int) Math.toDegrees(Math.atan((double) (width / 2) / ((double) (height / 2) - y)));
                }
            } else {
                if (y < getContentPane().getHeight() / 2) {
                    orientation = 180 + (int) Math.toDegrees(Math.atan((double) (width / 2) / ((double) (height / 2) - y)));
                } else {
                    orientation = 360 + (int) Math.toDegrees(Math.atan((double) (width / 2) / ((double) (height / 2) - y)));
                }
            }

            Asteroid asteroid = new Asteroid(x, y, orientation, size);
            asteroids.add(asteroid);
            canvas.addDrawable(asteroid);
            lastAsteroid = System.currentTimeMillis();
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
