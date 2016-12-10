package aiad.feup.messageObjects;

import java.io.Serializable;

/**
 * End game
 */
public class EndGame implements Serializable {

    /**
     * Message of the end game
     */
    final String message;

    /**
     * Constructor of KickPlayer
     * @param message message of the end game
     */
    public EndGame(String message) {
        this.message = message;
    }

    /**
     * Get the message
     * @return message of the end game
     */
    public String getMessage() {
        return message;
    }
}
