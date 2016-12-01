package aiad.feup.messages;

import jade.lang.acl.ACLMessage;

/**
 * Kick the player
 */
public class KickPlayer extends ACLMessage {

    /**
     * Message of the kick
     */
    final String message;

    /**
     * Constructor of KickPlayer
     * @param message message of the kick player
     */
    public KickPlayer(int performative, String message) {
        super(performative);
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
