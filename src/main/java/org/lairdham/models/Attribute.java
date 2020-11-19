package org.lairdham.models;

public class Attribute implements Trait {

    String name;
    TraitValue value = TraitValue.d4;

    public Attribute(String name) {
        this.name = name;
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
    public TraitValue getValue() {
        return value;
    }
}
