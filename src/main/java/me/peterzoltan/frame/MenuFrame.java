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

    /**
     * Sets up the Frame, initializing all of it's Components.
     * Contains a BoxLayout with it's direct components in a cloumn aligned to the center.
     * @param title the title of the Frame to be displayed.
     */
    public MenuFrame(String title) {
        super(title);
        setSize(400, 100);
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

        difficulties = configUtil.getConfig();
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

        saveButton = new JButton("Save");
        saveButton.addActionListener(saveListener);
        buttonPanel.add(saveButton);
        saveButton.setVisible(false);

        add(buttonPanel);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Initializes the Panel where the new Difficulties can be put in.
     * Contains a GridLayoit with two columns and an unspecified amount of rows (6 in practice).
     */
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

    /**
     * Initializes a default Difficulty if it does not already exist so that a game can be started without setting one.
     * Sets chosenDifficulty to the default one.
     */
    private void initializeDefaultDifficulty() {
        if(!difficulties.isEmpty()) {
            for (Difficulty difficulty : difficulties) {
                if (difficulty.getName().equals("default")) {
                    chosenDifficulty = difficulty;
                    return;
                }
            }
        }
        Difficulty defaultDiff = new Difficulty(
                "default",
                9,
                2,
                new int[] {1, 1, 1}
        );
        difficulties.add(defaultDiff);
        configUtil.putDifficulty(defaultDiff);
        chosenDifficulty = defaultDiff;
    }

    /**
     * On clicking the start button starts the game in a new Frame with the chosen Difficulty.
     */
    ActionListener startActionListener = e -> {
        GameFrame frame = new GameFrame(chosenDifficulty);
        frame.pack();
        frame.setVisible(true);
        SwingUtilities.invokeLater(frame::init);
    };

    /**
     * On clicking the settings button the settings are either revealed or hidden.
     */
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
            setSize(400, 100);
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
            setSize(400, 300);
        }
    };

    /**
     * Sets the Difficulty with the same name as the clicked MenuItem s the chosenDifficulty, and adjust the label.
     */
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

    /**
     * Adds a new Difficulty with the configuration put in to the list of Difficulties and saves it to config.json.
     * Checks for unallowed 0 values and duplicate names.
     */
    ActionListener saveListener = e -> {
        try {
            int wSmall = parseInt(smallWeightField.getText());
            int wMedium = parseInt(mediumWeightField.getText());
            int wLarge = parseInt(largeWeightField.getText());
            if (wSmall + wMedium + wLarge == 0) {
                JOptionPane.showMessageDialog(null,
                        "All weights can't be 0",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int hitpoints = parseInt(hitpointsField.getText());
            if (hitpoints == 0) {
                JOptionPane.showMessageDialog(null,
                        "Hitpoints can't be 0",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int frequency = parseInt(frequencyField.getText());
            if (frequency == 0) {
                JOptionPane.showMessageDialog(null,
                        "Frequency can't be 0",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Difficulty difficulty = new Difficulty(
                    nameField.getText(),
                    hitpoints,
                    frequency,
                    new int[]{
                            wSmall,
                            wMedium,
                            wLarge
                    }
            );
            for (Difficulty difficulty1 : difficulties) {
                if (difficulty1.getName().equals(difficulty.getName())) {
                    JOptionPane.showMessageDialog(null,
                            "Difficulty with this name already exists.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            configUtil.putDifficulty(difficulty);
            difficulties.add(difficulty);
            JMenuItem item = new JMenuItem(difficulty.getName());
            item.addActionListener(menuItemListener);
            presets.add(item);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Incorrect diffculty format:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    };

    /**
     * Removes the chosenDifficulty from config.json and the list of Difficulties, and set the chosenDifficulty to the
     * default, and adjust the label.
     * Cannot remove the default Difficulty.
     */
    ActionListener removeListener = e -> {
        if (chosenDifficulty.getName().equals("default")) {
            return;
        }
        configUtil.removeDifficulty(chosenDifficulty);
        difficulties.remove(chosenDifficulty);
        for (Difficulty difficulty : difficulties) {
            if (difficulty.getName().equals("default")) {
                chosenDifficulty = difficulty;
                chosenLabel.setText("Chosen difficulty: " + chosenDifficulty.getName());
            }
        }
        setMenuItems();
    };

    /**
     * Only allows digits to be typed. Allows for deletion.
     */
    KeyAdapter numberAdapter = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!(isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                e.consume();
            }
        }
    };

    /**
     * Resets and reinitializes the MenuItems of the Menu that shows the Difficulties.
     */
    private void setMenuItems() {
        presets.removeAll();
        for (Difficulty difficulty : difficulties) {
            JMenuItem item = new JMenuItem(difficulty.getName());
            item.addActionListener(menuItemListener);
            presets.add(item);
        }
    }

}


