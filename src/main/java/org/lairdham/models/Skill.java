package org.lairdham.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Skill implements Trait {

    private String name;
    private TraitValue value = TraitValue.noValue;
    private Attribute linkedAttribute;

    public Skill(String name, Attribute linkedAttribute) {
        this.name = name;
        this.linkedAttribute = linkedAttribute;
    }

    @JsonCreator
    public Skill(@JsonProperty("name") String name, @JsonProperty("linkedAttribute") Attribute linkedAttribute,
                 @JsonProperty("value") TraitValue value) {
        this.name = name;
        this.linkedAttribute = linkedAttribute;
        this.value = value;
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

    public String getName() {
        return name;
    }

    @JsonIgnore
    public boolean isEqualToOrGreaterThanLinkedAttribute() {
        return this.value.isGreaterThanOrEqualTo(linkedAttribute.getValue());
    }

    @JsonIgnore
    public boolean isGreaterThanLinkedAttribute() {
        return this.value.getNumericalValue() > linkedAttribute.getValue().getNumericalValue();
    }

    @Override
    public String toString() {
        return name + " " + value;
    }
}
