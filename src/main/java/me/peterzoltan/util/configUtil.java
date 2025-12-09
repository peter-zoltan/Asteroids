package me.peterzoltan.util;

import me.peterzoltan.game.Difficulty;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class configUtil {

    private static JSONArray jsonArray = new JSONArray();

    /**
     * Writes the current config to the config.json file.
     */
    private static void saveConfig() {
        try {
            Files.writeString((new File("config.json")).toPath(), jsonArray.toString(4));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error writing JSON file:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Reads the current config from config.json, and translates it to a list of Difficulties.
     * @return the resulting list of Difficulties.
     */
    public static List<Difficulty> getConfig() {
        try {
            File configFile = new File("config.json");
            String jsonString = Files.readString(configFile.toPath());
            jsonArray = new JSONArray(jsonString);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error loading JSON file:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        List<Difficulty> difficulties = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonDiff = (JSONObject) jsonArray.get(i);
            JSONObject jsonWeights = (JSONObject) jsonDiff.get("weights");
            Difficulty difficulty = new Difficulty(
                    (String) jsonDiff.get("name"),
                    (int) jsonDiff.get("hitpoints"),
                    (int) jsonDiff.get("frequency"),
                    new int[] {
                            (int) jsonWeights.get("small"),
                            (int) jsonWeights.get("medium"),
                            (int) jsonWeights.get("large")
                    }
            );
            difficulties.add(difficulty);
        }

        return difficulties;
    }

    /**
     * Adds a Difficulty to the JSONArray and writes the changes to config.json, if one with the same name doesn't
     * already exist.
     * @param difficulty the difficulty to be written to file.
     */
    public static void putDifficulty(Difficulty difficulty) {
        if (jsonArray == null) {
            JOptionPane.showMessageDialog(null,
                    "No JSON file loaded. Click 'Load JSON File' first.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonDiff = (JSONObject) jsonArray.get(i);
            if (jsonDiff.get("name").equals(difficulty.getName())) {
                JOptionPane.showMessageDialog(null,
                        "Difficulty with this name already exists.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        JSONObject diff = new JSONObject();
        diff.put("name", difficulty.getName());
        diff.put("hitpoints", difficulty.getHitpoints());
        diff.put("frequency", difficulty.getFrequency());

        JSONObject weights = new JSONObject();
        weights.put("small", difficulty.getWeights()[0]);
        weights.put("medium", difficulty.getWeights()[1]);
        weights.put("large", difficulty.getWeights()[2]);
        diff.put("weights", weights);

        jsonArray.put(diff);
        saveConfig();
    }

    /**
     * Removes the first Difficulty with the same name as the parameter from the JSONArray and writes the changes to
     * config.json.
     * @param difficulty the Difficulty to be removed.
     */
    public static void removeDifficulty(Difficulty difficulty) {
        if (jsonArray == null) {
            JOptionPane.showMessageDialog(null,
                    "No JSON file loaded. Click 'Load JSON File' first.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int removeInd = -1;
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).get("name").equals(difficulty.getName())) {
                removeInd = i;
                break;
            }
        }
        if (removeInd >= 0) {
            jsonArray.remove(removeInd);
        }
        saveConfig();
    }

}
