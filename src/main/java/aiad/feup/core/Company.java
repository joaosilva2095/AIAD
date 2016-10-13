package aiad.feup.core;

import aiad.feup.core.exceptions.MalformedObjectException;

import java.util.Random;

/**
 * A company.
 * Holds information about the share.
 */
public class Company {

    /**
     * Random utility class
     */
    private static final Random random = new Random();

    /**
     * The name of the company
     */
    private String name;

    /**
     * Current stock price of the company
     */
    private int price;

    /**
     * Fluctuation average of the stock price of the company
     * Represents maximum % fluctuation of the company stock value
     */
    private int fluctuation;

    /**
     * True if company is double revenue type
     */
    private boolean isDoubleRevenue;

    /**
     * True if no new negotiation is allowed
     */
    private boolean closed;


    /**
     * Constructor of Company
     * @param price stock price of the company
     * @param fluctuation fluctuation of the company
     */
    public Company(String name, int price, boolean isDoubleRevenue, int fluctuation) throws MalformedObjectException {
        if(fluctuation > 101)
            throw new MalformedObjectException("Company " + name + " is being created with fluctuation over 100. Aborting.");
        this.name = name;

        this.price = price;
        this.isDoubleRevenue = isDoubleRevenue;
        this.fluctuation = fluctuation;
        this.closed = false;
    }

    /**
     * Get the price of the company
     * @return price of the company
     */
    public int getPrice() {
        return price;
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
        return closed;
    }

    /**
     * Close the company for auction
     */
    public void close() {
        this.closed = true;
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
        if(price == 0)
            return;

        double currentRoundFluctuation = (double) random.nextInt(fluctuation) / 100;
        if(isDoubleRevenue())
            currentRoundFluctuation *= 2;

        if(random.nextBoolean())
            price *= (1-currentRoundFluctuation);
        else
            price *= (1+currentRoundFluctuation);

        if(price < 0) {
            price = 0;
            System.out.println("Company " + name + " went bankrupt.");
        }


    }
}
