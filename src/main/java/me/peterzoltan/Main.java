package me.peterzoltan;

import me.peterzoltan.frame.GameFrame;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) {
        int[] weights = {1, 2, 3};
        GameFrame frame = new GameFrame("Game", weights);
        frame.setVisible(true);
        frame.init();
    }

}