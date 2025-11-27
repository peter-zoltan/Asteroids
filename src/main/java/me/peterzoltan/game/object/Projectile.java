package me.peterzoltan.game.object;

import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;
import me.peterzoltan.game.Movable;

import java.awt.*;

public class Projectile extends GameObject implements Drawable, Movable {

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        graphics.drawOval(100, 100, 20, 20);
    }

    @Override
    public void updatePosition() {

    }
}
