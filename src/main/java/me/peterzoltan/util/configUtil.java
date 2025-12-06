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


    public static void putDifficulty(Difficulty difficulty) {
        if (jsonArray == null) {
            JOptionPane.showMessageDialog(null,
                    "No JSON file loaded. Click 'Load JSON File' first.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
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
    }

    public static void saveConfig() {
        try {
            Files.writeString((new File("config.json")).toPath(), jsonArray.toString(4));
            JOptionPane.showMessageDialog(null, "Saved to file!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error writing JSON file:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
