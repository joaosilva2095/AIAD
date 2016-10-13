package aiad.feup.core;

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
     * Fluctuation of the stock price of the company
     */
    private double fluctuation;

    /**
     * True if no new negotiation is allowed
     */
    private boolean closed;


    /**
     * Constructor of Company
     * @param price stock price of the company
     * @param fluctuation fluctuation of the company
     */
    public Company(String name, int price, double fluctuation) {
        this.name = name;

        this.price = price;
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
     * Apply a fluctuation to the stock price of the company
     */
    public void applyFluctuation() {
        int multiplier = random.nextBoolean() ? 1 : -1;
        double offset = fluctuation * price;

        price += multiplier * offset;
    }
}
