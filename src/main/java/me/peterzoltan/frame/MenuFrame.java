package me.peterzoltan.frame;

import me.peterzoltan.game.Difficulty;
import me.peterzoltan.util.configUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;

public class MenuFrame extends JFrame {

    boolean settingsToggle = false;
    JButton startButton;
    JButton settingsButton;
    JMenu presets;
    JButton saveButton;
    JButton removeButton;
    JPanel buttonPanel;
    JPanel settingsPanel;

    private JTextField nameField;
    private JTextField hitpointsField;
    private JTextField frequencyField;

    private JTextField smallWeightField;
    private JTextField mediumWeightField;
    private JTextField largeWeightField;

    private List<Difficulty> difficulties = new ArrayList<>();
    private Difficulty chosenDifficulty;
    JLabel chosenLabel;

    public MenuFrame(String title) {
        super(title);
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

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(presets);
        add(menuBar);

        initializeDefaultDifficulty();

        chosenLabel = new JLabel("Chosen difficulty: " + chosenDifficulty.getName());
        chosenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chosenLabel.setVisible(false);
        add(chosenLabel);

        initializeSettingsPanel();

        buttonPanel = new JPanel();

        removeButton = new JButton("Remove");
        removeButton.addActionListener(removeListener);
        buttonPanel.add(removeButton);
        removeButton.setVisible(false);

        saveButton = new JButton("Save to JSON");
        saveButton.addActionListener(saveListener);
        buttonPanel.add(saveButton);
        saveButton.setVisible(false);

        add(buttonPanel);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void initializeSettingsPanel() {

        settingsPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        settingsPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        settingsPanel.add(nameField);

        settingsPanel.add(new JLabel("Hitpoints:"));
        hitpointsField = new JTextField();
        hitpointsField.addKeyListener(numberAdapter);
        settingsPanel.add(hitpointsField);

        settingsPanel.add(new JLabel("Frequency:"));
        frequencyField = new JTextField();
        frequencyField.addKeyListener(numberAdapter);
        settingsPanel.add(frequencyField);

        settingsPanel.add(new JLabel("Small Weight:"));
        smallWeightField = new JTextField();
        smallWeightField.addKeyListener(numberAdapter);
        settingsPanel.add(smallWeightField);

        settingsPanel.add(new JLabel("Medium Weight:"));
        mediumWeightField = new JTextField();
        mediumWeightField.addKeyListener(numberAdapter);
        settingsPanel.add(mediumWeightField);

        settingsPanel.add(new JLabel("Large Weight:"));
        largeWeightField = new JTextField();
        largeWeightField.addKeyListener(numberAdapter);
        settingsPanel.add(largeWeightField);

        add(settingsPanel);
        settingsPanel.setVisible(false);
    }

    private void initializeDefaultDifficulty() {
        Difficulty defaultDiff = new Difficulty(
                "default",
                6,
                2,
                new int[] {1, 1, 1}
        );
        difficulties.add(defaultDiff);
        configUtil.putDifficulty(defaultDiff);
        chosenDifficulty = defaultDiff;
    }

    ActionListener startActionListener = e -> {
        GameFrame frame = new GameFrame("Asteroids", chosenDifficulty);
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
            chosenLabel.setVisible(false);
            settingsPanel.setVisible(false);
            buttonPanel.setVisible(false);
            saveButton.setVisible(false);
            removeButton.setVisible(false);
        } else {
            difficulties = configUtil.getConfig();
            setMenuItems();
            settingsToggle = true;
            startButton.setVisible(false);
            settingsButton.setText("Menu");
            presets.setVisible(true);
            chosenLabel.setVisible(true);
            settingsPanel.setVisible(true);
            buttonPanel.setVisible(true);
            saveButton.setVisible(true);
            removeButton.setVisible(true);
        }
    };

    ActionListener saveListener = e -> {
        try {
            Difficulty difficulty = new Difficulty(
                    nameField.getText(),
                    parseInt(hitpointsField.getText()),
                    parseInt(frequencyField.getText()),
                    new int[]{
                            parseInt(smallWeightField.getText()),
                            parseInt(mediumWeightField.getText()),
                            parseInt(largeWeightField.getText())
                    }
            );
            configUtil.putDifficulty(difficulty);
            difficulties.add(difficulty);
            presets.add(new JMenuItem(difficulty.getName()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Incorrect diffculty format:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    };

    ActionListener removeListener = e -> {
        configUtil.removeDifficulty(chosenDifficulty);
        difficulties.remove(chosenDifficulty);
        setMenuItems();
    };

    ActionListener menuItemListener = e -> {
        JMenuItem selected = (JMenuItem) e.getSource();
        String name = selected.getText();
        for (Difficulty difficulty : difficulties) {
            if (name.equals(difficulty.getName())) {
                chosenDifficulty = difficulty;
            }
        }
        chosenLabel.setText("Chosen difficulty: " + chosenDifficulty.getName());
    };

    KeyAdapter numberAdapter = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!(isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                e.consume();
            }
        }
    };

    private void setMenuItems() {
        presets.removeAll();
        for (Difficulty difficulty : difficulties) {
            JMenuItem item = new JMenuItem(difficulty.getName());
            item.addActionListener(menuItemListener);
            presets.add(item);
        }
    }

}


