package org.lairdham.models.spc;

public class SuperPowerModifier {

    String name;
    String description;
    int cost;
    boolean levelled;
    int[] steppedCosts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isLevelled() {
        return levelled;
    }

    public void setLevelled(boolean levelled) {
        this.levelled = levelled;
    }

    public int[] getSteppedCosts() {
        return steppedCosts;
    }

    public void setSteppedCosts(int[] steppedCosts) {
        this.steppedCosts = steppedCosts;
    }
}