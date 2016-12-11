package aiad.feup.desires;

import aiad.feup.models.Company;

/**
 * The Desire class, represents a desire of an agent
 */
public abstract class Desire {
    protected Company company;
    protected double desire;

    public abstract void calculateDesire();

    public double getDesire() {
        return desire;
    }
}
