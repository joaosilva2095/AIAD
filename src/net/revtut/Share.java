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
     * Manager of the share
     */
    private Manager manager;

    /**
     * Shareholder of the share
     */
    private Investor shareholder;

    /**
     * Current investment value
     */
    private int value;

    /**
     * Fluctuation of the share
     */
    private double fluctuation;

    /**
     * True if no new negotiation is allowed
     */
    private boolean closed;
}
