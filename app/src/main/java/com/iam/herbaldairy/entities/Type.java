package com.iam.herbaldairy.entities;

public enum Type {

    herba("herba"),
    flores("flores"),
    fructus("fructus"),
    radix("radix");

    Type(String description) {
        this.description = description;
    }

    private String description;

    public static String[] names() {
        String[] names = new String[Type.values().length];
        int i = 0;
        for (Type type : Type.values()) {
            names[i++] = type.name();
        }
        return names;
    }

}
