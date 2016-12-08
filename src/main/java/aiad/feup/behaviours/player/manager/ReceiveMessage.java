package aiad.feup.behaviours.player.manager;

import aiad.feup.agents.Player;
import aiad.feup.messages.Offer;
import aiad.feup.models.GameState;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Receive Message Behaviour. Continuously listens for messages from other agents
 */
public class ReceiveMessage extends SimpleBehaviour {

    private static ReceiveMessage instance;

    private ReceiveMessage(Player player) { super(player); }

    public static ReceiveMessage getInstance(Player player) {
        if(instance == null)
            instance = new ReceiveMessage(player);
        return instance;
    }

    @Override
    public void action() {
        System.out.println("Receiving messages");
        ACLMessage message = getAgent().blockingReceive();
        Player player = (Player)getAgent();

        System.out.println("Received a message");
        Object content = player.extractMessageContentObject(message);
        System.out.println("HELLO?!??!?!?!?");
        player.handleEndGame(content);
        System.out.println("Not stuck at handle end game");
        System.out.println(content);

        // Received an Offer
        if(content instanceof Offer) {
            System.out.println("Adding handle offer behaviour.");
            player.addBehaviour(player.getFactory().wrap(HandleOffer.getInstance(player, (Offer) content)));
        }
    }

    @Override
    public boolean done() {
        return ((Player)getAgent()).getGameState() != GameState.START_AUCTION;
    }
}
