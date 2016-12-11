package aiad.feup.messageObjects;

import aiad.feup.agents.RemoteAgent;
import aiad.feup.models.Company;
import aiad.feup.models.OfferType;

import java.io.Serializable;

/**
 * Offer message
 */
public class Offer implements Serializable {

    /**
     * Serial Version UUID
     */
    private static final long serialVersionUID = 6930657412037877676L;

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
     * Type of the offer
     */
    private OfferType type;

    /**
     * The remote agent who made the offer
     */
    private RemoteAgent investor;

    /**
     * Constructor of Offer
     * @param company company of the offer
     * @param offeredValue value offered for the company
     * @param isClosed true if it is a closed deal
     * @param type type of the offer
     * @param investor investor of the offer
     */
    public Offer(Company company, double offeredValue, boolean isClosed, OfferType type, RemoteAgent investor) {
        this.company = company;
        this.offeredValue = offeredValue;
        this.isClosed = isClosed;
        this.type = type;
        this.investor = investor;
    }

    public Company getCompany() {
        return company;
    }

    public double getOfferedValue() {
        return offeredValue;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public OfferType getType() {
        return type;
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

    public RemoteAgent getInvestor() { return investor; }
}
