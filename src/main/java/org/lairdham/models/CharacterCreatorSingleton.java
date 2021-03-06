package org.lairdham.models;

import java.util.List;

public final class CharacterCreatorSingleton {

    private Character character;
    private int edgePoints = 1;
    private int extraEdgePointsBought = 0;
    private int hindrancePoints = 0;
    private int attributePoints = 5;
    private int extraAttributePointsBought = 0;
    private int skillPoints = 15;
    private int extraSkillPointsBought = 0;

    private int majorHindrancesAllowed = 1;
    private int minorHindrancesAllowed = 2;

    private int majorHindrancesChosen = 0;
    private int minorHindrancesChosen = 0;

    private List<Edge> settingRequiredEdges;
    private int initialPowerPoints = 20;
    private int currentPowerPoints = initialPowerPoints;


    private final static CharacterCreatorSingleton CHARACTER_CREATOR_INSTANCE = new CharacterCreatorSingleton();

    private CharacterCreatorSingleton() {}

    public static CharacterCreatorSingleton getInstance() {
        return CHARACTER_CREATOR_INSTANCE;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public void adjustEdgePoints(int amount) {
        edgePoints+=amount;
    }

    public int getEdgePoints() {
        return edgePoints;
    }

    public void adjustExtraEdgePointsBought(int amount) {
        extraEdgePointsBought += amount;
    }

    public void setExtraEdgePointsBought(int amount) {
        this.extraEdgePointsBought = amount;
    }

    public int getExtraEdgePointsBought() {
        return extraEdgePointsBought;
    }

    public void adjustHindrancePoints(int amount) {
        hindrancePoints+=amount;
    }

    public int getHindrancePoints() {
        return hindrancePoints;
    }

    public void adjustAttributePoints(int amount) {
        attributePoints+=amount;
    }

    public int getAttributePoints() {
        return attributePoints;
    }

    public void adjustExtraAttributePointsBought(int amount) {
        extraAttributePointsBought += amount;
    }

    public void setExtraAttributePointsBought(int amount) {
        this.extraAttributePointsBought = amount;
    }

    public int getExtraAttributePointsBought() {
        return extraAttributePointsBought;
    }

    public void adjustSkillPoints(int amount) {
        skillPoints+=amount;
    }

    public void setSkillPoints(int amount) {
        this.skillPoints = amount;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void adjustExtraSkillPointsBought(int amount) {
        extraSkillPointsBought += amount;
    }

    public void setExtraSkillPointsBought(int amount) {
        this.extraSkillPointsBought = amount;
    }

    public int getExtraSkillPointsBought() {
        return extraSkillPointsBought;
    }

    public int getHindrancesAllowed(Hindrance.HindranceType type) {
        switch (type){
            case Major:
                return majorHindrancesAllowed;

            case Minor:
                return minorHindrancesAllowed;

            default:
                return 0;
        }
    }

    public void adjustHindrancesChosen(Hindrance.HindranceType type, int value) {
        switch (type){
            case Major:
                majorHindrancesChosen+=value;
                break;

            case Minor:
                minorHindrancesChosen+=value;
                break;

            default:
                break;
        }
    }

    public int getRemainingHindrances(Hindrance.HindranceType type) {
        switch (type){
            case Major:
                return majorHindrancesAllowed - majorHindrancesChosen;

            case Minor:
                return minorHindrancesAllowed - minorHindrancesChosen;

            default:
                return 0;
        }
    }

    public boolean allHindrancesChosen() {
        return majorHindrancesAllowed+minorHindrancesAllowed==0;
    }

    public List<Edge> getSettingRequiredEdges() {
        return settingRequiredEdges;
    }

    public void setSettingRequiredEdges(List<Edge> settingRequiredEdges) {
        this.settingRequiredEdges = settingRequiredEdges;
    }

    public int getCurrentPowerPoints() {
        return currentPowerPoints;
    }

    public void setCurrentPowerPoints(int currentPowerPoints) {
        this.currentPowerPoints = currentPowerPoints;
    }

    public void adjustCurrentPowerPoints(int amount) {
        this.currentPowerPoints+=amount;
    }

    public int getInitialPowerPoints() {
        return initialPowerPoints;
    }

    public void setInitialPowerPoints(int initialPowerPoints) {
        this.initialPowerPoints = initialPowerPoints;
    }
}
