package org.lairdham.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.lairdham.CustomRequirementDeserializer;

@JsonDeserialize(using= CustomRequirementDeserializer.class)
public class Requirement<T> {

    RequirementType type;

    T requiredValue;

    public RequirementType getType() {
        return type;
    }

    public void setType(RequirementType type) {
        this.type = type;
    }

    public T getRequiredValue() {
        return requiredValue;
    }

    public void setRequiredValue(T requiredValue) {
        this.requiredValue = requiredValue;
    }

    public enum RequirementType {
        Rank, Attribute, Skill, Edge
    }


    @Override
    public String toString() {
        return requiredValue.toString();
    }
}
