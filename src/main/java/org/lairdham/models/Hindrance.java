package org.lairdham.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hindrance hindrance = (Hindrance) o;
        return Objects.equals(name, hindrance.name) &&
                type == hindrance.type &&
                Objects.equals(shortDescription, hindrance.shortDescription) &&
                Objects.equals(longDescription, hindrance.longDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, shortDescription, longDescription);
    }
}
