package aiad.feup;

/**
 * A company.
 */
public class Company {

    /**
     * Manager owner of the company
     */
    private Manager manager;

    /**
     * Investor of the company, might not exist
     */
    private Investor shareholder;

    /**
     * Current investment value
     */
    private int value;

    /**
     * True if no new negotiation is allowed
     */
    private boolean closed;
}
