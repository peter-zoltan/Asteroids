package me.peterzoltan.frame;

import me.peterzoltan.game.Difficulty;
import me.peterzoltan.game.MyCanvas;
import me.peterzoltan.game.object.Asteroid;
import me.peterzoltan.game.object.Planet;
import me.peterzoltan.game.object.Projectile;
import me.peterzoltan.game.object.Spaceship;
import me.peterzoltan.util.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameFrame extends JFrame implements KeyListener {

    private final MyCanvas canvas;
    private Spaceship spaceship;
    private Planet planet;
    private final List<Asteroid> asteroids = new ArrayList<>();
    private long lastAsteroid = 0;

    private final Set<Integer> pressedKeys = new HashSet<>();

    private final int hitpoints;
    private final int frequency;
    private final int[] weights;

    /**
     * Sets up a maximized, undecorated frame for the game, that contains the canvas where the game will be drawn.
     * @param difficulty the difficulty of the sesson, is used to intialize the relevant variables:
     *                   - Frequency of Asteroids spawning
     *                   - Hitpoints of the Planet
     *                   - Distribution of the weights of the Asteroids
     */
    public GameFrame(Difficulty difficulty) {
        setExtendedState(MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        frequency = difficulty.getFrequency();
        hitpoints = difficulty.getHitpoints();
        weights = difficulty.getWeights();

        canvas = new MyCanvas();
        add(canvas, BorderLayout.CENTER);
    }

    /**
     * Initializes the canvas and the game objects that start on it.
     * Creates and starts the timer that manages actions taken every tick. These actions are:
     * - Adding a new Asteroid to the Canvas as needed
     * - Adding the newest Projectile to the Canvas if does not already have it (at most one Projectile can be added per tick)
     * - Updating the poition of all moving objects
     * - Checking for collisions between objects
     * - Calling the Canvas to repaint itself according to the changes
     */
    public void init() {
        canvas.init();

        planet = new Planet(hitpoints, getContentPane().getWidth() / 2, getContentPane().getHeight() / 2);
        spaceship = new Spaceship(pressedKeys, getContentPane().getWidth(), getContentPane().getHeight());

        canvas.setSize(getContentPane().getWidth(), getContentPane().getHeight());
        canvas.setFocusable(true);
        canvas.addKeyListener(this);
        canvas.addDrawable(planet);
        canvas.addDrawable(spaceship);

        new Timer(16, e -> {
            addAsteroid();
            addProjectile();
            updatePositions();
            checkCollisions();
            canvas.repaint();
        }).start();
    }

    /**
     * Updates the position of all moving objects
     */
    private void updatePositions() {
        spaceship.updateProjectiles();
        spaceship.updatePosition();
        for (Asteroid asteroid : asteroids) {
            asteroid.updatePosition();
        }
    }

    /**
     * Adds the latest Projectile shotby the Spaceship to the Canvas if it has not yet been added.
     */
    private void addProjectile() {
        if (!spaceship.projectiles.isEmpty()) {
            Projectile newest = spaceship.projectiles.getLast();
            if (!canvas.getDrawables().contains(newest)) {
                canvas.addDrawable(newest);
            }
        }
    }

    /**
     * Adds a new Asteroid to the Canvas if enough time has elapsed since the last one (frequency).
     * The Asteroid has the following potential size distribution:
     * - weights[0] / (weights[0] + weights[1] + weights[2]) chance to be a Small one
     * - weights[1] / (weights[0] + weights[1] + weights[2]) chance to be a Medium one
     * - weights[2] / (weights[0] + weights[1] + weights[2]) chance to be a Large one
     * The Asteroid starts from either the left or right side of the screen, at a random height, and moves towards the middle.
     */
    private void addAsteroid() {
        if (System.currentTimeMillis() - lastAsteroid > 1000L * frequency) {

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

    /**
     * Iterates over the objects in the game checking for collisions. On collision the following happen:
     * - Asteroid & Projectile: The Asteroid loses 1 hitpoint, if this results in it having 0 or less, it is removed.
     *      The Projectile is removed.
     * - Asteroid & Planet: The Planet loses hitpoints according to the Asteroid's size, if this results in it having 0
     *      or less, the game is over. The Asteroid is removed.
     * - Asteroid & Spaceship: If not already disabled, the Spaceship loses the ability to shoot for a second.
     * - Other: No action is taken.
     */
    private void checkCollisions() {
        Set<Asteroid> asteroidsToBeRemoved = new HashSet<>();
        for (Asteroid asteroid : asteroids) {

            double distance = 0;
            int aX = asteroid.getLocation().x;
            int aY = asteroid.getLocation().y;
            int aR = asteroid.getRadius();
            Set<Projectile> projectilesToBeRemoved = new HashSet<>();

            for (Projectile projectile : spaceship.projectiles) {
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
            spaceship.projectiles.removeAll(projectilesToBeRemoved);
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

            int sX = spaceship.getLocation().x;
            int sY = spaceship.getLocation().y;
            int sR = spaceship.getRadius();
            distance = Math.sqrt((aX - sX) * (aX - sX) + (aY - sY) * (aY - sY));
            if (distance < aR + sR) {
                spaceship.registerHit();
            }
        }
        asteroids.removeAll(asteroidsToBeRemoved);
        for (Asteroid asteroidToBeRemoved : asteroidsToBeRemoved) {
            canvas.removeDrawable(asteroidToBeRemoved);
        }
    }

    /**
     * Ignored, but all abstract methods of KeyListener have to be implemented.
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * The key that has been pressed is added to the set that keeps record of the keys currently being pressed.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    /**
     * The key that has been released is removed from the set that keeps record of the keys currently being pressed.
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

}
