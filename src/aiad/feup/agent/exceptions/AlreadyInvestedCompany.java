package aiad.feup.agent.exceptions;

import aiad.feup.agent.Investor;

/**
 * Player has already invested in the company
 */
public class AlreadyInvestedCompany extends Exception {

    /**
     * Constructor of {@link AlreadyInvestedCompany}
     * @param investor player that has already invested in the company
     */
    public AlreadyInvestedCompany(final Investor investor) {
        super(investor.getName() + " has already invested in the company.");
    }
}
