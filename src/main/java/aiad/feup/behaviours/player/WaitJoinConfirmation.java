package aiad.feup.behaviours.player;

import aiad.feup.agents.Player;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Wait for join the game confirmation
 */
public class WaitJoinConfirmation extends OneShotBehaviour {

    /**
     * Constructor of WaitJoinConfirmation
     * @param player player to wait for join confirmation
     */
    public WaitJoinConfirmation(final Player player) {
        super(player);
    }

    @Override
    public void action() {
        ACLMessage message = getAgent().blockingReceive(10000);
        if(message == null) {
            System.out.println("Could not join the game! No response from the board.");
            return;
        }

        if(message.getPerformative() != ACLMessage.CONFIRM) {
            System.out.println("Could not join the game! Invalid performative " + message.getPerformative());
            return;
        }

        System.out.println("Joined the game successfully!");
    }
}