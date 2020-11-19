package org.lairdham.models;

public enum Rank {
    Novice(0), Seasoned(20), Veteran(40), Heroic(60),
    Legendary(80) {
        @Override
        public Rank next() {
            return this;
        }
    };

    private final int minXPThreshold;

    Rank(int minXPThreshold) {
        this.minXPThreshold = minXPThreshold;
    }

    public int getMinXPThreshold() {
        return minXPThreshold;
    }

    public Rank next() {
        return values()[ordinal() +1];
    }
}
