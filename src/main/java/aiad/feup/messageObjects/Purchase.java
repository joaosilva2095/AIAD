package aiad.feup.messageObjects;

import aiad.feup.agents.RemoteAgent;
import aiad.feup.models.Company;

import java.io.Serializable;

/**
 * Purchase message.
 * Updates the board telling that a company was bought by an investor
 */
public class Purchase implements Serializable {

    /**
     * Company that was purchased
     */
    private Company company;

    /**
     * Value of the purchase
     */
    private double value;

    /**
     * Name of the investor
     */
    private RemoteAgent investor;

    /**
     * Constructor of Purchase
     * @param company company that was purchased
     * @param value value of the purchase
     * @param investor investor that purchased
     */
    public Purchase(final Company company, final double value, final RemoteAgent investor) {
        this.company = company;
        this.value = value;
        this.investor = investor;
    }

    /**
     * Get the company that was purchased
     * @return company that was purchased
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Get the value of the purchase
     * @return value of the purchase
     */
    public double getValue() {
        return value;
    }

    /**
     * Get the investor that purchased the company
     * @return investor that purchased the company
     */
    public RemoteAgent getInvestor() {
        return investor;
    }
}