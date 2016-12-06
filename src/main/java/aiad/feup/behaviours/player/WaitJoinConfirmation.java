package aiad.feup.behaviours.player;

import aiad.feup.agents.Player;
import aiad.feup.models.GameState;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Waiting for join confirmation behaviour
 */
public class WaitJoinConfirmation extends OneShotBehaviour {

    private static WaitJoinConfirmation instance;

    private static final long WAIT_TIMEOUT = 100000;

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

        if(message.getPerformative() == ACLMessage.CONFIRM) {
            System.out.println("Confirmation Received. Successfully joined the game.");
            player.setGameState(GameState.WAITING_GAME_START);
            player.addBehaviour(player.getFactory().wrap(WaitStartGame.getInstance(player)));
        } else if(message.getPerformative() == ACLMessage.REFUSE){
            System.out.println("Attempt to join the game was refused. Aborting...");
            System.exit(0);
        }
    }
}
