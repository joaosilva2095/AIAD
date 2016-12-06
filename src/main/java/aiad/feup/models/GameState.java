package aiad.feup.models;

/**
 * Player possible states
 */
public enum GameState {

    /**
     * No game state
     */
    NONE,

    /**
     * Waiting for a join confirmation
     */
    WAITING_JOIN_CONFIRMATION,

    /**
     * Waiting for the game start
     */
    WAITING_GAME_START,

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
     * Kicked
     */
    KICKED,

    /**
     * End the game
     */
    END_GAME
}
