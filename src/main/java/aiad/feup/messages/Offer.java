package aiad.feup.messages;

import aiad.feup.agents.RemoteAgent;
import aiad.feup.models.Company;

import java.io.Serializable;

/**
 * Offer message
 */
public class Offer implements Serializable {

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
     * The remote agent who is making the offer
     */
    private RemoteAgent offerer;

    /**
     * Constructor of Offer
     * @param company company of the offer
     * @param offeredValue value offered for the company
     * @param isClosed true if it is a closed deal
     */
    public Offer(Company company, double offeredValue, boolean isClosed, RemoteAgent offerer) {
        this.company = company;
        this.offeredValue = offeredValue;
        this.isClosed = isClosed;
        this.offerer = offerer;
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


    public void setCompany(Company company) {
        this.company = company;
    }

    public void setOfferedValue(double offeredValue) {
        this.offeredValue = offeredValue;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public RemoteAgent getOfferer() { return offerer; }
}
