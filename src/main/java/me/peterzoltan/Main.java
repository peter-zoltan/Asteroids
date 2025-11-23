package me.peterzoltan;

import me.peterzoltan.frame.GameFrame;
import me.peterzoltan.game.MyCanvas;
import me.peterzoltan.game.object.Asteroid;
import me.peterzoltan.game.object.SpaceShip;

public class Main {

    public static void main(String[] args) {
        GameFrame frame = new GameFrame("Game");
        frame.setVisible(true);

        MyCanvas canvas = new MyCanvas();
        frame.add(canvas);
        canvas.setSize(frame.getWidth(), frame.getHeight());
        canvas.add(new SpaceShip());
        canvas.paint(canvas.getGraphics());
    }

}