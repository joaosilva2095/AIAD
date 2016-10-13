package aiad.feup.agent.exceptions;

import aiad.feup.agent.Investor;

/**
 * Player has not invested in the company
 */
public class NotInvestedCompany extends Exception {

    /**
     * Constructor of {@link NotInvestedCompany}
     * @param investor player that has not invested in the company
     */
    public NotInvestedCompany(final Investor investor) {
        super(investor.getName() + " has not invested in the company.");
    }
}
