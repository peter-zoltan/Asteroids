package me.peterzoltan.frame;

import me.peterzoltan.game.Difficulty;
import me.peterzoltan.util.configUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class MenuFrame extends JFrame {

    boolean settingsToggle = false;
    JButton startButton;
    JButton settingsButton;
    JMenu presets;
    JButton saveButton;
    JPanel settingsPanel;

    private JTextField nameField;
    private JTextField hitpointsField;
    private JTextField frequencyField;

    private JTextField smallWeightField;
    private JTextField mediumWeightField;
    private JTextField largeWeightField;

    private List<Difficulty> difficulties = new ArrayList<>();
    private Difficulty chosenDifficulty = new Difficulty("example", 6, 2, new int[]{1, 1, 1});

    public MenuFrame() {
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(startActionListener);
        add(startButton);

        settingsButton = new JButton("Settings");
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.addActionListener(settingsActionListener);
        add(settingsButton);

        presets = new JMenu("Presets");
        presets.setAlignmentX(Component.CENTER_ALIGNMENT);
        presets.setVisible(false);
        presets.add(new JMenuItem("Easy"));
        presets.add(new JMenuItem("Medium"));
        presets.add(new JMenuItem("Hard"));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(presets);
        add(menuBar);

        initializeSettingsPanel();

        saveButton = new JButton("Save to JSON");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(saveListener);
        add(saveButton);
        saveButton.setVisible(false);
    }

    public void initializeSettingsPanel() {

        settingsPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        settingsPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        settingsPanel.add(nameField);

        settingsPanel.add(new JLabel("Hitpoints:"));
        hitpointsField = new JTextField();
        settingsPanel.add(hitpointsField);

        settingsPanel.add(new JLabel("Frequency:"));
        frequencyField = new JTextField();
        settingsPanel.add(frequencyField);

        settingsPanel.add(new JLabel("Small Weight:"));
        smallWeightField = new JTextField();
        settingsPanel.add(smallWeightField);

        settingsPanel.add(new JLabel("Medium Weight:"));
        mediumWeightField = new JTextField();
        settingsPanel.add(mediumWeightField);

        settingsPanel.add(new JLabel("Large Weight:"));
        largeWeightField = new JTextField();
        settingsPanel.add(largeWeightField);

        add(settingsPanel);
        settingsPanel.setVisible(false);
    }

    ActionListener startActionListener = e -> {
        GameFrame frame = new GameFrame("Game", chosenDifficulty);
        frame.pack();
        frame.setVisible(true);
        SwingUtilities.invokeLater(frame::init);
    };

    ActionListener settingsActionListener = e -> {
        if (settingsToggle) {
            settingsToggle = false;
            startButton.setVisible(true);
            settingsButton.setText("Settings");
            presets.setVisible(false);
            settingsPanel.setVisible(false);
            saveButton.setVisible(false);
        } else {
            difficulties = configUtil.getConfig();
            settingsToggle = true;
            startButton.setVisible(false);
            settingsButton.setText("Menu");
            presets.setVisible(true);
            settingsPanel.setVisible(true);
            saveButton.setVisible(true);
        }
    };

    ActionListener saveListener = e -> {

        try {
            configUtil.putDifficulty(
                    new Difficulty(
                            nameField.getText(),
                            parseInt(hitpointsField.getText()),
                            parseInt(frequencyField.getText()),
                            new int[]{
                                    parseInt(smallWeightField.getText()),
                                    parseInt(mediumWeightField.getText()),
                                    parseInt(largeWeightField.getText())
                            }
                    ));
            configUtil.saveConfig();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Incorrect diffculty format:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    };

}


