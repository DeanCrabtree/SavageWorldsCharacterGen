package org.lairdham.models;

public final class CharacterCreatorSingleton {

    private Character character;
    private int edgePoints = 0;
    private int hindrancePoints = 0;
    private int attributePoints = 5;
    private int skillPoints = 15;


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

    public void adjustSkillPoints(int amount) {
        skillPoints+=amount;
    }

    public void setSkillPoints(int amount) {
        this.skillPoints = amount;
    }

    public int getSkillPoints() {
        return skillPoints;
    }
}
