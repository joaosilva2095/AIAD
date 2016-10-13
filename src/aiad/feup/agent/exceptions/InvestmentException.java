package aiad.feup.agent.exceptions;

import aiad.feup.agent.Investor;

/**
 * Investment exception
 */
public class InvestmentException extends Exception {

    /**
     * Constructor of {@link InvestmentException}
     * @param investor investor that attempted to invest
     * @param message error message
     */
    public InvestmentException(final Investor investor, final String message) {
        super(investor.getName() + " " + message);
    }

}
