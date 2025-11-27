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
    SpaceShip spaceShip;    // initiated later
    Planet planet;
    List<Asteroid> asteroids = new ArrayList<Asteroid>();
    List<Projectile> projectiles = new ArrayList<Projectile>();
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
        spaceShip = new SpaceShip(spaceShipMovement);

        canvas = new MyCanvas(getContentPane().getWidth(), getContentPane().getHeight());
        add(canvas);
        canvas.setFocusable(true);
        canvas.addKeyListener(this);
        canvas.addDrawable(planet);
        canvas.addDrawable(spaceShip);

        new Timer(tick, e -> {
            updatePositions();
            canvas.repaint();
        }).start();

    }

    public void updatePositions() {
        spaceShip.updatePosition();
        for (Asteroid asteroid : asteroids) {
            asteroid.updatePosition();
        }
        for (Projectile projectile : projectiles) {
            projectile.updatePosition();
        }
    }

    Movable spaceShipMovement = new Movable() {
        @Override
        public void updatePosition() {
            int x = spaceShip.getLocation().x;
            int y = spaceShip.getLocation().y;
            int orientation = spaceShip.getOrientation();
            int xOffset = 0;
            int yOffset = 0;
            int orientationOffset = 0;
            for (Integer keyCode : pressedKeys) {
                switch (keyCode) {
                    case KeyEvent.VK_W -> {
                        xOffset += (int) (Math.sin(Math.toRadians(orientation)) * 12);
                        yOffset -= (int) (Math.cos(Math.toRadians(orientation)) * 12);
                    }
                    case KeyEvent.VK_S -> {
                        xOffset -= (int) (Math.sin(Math.toRadians(orientation)) * 12);
                        yOffset += (int) (Math.cos(Math.toRadians(orientation)) * 12);
                    }
                    case KeyEvent.VK_A -> orientationOffset -= 10;
                    case KeyEvent.VK_D -> orientationOffset += 10;
                }
            }
            spaceShip.setLocation(x + xOffset, y + yOffset);
            spaceShip.setOrientation(orientation + orientationOffset);
        }
    };

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
