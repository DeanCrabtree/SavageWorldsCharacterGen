package org.lairdham.models;

public final class CharacterCreatorSingleton {

    private Character character;
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
}
