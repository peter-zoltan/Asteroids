package me.peterzoltan.frame;

import me.peterzoltan.game.Movable;
import me.peterzoltan.game.MyCanvas;
import me.peterzoltan.game.object.Asteroid;
import me.peterzoltan.game.object.Planet;
import me.peterzoltan.game.object.Projectile;
import me.peterzoltan.game.object.SpaceShip;
import me.peterzoltan.util.Size;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;

public class GameFrame extends JFrame implements KeyListener {

    MyCanvas canvas;
    SpaceShip spaceShip;
    Planet planet;
    List<Asteroid> asteroids = new ArrayList<>();
    long lastAsteroid = 0;
    private final Set<Integer> pressedKeys = new HashSet<>();
    public static int tick = 16;
    int[] weights;

    public boolean gameOver = false;

    public GameFrame(String title, int[] weights) {
        super(title);
        if (weights.length != 3) {
            throw new IllegalArgumentException("Weights must have 3 elements");
        }
        this.weights = weights;
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        canvas = new MyCanvas();
        add(canvas, BorderLayout.CENTER);
    }

    public void init() {

        canvas.init();

        planet = new Planet(getContentPane().getWidth() / 2, getContentPane().getHeight() / 2, 6);
        spaceShip = new SpaceShip(pressedKeys);

        canvas.setSize(getContentPane().getWidth(), getContentPane().getHeight());
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
            checkCollisions();
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

    public void cleanUp() {
        for (Projectile projectile : spaceShip.projectiles) {
            int x = projectile.getLocation().x;
            int y = projectile.getLocation().y;
            if (x < 0 || x > getContentPane().getWidth() || y < 0 || y > getContentPane().getHeight()) {
                // remove from spaceship and from canvas
            }
        }
    }

    public void checkCollisions() {
        Set<Asteroid> asteroidsToBeRemoved = new HashSet<>();
        for (Asteroid asteroid : asteroids) {

            double distance = 0;
            int aX = asteroid.getLocation().x;
            int aY = asteroid.getLocation().y;
            int aR = asteroid.getRadius();
            Set<Projectile> projectilesToBeRemoved = new HashSet<>();

            for (Projectile projectile : spaceShip.projectiles) {
                int prX = projectile.getLocation().x;
                int prY = projectile.getLocation().y;
                int prR = projectile.getRadius();
                distance = Math.sqrt((aX - prX) * (aX - prX) + (aY - prY) * (aY - prY));
                if (distance < aR + prR) {
                    asteroid.registerHit();
                    if (asteroid.isDestroyed()) {
                        asteroidsToBeRemoved.add(asteroid);
                    }
                    projectilesToBeRemoved.add(projectile);
                }
            }
            spaceShip.projectiles.removeAll(projectilesToBeRemoved);
            for (Projectile projectileToBeRemoved : projectilesToBeRemoved) {
                canvas.removeDrawable(projectileToBeRemoved);
            }

            int plX = planet.getLocation().x;
            int plY = planet.getLocation().y;
            int plR = planet.getRadius();
            distance = Math.sqrt((aX - plX) * (aX - plX) + (aY - plY) * (aY - plY));
            if (distance < aR + plR) {
                planet.registerHit(asteroid.getDamage());
                asteroidsToBeRemoved.add(asteroid);
                if (planet.isDestroyed()) {
                    dispose();
                }
            }

            int sX = spaceShip.getLocation().x;
            int sY = spaceShip.getLocation().y;
            int sR = spaceShip.getRadius();
            distance = Math.sqrt((aX - sX) * (aX - sX) + (aY - sY) * (aY - sY));
            if (distance < aR + sR) {
                spaceShip.registerHit();
            }
        }
        asteroids.removeAll(asteroidsToBeRemoved);
        for (Asteroid asteroidToBeRemoved : asteroidsToBeRemoved) {
            canvas.removeDrawable(asteroidToBeRemoved);
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
