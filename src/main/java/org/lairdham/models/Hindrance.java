package org.lairdham.models;

public class Hindrance {

    String name;
    HindranceType type;
    String shortDescription;
    String longDescription;

    public String getName() {
        return name;
    }

    public HindranceType getType() {
        return type;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public enum HindranceType {
        Minor, Major
    }
}
