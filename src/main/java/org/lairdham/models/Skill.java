package org.lairdham.models;

public class Skill implements Trait {

    private String name;
    private TraitValue value = TraitValue.noValue;
    private Attribute linkedAttribute;

    public Skill(String name, Attribute linkedAttribute) {
        this.name = name;
        this.linkedAttribute = linkedAttribute;
    }

    @Override
    public void setValue(TraitValue value) {
        this.value = value;
    }

    @Override
    public void incrementValue() {
        value = value.next();
    }

    @Override
    public void decrementValue() {
        value = value.previous();
    }

    @Override
    public TraitValue getValue() {
        return value;
    }

    public Attribute getLinkedAttribute() {
        return linkedAttribute;
    }

    public boolean isEqualToOrGreaterThanLinkedAttribute() {
        return this.value.getNumericalValue() >= linkedAttribute.getValue().getNumericalValue();
    }

    public boolean isGreaterThanLinkedAttribute() {
        return this.value.getNumericalValue() > linkedAttribute.getValue().getNumericalValue();
    }
}
