package org.lairdham.models;

public interface Trait {

    void setValue(TraitValue value);
    void incrementValue();
    void decrementValue();
    TraitValue getValue();
}
