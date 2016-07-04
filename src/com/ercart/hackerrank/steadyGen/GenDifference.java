package com.ercart.hackerrank.steadyGen;

/**
 * @author dkyryk
 */
public class GenDifference {
    private int appliedDifference;
    private final int initialDifference;

    public GenDifference(int initialDifference) {
        this.initialDifference = initialDifference;
        this.appliedDifference = initialDifference;
    }

    public void resetAppliedDifference() {
        appliedDifference = initialDifference;
    }

    public void increaseAppliedDifference() {
        this.appliedDifference++;
    }

    public void decreaseAppliedDifference() {
        this.appliedDifference--;
    }

    public int getAppliedDifference() {
        return appliedDifference;
    }

    public int getInitialDifference() {
        return initialDifference;
    }
}
