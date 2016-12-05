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

    public WaitForPlayers(final Board board, final DFAgentDescription dfd, final SearchConstraints sc){
        super(board, DFService.createSubscriptionMessage(board, board.getDefaultDF(), dfd, sc));
    }

    /**
     * Handle informs of new DF registrations
     * @param inform
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            final DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
            if(results.length < 0)
                return;

            for (final DFAgentDescription dfd : results) {
                final AID player = dfd.getName();
                ((Board) getAgent()).addPlayer(new RemoteAgent(player.getName()));
                System.out.println("Player " + player.getLocalName() + " joined the game (" + ((Board) getAgent()).getNumberPlayers() + ")");
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
