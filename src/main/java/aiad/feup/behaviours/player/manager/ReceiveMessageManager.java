package aiad.feup.behaviours.player.manager;

import aiad.feup.agents.Player;
import aiad.feup.messageObjects.Offer;
import aiad.feup.messageObjects.UpdatePlayer;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Receive Message Behaviour. Continuously listens for messageObjects from other agents
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

        switch(player.getGameState()){
            case START_NEGOTIATION:
                // Received Update Player (round end)
                if(content instanceof UpdatePlayer) {
                    UpdatePlayer updatePlayer = (UpdatePlayer) content;
                    System.out.println("Received update. " + updatePlayer.getState());
                    player.setGameState(updatePlayer.getState());
                    player.addBehaviour(player.getFactory().wrap(SendRoundInformation.getInstance()));
                }
                // Received an Offer
                if(content instanceof Offer)
                    player.addBehaviour(player.getFactory().wrap(new HandleOffer(player, (Offer) content)));


                break;
            case END_NEGOTIATION:
                // Received Update Player (new negotiation about to start)
                if(content instanceof UpdatePlayer) {
                    UpdatePlayer updatePlayer = (UpdatePlayer) content;
                    System.out.println("Received update. " + updatePlayer.getState());
                    player.setGameState(updatePlayer.getState());
                }

                break;
            default:
                break;
        }


    }

    @Override
    public boolean done() {
        GameState state = ((Player)getAgent()).getGameState();
        return state != GameState.START_NEGOTIATION && state != GameState.END_NEGOTIATION;    }
}
