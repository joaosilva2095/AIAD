package aiad.feup;

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
    public Company(int price, double fluctuation) {
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
     * Buy the company for a given price
     * @param price price of the transaction
     * @throws IllegalStateException if the company is already closed for sale
     */
    public void buy(int price) {
        if(isClosed())
            throw new IllegalStateException("Company is closed for sale.");

        this.price = price;
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
