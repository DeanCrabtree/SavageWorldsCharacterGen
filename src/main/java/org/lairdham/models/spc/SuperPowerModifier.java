package org.lairdham.models.spc;

import java.util.Arrays;

public class SuperPowerModifier {

    String name;
    String description;
    int baseCost;
    boolean levelled;
    int[] steppedCosts;
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

    public int getPointsSpentOn() {
        return pointsSpentOn;
    }

    public void setPointsSpentOn(int pointsSpentOn) {
        this.pointsSpentOn = pointsSpentOn;
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
        return "SuperPowerModifier{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", baseCost=" + baseCost +
                ", levelled=" + levelled +
                ", steppedCosts=" + Arrays.toString(steppedCosts) +
                ", pointsSpentOn=" + pointsSpentOn +
                '}';
    }
}
