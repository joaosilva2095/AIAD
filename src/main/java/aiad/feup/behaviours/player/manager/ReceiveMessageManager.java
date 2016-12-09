package aiad.feup.behaviours.player.manager;

import aiad.feup.agents.Player;
import aiad.feup.messages.Offer;
import aiad.feup.messages.UpdatePlayer;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Receive Message Behaviour. Continuously listens for messages from other agents
 */
public class ReceiveMessageManager extends SimpleBehaviour {

    private static ReceiveMessageManager instance;

    private ReceiveMessageManager(Player player) { super(player); }

    public static ReceiveMessageManager getInstance(Player player) {
        if(instance == null)
            instance = new ReceiveMessageManager(player);
        return instance;
    }

    @Override
    public void action() {
        ACLMessage message = getAgent().blockingReceive();
        Player player = (Player)getAgent();

        Object content = player.extractMessageContentObject(message);
        player.handleEndGame(content);
        if(content instanceof UpdatePlayer)
            return;

        System.out.println(content);

        // Received an Offer
        if(content instanceof Offer)
            player.addBehaviour(player.getFactory().wrap(HandleOffer.getInstance(player, (Offer) content)));

    }

    @Override
    public boolean done() {
        return ((Player)getAgent()).getGameState() != GameState.START_NEGOTIATION;
    }
}
