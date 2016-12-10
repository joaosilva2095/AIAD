package aiad.feup.messageObjects;

import java.io.Serializable;

/**
 * Kick the player
 */
public class KickPlayer implements Serializable {

    /**
     * Message of the kick
     */
    final String message;

    /**
     * Constructor of KickPlayer
     * @param message message of the kick player
     */
    public KickPlayer(String message) {
        this.message = message;
    }

    /**
     * Get the message
     * @return message of the kick
     */
    public String getMessage() {
        return message;
    }
}
