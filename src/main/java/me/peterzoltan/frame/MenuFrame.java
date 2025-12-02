package me.peterzoltan.frame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Thread.sleep;

public class MenuFrame extends JFrame {

    public MenuFrame() {
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new startActionListener());
        add(startButton);
    }

}

class startActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int[] weights = {1, 2, 3};
        GameFrame frame = new GameFrame("Game", weights);
        frame.pack();
        frame.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            frame.init();
        });
    }
}
