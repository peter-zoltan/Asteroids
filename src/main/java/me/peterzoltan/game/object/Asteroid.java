package me.peterzoltan.game.object;

import me.peterzoltan.game.CollisionObject;
import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.GameObject;

import java.awt.*;

public class Asteroid extends GameObject implements CollisionObject, Drawable {

    public void draw(Graphics graphics) {
        graphics.drawOval(100, 100, 50, 50);
    }

}
