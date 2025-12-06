package me.peterzoltan.game;

public class Difficulty {
    String name;
    int hitpoints;
    int frequency;
    int[] weights;
    public Difficulty(String name, int hitpoints, int frequency, int[] weights) {
        this.name = name;
        this.hitpoints = hitpoints;
        this.frequency = frequency;
        this.weights = weights;
    }
    public String getName() {
        return name;
    }
    public int getHitpoints() {
        return hitpoints;
    }
    public int getFrequency() {
        return frequency;
    }
    public int[] getWeights() {
        return weights;
    }
}
