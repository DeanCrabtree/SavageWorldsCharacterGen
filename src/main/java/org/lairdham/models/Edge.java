package org.lairdham.models;

import java.util.List;

public class Edge {

    String name;
    EdgeType type;
    String shortDescription;
    String longDescription;

    List<Requirement> requirements;

    public enum EdgeType {
        Background, Combat, Leadership, Power, Professional, Social, Weird, Wild_Card, Legendary
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdgeType getType() {
        return type;
    }

    public void setType(EdgeType type) {
        this.type = type;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    @Override
    public String toString() {
        return name;
    }
}
