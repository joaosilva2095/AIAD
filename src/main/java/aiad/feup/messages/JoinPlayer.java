package aiad.feup.messages;

import jade.lang.acl.ACLMessage;

/**
 * Join the game
 */
public class JoinPlayer extends ACLMessage {

    public JoinPlayer(int performative) {
        super(performative);

    }
}
