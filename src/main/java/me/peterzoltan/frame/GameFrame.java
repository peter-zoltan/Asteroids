package me.peterzoltan.frame;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame(String title) {
        super(title);
        setSize(800, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
