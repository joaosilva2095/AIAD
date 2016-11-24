package aiad.feup.models;

import aiad.feup.exceptions.MalformedObjectException;

/**
 * A company.
 * Holds information about the share.
 */
public class Company {


    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -484968079362726486L;

    /**
     * The name of the company
     */
    private String name;

    /**
     * Current stock value of the company
     */
    private double value;

    /**
     * Fluctuation average of the stock price of the company
     * Represents maximum % fluctuation of the company stock value
     */
    private double fluctuation;

    /**
     * True if company is double revenue type
     */
    private boolean isDoubleRevenue;

    /**
     * True if no new negotiation is allowed
     */
    private boolean isClosed;

    /**
     * Constructor of Company
     * @param value stock price of the company
     * @param fluctuation fluctuation of the company
     */
    public Company(String name, double value, boolean isDoubleRevenue, double fluctuation) {
        if(fluctuation > 100 || fluctuation < 0)
            throw new MalformedObjectException("Company " + name + " fluctuation must be a value between 0 and 100.");

        this.name = name;
        this.value = value;
        this.isDoubleRevenue = isDoubleRevenue;
        this.fluctuation = fluctuation;
        this.isClosed = false;
    }

    /**
     * Get the price of the company
     * @return price of the company
     */
    public double getValue() {
        return value;
    }


    /**
     * Returns the name of the company
     * @return the name of the company
     */
    public String getName() {
        return name;
    }


    /**
     * Check if the company is closed for auction
     * @return true if closed for auction
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Close the company for auction
     */
    public void close() {
        this.isClosed = true;
    }


    /**
     * Gets the fluctuation of the company
     * @return the fluctuation of the company
     */
    public double getFluctuation() {
        return fluctuation;
    }

    /**
     * Returns whether the company is double revenue
     * @return whether the company is double revenue
     */
    public boolean isDoubleRevenue() {
        return isDoubleRevenue;
    }

    /**
     * Apply a fluctuation to the stock price of the company based on its risk
     */
    public void applyFluctuation() {
        //TODO debate whether it should be removed from the board
        if(value == 0)
            return;

        double currentRoundFluctuation = Math.random() * fluctuation / 100;

        if(Math.random() > 0.5)
            value *= (1-currentRoundFluctuation);
        else
            value *= (1+currentRoundFluctuation);

    }
}
