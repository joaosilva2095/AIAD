package aiad.feup.intentions;

import aiad.feup.models.Company;

/**
 * The Intention class, represents a desire of an agent
 */
public abstract class Intention {
    protected Company company;
    protected double weight;

    public abstract void calculateWeight();

    public double getWeight() {
        return weight;
    }
    public Company getCompany() {
        return company;
    }
}
