package net.revtut;

import java.util.Random;

/**
 * A company share. A share has initially a purchase price that will negotiated
 * between managers and investors.
 * When a investor buys a share he becomes the shareholder and might sell the share
 * by its new value that will vary accordingly the fluctuation of it.
 */
public class Share {

    /**
     * Random class
     */
    private static final Random random = new Random();

    /**
     * Shareholder's purchase price of the share
     */
    private double purchasePrice;

    /**
     * Shareholder's sell price of the share
     */
    private double salePrice;

    /**
     * Fluctuation ratio of the share
     */
    private double fluctuation;

    /**
     * Manager of the share
     */
    private Manager manager;

    /**
     * Current investor
     */
    private Investor shareholder;

    /**
     * Constructor of Share
     *
     * @param manager     manager of the share
     * @param fluctuation fluctuation of the share
     */
    public Share(final Manager manager, final double fluctuation) {
        this.fluctuation = fluctuation;
        this.manager = manager;
    }

    /**
     * Get the fluctuation ratio of the share
     *
     * @return fluctuation ratio of the share
     */
    public double getFluctuation() {
        return fluctuation;
    }

    /**
     * Get the manager of the share
     *
     * @return manager of the share
     */
    public Manager getManager() {
        return manager;
    }

    /**
     * Get the share's purchase price
     *
     * @return share's purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Set the share's purchase price
     *
     * @param purchasePrice new share's purchase price
     */
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * Get the share's sale price
     *
     * @return share's sale price
     */
    public double getSalePrice() {
        return salePrice;
    }

    /**
     * Set the share's sale price
     *
     * @param salePrice new share's sale price
     */
    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * Get the shareholder of the share
     *
     * @return shareholder of the share
     */
    public Investor getShareholder() {
        return shareholder;
    }

    /**
     * Set the shareholder of the share
     *
     * @param shareholder new shareholder of the share
     */
    public void setShareholder(Investor shareholder) {
        this.shareholder = shareholder;
    }

    /**
     * Apply the fluctuation the the share's price.
     */
    public void applyFluctuation() {
        double fluctuationValue = random.nextDouble() * this.fluctuation * this.purchasePrice;
        fluctuationValue *= random.nextBoolean() ? 1 : -1; // If true share's price will rise, otherwise will drop

        this.salePrice = this.purchasePrice + fluctuationValue;
    }
}
