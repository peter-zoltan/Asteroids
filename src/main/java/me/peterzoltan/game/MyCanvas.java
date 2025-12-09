package me.peterzoltan.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class MyCanvas extends JPanel {

    private Graphics2D graphics;
    private BufferedImage image;
    List<Drawable> drawables = new ArrayList<>();

    /**
     * Initialized the Graphics of the Canvas.
     * Not done in the constructor so that the Canvas can be created with the Frame it is added to,
     * but still use parameters only available after the Frame is visible.
     */
    public void init() {
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setBackground(new Color(28, 23, 87));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Adds an object implementing Drawable to the list of objects to be drawn each tick.
     * @param drawable the object implementing Drawable.
     */
    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }

    /**
     * Removes an object implementing Drawable from the list of objects to be drawn each tick, no longer to be drawn.
     * @param drawable the object implementing Drawable.
     */
    public void removeDrawable(Drawable drawable) {
        drawables.remove(drawable);
    }

    /**
     * Returns the list of objects to be drawn each tick.
     * @return the list of objects to be drawn each tick.
     */
    public List<Drawable> getDrawables() {
        return drawables;
    }

    /**
     * Draws the BufferedImage the Graphics of which the object manipulates to the Graphics object passed as a parameter.
     * @param g the Graphics object it will draw onto.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintToBuffer();
        g.drawImage(image, 0, 0, null);
    }

    /**
     * Draws to the Graphics object of the BufferedImage that holds the state of the Canvas.
     * Draws each Drawable stored.
     */
    private void paintToBuffer() {
        try {
            graphics.clearRect(0, 0, getWidth(), getHeight());
        } catch (NullPointerException e) {
            System.out.println("First draw");
        }
        for (Drawable drawable : drawables) {
            drawable.draw(graphics);
        }
    }

    /**
     * Requests focus so inputs can be handled properly during gameplay.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    /**
     * Returns the preferred size of the Canvas.
     * @return the preferred size (fullscreen).
     */
    @Override
    public Dimension getPreferredSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

}
