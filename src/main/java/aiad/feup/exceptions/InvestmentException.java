package aiad.feup.exceptions;

import aiad.feup.agents.Player;

/**
 * Investment exception
 */
public class InvestmentException extends RuntimeException {

    /**
     * Constructor of {@link InvestmentException}
     * @param player player that attempted to invest
     * @param message error message
     */
    public InvestmentException(final Player player, final String message) {
        super(player.getName() + " " + message);
    }

}
