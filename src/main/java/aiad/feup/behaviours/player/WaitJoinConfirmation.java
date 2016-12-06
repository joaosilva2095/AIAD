package aiad.feup.behaviours.player;

import aiad.feup.agents.Player;
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
            killAgent("Join Game request timed out. Could not find board in time. Aborting...", 0);
            return;
        }

        //Message received, decode it
        if(message.getPerformative() == ACLMessage.CONFIRM) {
            System.out.println("Confirmation Received. Successfully joined the game.");
            player.setGameState(GameState.WAITING_GAME_START);
            player.addBehaviour(player.getFactory().wrap(WaitStartGame.getInstance(player)));
        } else if(message.getPerformative() == ACLMessage.REFUSE){
            killAgent("Connection refused by board. Game is maybe full already? Aborting...", 0);
        }

        return;
    }

    /**
     * Kill agent method. Attempts to kill the agent.
     * If agent could not be killed, exits with status -1
     * @param message Message to convey to user.
     * @param errorStatus Error status for the program to exit with.
     */
    private void killAgent(String message, int errorStatus){
        final Player player = (Player)getAgent();
        System.out.println(message);
        try {
            player.getContainerController().kill();
            player.takeDown();
            System.exit(errorStatus);
        } catch (StaleProxyException e) {
            System.out.println("Failed to kill container. Force Aborting. Start praying...");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
