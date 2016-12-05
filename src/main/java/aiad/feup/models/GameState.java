package aiad.feup.models;

/**
 * Player possible states
 */
public enum GameState {

    /**
     * Waiting for new instructions
     */
    IDLE,

    /**
     * Started negotiating
     */
    START_NEGOTIATION,

    /**
     * Ended negotiating
     */
    END_NEGOTIATION,

    /**
     * Started auctioning
     */
    START_AUCTION,

    /**
     * Ended auctioning
     */
    END_AUCTION,

    /**
     * End the game
     */
    END_GAME
}
