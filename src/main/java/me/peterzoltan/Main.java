package me.peterzoltan;

import me.peterzoltan.frame.GameFrame;
import java.awt.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        GameFrame frame = new GameFrame("Game");
        frame.setVisible(true);
        frame.init();
    }

}