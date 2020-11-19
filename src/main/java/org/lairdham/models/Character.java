package org.lairdham.models;

public class Character {

    String name;
    Ancestry ancestry;
    int experience;
    Rank rank;

    Attribute agility;
    Attribute smarts;
    Attribute spirit;
    Attribute strength;
    Attribute vigor;

    //Agility Skills
    Skill boating;
    Skill driving;
    Skill fighting;
    Skill lockpicking;
    Skill piloting;
    Skill riding;
    Skill shooting;
    Skill stealth;
    Skill swimming;
    Skill throwing;

    //Smarts Skills
    Skill gambling;
    Skill healing;
    Skill investigation;
    //TODO: Knowledge
    Skill notice;
    Skill repair;
    Skill streetwise;
    Skill survival;
    Skill taunt;
    Skill tracking;

    //Spirit Skills
    Skill intimidation;
    Skill persuasion;

    //Strength Skills;
    Skill climbing;

    int armourBonus;
    int parryBonus;
    int bennies;
    boolean shaken;
    boolean incapacitated;
    int wounds;
    int fatigueLevel;

    public int getCharisma() {
        return 0;
    }

    public int getPace() {
        return 0;
    }

    public int getNaturalParry() {
        return 0;
    }

    public int getTotalParry() {
        return 0;
    }

    public int getNaturalToughness() {
        return 2 + (vigor.getValue().getNumericalValue() / 2);
    }

    public int getTotalToughness() {
        return getNaturalToughness() + armourBonus;
    }


}
