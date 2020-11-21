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

    ////////////////Derived Statistics////////////////////
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
    ///////////////End of Derived Statistics///////////////

    public Ancestry getAncestry() {
        return ancestry;
    }
    public void setAncestry(Ancestry ancestry) {
        this.ancestry = ancestry;
    }

    public void addExperience(int experience) {
        this.experience = this.experience + experience;
    }

    public void adjustTrait(Trait trait, int value) {
        if (value > 0) {
            trait.incrementValue();
        } else {
            trait.decrementValue();
        }
    }

    ////////Get Traits//////////////////////////////
    public Attribute getAgility() {
        return agility;
    }

    public Attribute getSmarts() {
        return smarts;
    }

    public Attribute getSpirit() {
        return spirit;
    }

    public Attribute getStrength() {
        return strength;
    }

    public Attribute getVigor() {
        return vigor;
    }

    public Skill getBoating() {
        return boating;
    }

    public Skill getDriving() {
        return driving;
    }

    public Skill getFighting() {
        return fighting;
    }

    public Skill getLockpicking() {
        return lockpicking;
    }

    public Skill getPiloting() {
        return piloting;
    }

    public Skill getRiding() {
        return riding;
    }

    public Skill getShooting() {
        return shooting;
    }

    public Skill getStealth() {
        return stealth;
    }

    public Skill getSwimming() {
        return swimming;
    }

    public Skill getThrowing() {
        return throwing;
    }

    public Skill getGambling() {
        return gambling;
    }

    public Skill getHealing() {
        return healing;
    }

    public Skill getInvestigation() {
        return investigation;
    }

    public Skill getNotice() {
        return notice;
    }

    public Skill getRepair() {
        return repair;
    }

    public Skill getStreetwise() {
        return streetwise;
    }

    public Skill getSurvival() {
        return survival;
    }

    public Skill getTaunt() {
        return taunt;
    }

    public Skill getTracking() {
        return tracking;
    }

    public Skill getIntimidation() {
        return intimidation;
    }

    public Skill getPersuasion() {
        return persuasion;
    }

    public Skill getClimbing() {
        return climbing;
    }

}
