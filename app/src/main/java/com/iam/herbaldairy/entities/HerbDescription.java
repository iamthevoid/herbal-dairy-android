package com.iam.herbaldairy.entities;

public class HerbDescription {
    private String name;
    private int weight;

    public HerbDescription(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String name() {
        return name;
    }

    public int weight() {
        return weight;
    }
}
