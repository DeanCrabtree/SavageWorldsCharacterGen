package org.lairdham.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Ancestry {

    private final String name;
    private final String description;
    private final ArrayList<String> ancestralAbilities;

    @JsonCreator
    public Ancestry(@JsonProperty("name") String name, @JsonProperty("description") String description,
                    @JsonProperty("ancestralAbilities") ArrayList<String> ancestralAbilities) {
        this.name = name;
        this.description = description;
        this.ancestralAbilities = ancestralAbilities;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getAncestralAbilities() {
        return ancestralAbilities;
    }
}
