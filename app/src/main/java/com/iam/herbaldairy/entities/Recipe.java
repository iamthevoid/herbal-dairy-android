package com.iam.herbaldairy.entities;

import java.util.ArrayList;

public class Recipe {

    private static ArrayList<Recipe> recipes = new ArrayList<>();

    protected double spiritVolume;
    protected ArrayList<HerbOwned> herbs;

    protected String infuse;
    protected String distill;
    protected String colouring;

    public static void init() {

    }
}
