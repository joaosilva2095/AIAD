package aiad.feup.exceptions;

import aiad.feup.agent.Player;

/**
 * Balance exception
 */
public class BalanceException extends RuntimeException {

    /**
     * Constructor of {@link BalanceException}
     * @param player player where the error occurred
     * @param message error message
     */
    public BalanceException(final Player player, final String message) {
        super(player.getName() + " " + message);
    }
}
