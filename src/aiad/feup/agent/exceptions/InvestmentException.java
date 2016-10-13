package aiad.feup.agent.exceptions;

import aiad.feup.agent.Player;

/**
 * Investment exception
 */
public class InvestmentException extends Exception {

    /**
     * Constructor of {@link InvestmentException}
     * @param player player that attempted to invest
     * @param message error message
     */
    public InvestmentException(final Player player, final String message) {
        super(player.getName() + " " + message);
    }

}
