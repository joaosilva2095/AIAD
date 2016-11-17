package aiad.feup.messages;

import jade.util.leap.Serializable;

/**
 * SetupPlayer Message
 */
public class SetupPlayer extends Message {

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
    public SetupPlayer(PlayerType playerType){
        this.playerType = playerType;
    }


}
