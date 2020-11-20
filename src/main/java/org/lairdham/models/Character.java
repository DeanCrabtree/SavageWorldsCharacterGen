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

    public Character() {
        //Default character values

        agility = new Attribute("agility");
        smarts = new Attribute("smarts");
        spirit = new Attribute("spirit");
        strength = new Attribute("strength");
        vigor = new Attribute("vigor");

        boating = new Skill("boating", agility);
        driving = new Skill("driving", agility);
        fighting = new Skill("fighting", agility);
        lockpicking = new Skill("lockpicking", agility);
        piloting = new Skill("piloting", agility);
        riding = new Skill("riding", agility);
        shooting = new Skill("shooting", agility);
        stealth = new Skill("stealth", agility);
        swimming = new Skill("swimming", agility);
        throwing = new Skill("throwing", agility);

        gambling = new Skill("gambling", smarts);
        healing = new Skill("healing", smarts);
        investigation = new Skill("investigation", smarts);
        notice = new Skill("notice", smarts);
        repair = new Skill("repair", smarts);
        streetwise = new Skill("streetwise", smarts);
        survival = new Skill("survival", smarts);
        taunt = new Skill("taunt", smarts);
        tracking = new Skill("tracking", smarts);

        intimidation = new Skill("intimidation", spirit);
        persuasion = new Skill("persuasion", spirit);
        climbing = new Skill("climbing", strength);

        experience = 0;
        bennies = 0;

        shaken = false;
        incapacitated = false;
    }

    //Derived Statistics
    public int getCharisma() {
        return 0;
    }

    public int getPace() {
        return 0;
    }

    public int getNaturalParry() {
        int skillBonus = !fighting.getValue().equals(TraitValue.noValue) ? fighting.getValue().getNumericalValue() / 2 : 1;
        return 2 + skillBonus;
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

    public void setAncestry(Ancestry ancestry) {
        this.ancestry = ancestry;
    }

    public void addExperience(int experience) {
        this.experience = this.experience + experience;
    }


}
