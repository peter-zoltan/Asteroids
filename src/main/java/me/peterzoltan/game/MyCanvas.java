package me.peterzoltan.game;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class MyCanvas extends Canvas {

    List<Drawable> drawables = new ArrayList<>();

    public void add(Drawable drawable) {
        drawables.add(drawable);
    }

    @Override
    public void paint(Graphics graphics) {
        for (Drawable drawable : drawables) {
            drawable.draw(graphics);
        }
    }

}
