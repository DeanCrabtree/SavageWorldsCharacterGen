package org.lairdham.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Attribute implements Trait {

    String name;
    TraitValue value = TraitValue.d4;

    public Attribute(String name) {
        this.name = name;
    }

    @JsonCreator
    public Attribute(@JsonProperty("name") String name, @JsonProperty("value") TraitValue value) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(name, attribute.name) &&
                value == attribute.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
