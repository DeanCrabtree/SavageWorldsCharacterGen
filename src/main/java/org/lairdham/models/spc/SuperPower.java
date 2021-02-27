package org.lairdham.models.spc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuperPower {

    String name;
    String description;
    String trappings;
    int baseCost;
    boolean levelled;
    int[] steppedCosts;
    List<SuperPowerModifier> chosenModifiers = new ArrayList<>();
    List<SuperPowerModifier> allModifiers;
    int pointsSpentOn;

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

    public String getTrappings() {
        return trappings;
    }

    public void setTrappings(String trappings) {
        this.trappings = trappings;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
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

    public List<SuperPowerModifier> getChosenModifiers() {
        return chosenModifiers;
    }

    public void setChosenModifiers(List<SuperPowerModifier> chosenModifiers) {
        this.chosenModifiers = chosenModifiers;
    }

    public void addChosenModifier(SuperPowerModifier chosenModifier) {
        chosenModifiers.add(chosenModifier);
    }

    public void removeChosenModifier(SuperPowerModifier chosenModifier) {
        chosenModifiers.remove(chosenModifier);
    }

    public List<SuperPowerModifier> getAllModifiers() {
        if (allModifiers != null) {
            return allModifiers;
        } else {
            return new ArrayList<>();
        }
    }

    public void setAllModifiers(List<SuperPowerModifier> allModifiers) {
        this.allModifiers = allModifiers;
    }

    public int getPointsSpentOn() {
        return pointsSpentOn;
    }

    public void setPointsSpentOn(int pointsSpentOn) {
        this.pointsSpentOn = pointsSpentOn;
    }

    public void addPointsSpentOn(int addition) {
        pointsSpentOn += addition;
    }

    public void subtractPointsSpentOn(int subtraction) {
        pointsSpentOn -= subtraction;
    }

    public String getCostAsString() {
        if (levelled) {
            return baseCost + "/Level";
        } else if (steppedCosts != null && steppedCosts.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (int steppedCost : steppedCosts) {
                builder.append(steppedCost);
                builder.append("/");
            }
            return builder.substring(0, builder.length() - 1);
        } else {
            return baseCost + "";
        }
    }

    @Override
    public String toString() {
        return "SuperPower{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", trappings='" + trappings + '\'' +
                ", baseCost=" + baseCost +
                ", levelled=" + levelled +
                ", steppedCosts=" + Arrays.toString(steppedCosts) +
                ", chosenModifiers=" + chosenModifiers +
                ", allModifiers=" + allModifiers +
                ", pointsSpentOn=" + pointsSpentOn +
                '}';
    }
}
