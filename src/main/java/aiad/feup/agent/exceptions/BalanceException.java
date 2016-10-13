package aiad.feup.agent.exceptions;

import aiad.feup.agent.Player;

/**
 * Balance exception
 */
public class BalanceException extends Exception {

    /**
     * Constructor of {@link BalanceException}
     * @param player player where the error occurred
     * @param message error message
     */
    public BalanceException(final Player player, final String message) {
        super(player.getName() + " " + message);
    }
}
