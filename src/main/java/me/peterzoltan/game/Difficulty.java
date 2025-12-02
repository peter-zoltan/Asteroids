package me.peterzoltan.game;

public class Difficulty {
    String name;
    int hitpoints;
    double frequency;
    int[] weights;
    public Difficulty(String name, int hitpoints, double frequency, int[] weights) {
        this.name = name;
        this.hitpoints = hitpoints;
        this.frequency = frequency;
        this.weights = weights;
    }
}
