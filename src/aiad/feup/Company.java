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
     * Apply a fluctuation to the stock price of the company
     */
    public void applyFluctuation() {
        int multiplier = random.nextBoolean() ? 1 : -1;
        double offset = fluctuation * price;

        price += multiplier * offset;
    }
}
