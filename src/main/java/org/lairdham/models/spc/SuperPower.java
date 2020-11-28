package org.lairdham.models.spc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lairdham.App;

import java.io.IOException;
import java.util.List;

public class SuperPower {

    String name;
    String description;
    String trappings;
    int cost;
    boolean levelled;
    int[] steppedCosts;
    List<SuperPowerModifier> modifiers;
    List<SuperPowerModifier> genericModifiers;


    public SuperPower() {
        loadGenericModifiers();
    }

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

    public List<SuperPowerModifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<SuperPowerModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public List<SuperPowerModifier> getGenericModifiers() {
        return genericModifiers;
    }

    private void loadGenericModifiers() {
        try {
            genericModifiers = new ObjectMapper().readValue(App.class.getResource("datafiles/spc/genericModifiers.json"), new TypeReference<>() {});
        } catch (IOException e) {
            System.out.println("Exception trying to load datafiles/spc/genericModifiers.json: " + e);
        }

    }

}
