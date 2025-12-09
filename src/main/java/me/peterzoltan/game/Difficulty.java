package me.peterzoltan.game;

public class Difficulty {
    /**
     * Name of the Difficulty.
     */
    String name;

    /**
     * Hitpoints of the Planet. The number of hits it can take (scaled with Asteroid size) before the game ends.
     */
    int hitpoints;

    /**
     * The number of seconds between each Asteroid spawn.
     */
    int frequency;

    /**
     * The distribution of the Asteroids' sizes.
     */
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
