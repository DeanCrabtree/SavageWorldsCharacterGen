package org.lairdham.models;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

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

    List<Hindrance> hindrances = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    public Character() {
        //Default character values

        agility = new Attribute("Agility");
        smarts = new Attribute("Smarts");
        spirit = new Attribute("Spirit");
        strength = new Attribute("Strength");
        vigor = new Attribute("Vigor");

        boating = new Skill("Boating", agility);
        driving = new Skill("Driving", agility);
        fighting = new Skill("Fighting", agility);
        lockpicking = new Skill("Lockpicking", agility);
        piloting = new Skill("Piloting", agility);
        riding = new Skill("Riding", agility);
        shooting = new Skill("Shooting", agility);
        stealth = new Skill("Stealth", agility);
        swimming = new Skill("Swimming", agility);
        throwing = new Skill("Throwing", agility);

        gambling = new Skill("Gambling", smarts);
        healing = new Skill("Healing", smarts);
        investigation = new Skill("Investigation", smarts);
        notice = new Skill("Notice", smarts);
        repair = new Skill("Repair", smarts);
        streetwise = new Skill("Streetwise", smarts);
        survival = new Skill("Survival", smarts);
        taunt = new Skill("Taunt", smarts);
        tracking = new Skill("Tracking", smarts);

        intimidation = new Skill("Intimidation", spirit);
        persuasion = new Skill("Persuasion", spirit);
        climbing = new Skill("Climbing", strength);

        experience = 0;
        rank = Rank.Novice;
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

    public List<Hindrance> getAllHindrances() {
        return hindrances;
    }

    public void setHindrances(List<Hindrance> hindrances) {
        this.hindrances = hindrances;
    }

    public void addHindrance(Hindrance hindrance) {
        hindrances.add(hindrance);
    }

    public List<Edge> getAllEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public Rank getRank() {
        return rank;
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
    public Map<String, Attribute> getAllAttributes() {
        Map<String, Attribute> allAttributes = new HashMap<>();
        allAttributes.put(agility.getName(), agility);
        allAttributes.put(smarts.getName(), smarts);
        allAttributes.put(spirit.getName(), spirit);
        allAttributes.put(strength.getName(), strength);
        allAttributes.put(vigor.getName(), vigor);
        return allAttributes;
    }

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

    public Skill getSkill(String skillName) {
        try {
            return (Skill) Character.class.getDeclaredMethod("get"+skillName).invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.out.println("No such skill as: " + skillName);
        }
        return null;
    }

    public List<Skill> getAllSkills() {
        return Arrays.stream(this.getClass().getDeclaredFields()).filter(field -> field.getType().equals(Skill.class)).map(field -> {
            try {
                return (Skill) field.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

}
