package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.models.GameState;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;


/**
 * Receive Message Behaviour
 */
public class WaitForPlayers extends SubscriptionInitiator {

    /**
     * Constructor of WaitForPlayers behaviour
     * @param board board that will wait for players
     * @param dfd template for agents to be searched
     * @param sc search constraints for new players
     */
    public WaitForPlayers(final Board board, final DFAgentDescription dfd, final SearchConstraints sc){
        super(board, DFService.createSubscriptionMessage(board, board.getDefaultDF(), dfd, sc));
    }

    /**
     * Handle informs of new DF registrations
     * @param inform inform message received
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            final DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
            if(results.length < 0)
                return;

            final Board board = (Board)getAgent();
            for (final DFAgentDescription dfd : results) {
                final AID playerAID = dfd.getName();
                final RemoteAgent player = new RemoteAgent(playerAID.getName());
                board.addPlayer(player);

                board.sendMessage(player, new ACLMessage(ACLMessage.CONFIRM));
                System.out.println("Player " + playerAID.getLocalName() + " joined the game (" + board.getNumberPlayers() + ")");
            }
        } catch (FIPAException e) {
            System.out.println("Error while waiting for players to join the game! " + e.getMessage());
        }
    }
}
