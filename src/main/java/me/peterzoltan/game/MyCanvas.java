package me.peterzoltan.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyCanvas extends JPanel {

    private final Graphics2D graphics;
    private final BufferedImage image;
    List<Drawable> drawables = new ArrayList<>();

    public MyCanvas(int width, int height) {
        setSize(new Dimension(width, height));
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setBackground(Color.RED);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintToBuffer();
        g.drawImage(image, 0, 0, null);
    }

    private void paintToBuffer() {
        graphics.clearRect(0, 0, getWidth(), getHeight());
        for (Drawable drawable : drawables) {
            drawable.draw(graphics);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

}
