package me.peterzoltan.game.object;

import me.peterzoltan.game.CollisionObject;
import me.peterzoltan.game.Drawable;
import me.peterzoltan.game.MovableGameObject;

import java.awt.*;

public class Asteroid extends MovableGameObject implements CollisionObject, Drawable {

    public void draw(Graphics graphics) {
        graphics.drawOval(100, 100, 50, 50);
    }

}
