package me.peterzoltan.frame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

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

    private JSONArray jsonArray;

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

        // Button
        saveButton = new JButton("Save to JSON");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(saveListener);
        add(saveButton);
        saveButton.setVisible(false);
    }

    public void initializeSettingsPanel() {

        settingsPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Main fields
        settingsPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        settingsPanel.add(nameField);

        settingsPanel.add(new JLabel("Hitpoints:"));
        hitpointsField = new JTextField();
        settingsPanel.add(hitpointsField);

        settingsPanel.add(new JLabel("Frequency:"));
        frequencyField = new JTextField();
        settingsPanel.add(frequencyField);

        // Weight fields
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
        int[] weights = {1, 2, 3};
        GameFrame frame = new GameFrame("Game", weights);
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
            try {
                File configFile = new File("config.json");
                String jsonString = Files.readString(configFile.toPath());
                jsonArray = new JSONArray(jsonString);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error loading JSON file:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            settingsToggle = true;
            startButton.setVisible(false);
            settingsButton.setText("Menu");
            presets.setVisible(true);
            settingsPanel.setVisible(true);
            saveButton.setVisible(true);
        }
    };

    ActionListener saveListener = e -> {
        if (jsonArray == null) {
            JOptionPane.showMessageDialog(null,
                    "No JSON file loaded. Click 'Load JSON File' first.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create new object
        JSONObject obj = new JSONObject();
        obj.put("name", nameField.getText());
        obj.put("hitpoints", hitpointsField.getText());
        obj.put("frequency", frequencyField.getText());

        JSONObject weights = new JSONObject();
        weights.put("small", smallWeightField.getText());
        weights.put("medium", mediumWeightField.getText());
        weights.put("large", largeWeightField.getText());
        obj.put("weights", weights);

        // Append to array
        jsonArray.put(obj);

        // Save back to file
        try {
            Files.writeString((new File("config.json")).toPath(), jsonArray.toString(4));
            JOptionPane.showMessageDialog(null, "Saved to file!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error writing JSON file:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    };

}


