package aiad.feup.models;

import aiad.feup.agents.RemoteAgent;
import aiad.feup.exceptions.MalformedObjectException;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * A company.
 * Holds information about the share.
 */
public class Company implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -484968079362726486L;

    /**
     * Owner of the company
     */
    private RemoteAgent owner;

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
     * @param name name of the company
     * @param value stock price of the company
     * @param isDoubleRevenue true if is double revenue
     * @param fluctuation fluctuation of the company
     */
    public Company(final String name, final double value, final boolean isDoubleRevenue, final double fluctuation) {
        if(fluctuation > 100 || fluctuation < 0)
            throw new MalformedObjectException("Company " + name + " fluctuation must be a value between 0 and 100.");

        this.owner = null;
        this.name = name;
        this.value = value;
        this.isDoubleRevenue = isDoubleRevenue;
        this.fluctuation = fluctuation;
        this.isClosed = false;
    }

    /**
     * Get the owner of the company
     * @return owner of the company
     */
    public RemoteAgent getOwner() {
        return owner;
    }

    /**
     * Returns the name of the company
     * @return the name of the company
     */
    public String getName() {
        return name;
    }

    /**
     * Get the price of the company
     * @return price of the company
     */
    public double getValue() {
        return value;
    }

    /**
     * Gets the fluctuation of the company
     * @return the fluctuation of the company
     */
    public double getFluctuation() {
        return fluctuation;
    }

    /**
     * Set the owner of the company
     * @param owner owner of the company
     */
    public void setOwner(final RemoteAgent owner) {
        this.owner = owner;
    }

    /**
     * Returns whether the company is double revenue
     * @return whether the company is double revenue
     */
    public boolean isDoubleRevenue() {
        return isDoubleRevenue;
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
     * Apply a fluctuation to the stock price of the company based on its risk
     */
    public void applyFluctuation() {
        double currentRoundFluctuation = Math.random() * fluctuation / 100;

        if(Math.random() > 0.5)
            value *= (1-currentRoundFluctuation);
        else
            value *= (1+currentRoundFluctuation);

        DecimalFormat df = new DecimalFormat("#0.00");
        System.out.println(getName() + " values now " + df.format(value));
    }
}
