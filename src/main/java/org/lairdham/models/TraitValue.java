package org.lairdham.models;

public enum TraitValue {
    noValue(2) {
        @Override
        public TraitValue previous() {
            return this;
        }
    },
    d4(4),
    d6(6),
    d8(8),
    d10(10),
    d12(12) {
       @Override
       public TraitValue next() {
           return this;
       }
    };

    private final int numericalValue;

    TraitValue(int numericalValue) {
        this.numericalValue = numericalValue;
    }

    public int getNumericalValue() {
        return numericalValue;
    }

    public TraitValue next() {
        return values()[ordinal() +1];
    }
    public TraitValue previous() {
        return values()[ordinal() -1];
    }
    public boolean isGreaterThanOrEqualTo(TraitValue comparedValue) {
        return numericalValue >= comparedValue.getNumericalValue();
    }
}
