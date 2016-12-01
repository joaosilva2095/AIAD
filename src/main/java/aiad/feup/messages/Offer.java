package aiad.feup.messages;

import aiad.feup.models.Company;
import jade.lang.acl.ACLMessage;

/**
 * Offer message
 */
public class Offer extends ACLMessage {

    /**
     * Company that this offer corresponds to
     */
    private Company company;

    /**
     * Offered value for the company
     */
    private double offeredValue;

    /**
     * True if it is a closed deal, false otherwise
     */
    private boolean isClosed;

    /**
     * Constructor of Offer
     * @param company company of the offer
     * @param offeredValue value offered for the company
     * @param isClosed true if it is a closed deal
     */
    public Offer(int performative, Company company, double offeredValue, boolean isClosed) {
        super(performative);
        this.company = company;
        this.offeredValue = offeredValue;
        this.isClosed = isClosed;
    }

    /**
     * Get the company that this offer corresponds to
     * @return company of the offer
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Get the offered value for the company
     * @return offered value for the company
     */
    public double getOfferedValue() {
        return offeredValue;
    }

    /**
     * Check if it is a closed deal
     * @return true if it is a closed deal
     */
    public boolean isClosed() {
        return isClosed;
    }
}
