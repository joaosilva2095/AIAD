package aiad.feup.messages;

import aiad.feup.models.PlayerType;
import jade.lang.acl.ACLMessage;

/**
 * SetupPlayer Message
 */
public class SetupPlayer extends ACLMessage {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8854382165581053448L;

    /**
     * The requested player type
     */
    private PlayerType playerType;

    /**
     * Constructor for the SetupPlayer ontology
     * @param playerType the requested player type
     */
    public SetupPlayer(int performative, PlayerType playerType) {
        super(performative);
        this.playerType = playerType;
    }

    /**
     * Get the player type
     * @return player type
     */
    public PlayerType getPlayerType() {
        return playerType;
    }
}
