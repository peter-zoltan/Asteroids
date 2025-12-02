package me.peterzoltan;

import me.peterzoltan.frame.GameFrame;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) {
        GameFrame frame = new GameFrame("Game");
        frame.setVisible(true);
        frame.init();
    }

}