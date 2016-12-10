package aiad.feup.behaviours.player;

import aiad.feup.agents.Player;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.models.GameState;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

/**
 * Waiting for join confirmation behaviour
 */
public class WaitJoinConfirmation extends OneShotBehaviour {

    private static WaitJoinConfirmation instance;

    private static final long WAIT_TIMEOUT = 1000;

    private WaitJoinConfirmation(final Player player) { super(player); }

    public static WaitJoinConfirmation getInstance(final Player player) {
        if(instance == null)
            instance = new WaitJoinConfirmation(player);
        return instance;
    }

    @Override
    public void action() {
        final ACLMessage message = getAgent().blockingReceive(WAIT_TIMEOUT);
        final Player player = (Player)getAgent();

        //Check if no message was found
        if(message == null){
            player.killAgent("Join Game request timed out. Could not find board in time. Aborting...", 0);
            return;
        }

        //Message received, decode it
        if(message.getPerformative() == ACLMessage.CONFIRM) {
            System.out.println("Confirmation Received. Successfully joined the game.");
            player.setGameState(GameState.WAITING_GAME_START);
            player.setBoard(new RemoteAgent(message.getSender().getName()));
            player.addBehaviour(player.getFactory().wrap(WaitStartGame.getInstance(player)));
        } else if(message.getPerformative() == ACLMessage.REFUSE){
            player.killAgent("Connection refused by board. Game is maybe full already? Aborting...", 0);
        }

        return;
    }
}
