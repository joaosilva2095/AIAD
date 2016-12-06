package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import jade.core.AID;
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


    private static WaitForPlayers instance;

    /**
     * Constructor of WaitForPlayers behaviour
     * @param board board that will wait for players
     * @param dfd template for agents to be searched
     * @param sc search constraints for new players
     */
    private WaitForPlayers(final Board board, final DFAgentDescription dfd, final SearchConstraints sc){
        super(board, DFService.createSubscriptionMessage(board, board.getDefaultDF(), dfd, sc));
    }

    /**
     * Get Instance. Returns the instance of the Behaviour or makes a new one if not yet initialized
     * @param board the board to add the behaviour to
     * @param dfd the dfd to scout for players
     * @param sc the search constraint for the search for players
     * @return the WaitForPlayers behaviour instance
     */
    public static WaitForPlayers getInstance(Board board, DFAgentDescription dfd, SearchConstraints sc){
        if (instance == null)
            instance = new WaitForPlayers(board, dfd, sc);
        return instance;
    }

    /**
     * Get Instance. Returns the instance of the board, without guaranteeing it's initialization. Can return null.
     * Useful for getting the instance without needing to reconstruct it if you believe it has already been initialized.
     * Check the return value.
     * @return the instance. Can be null.
     */
    public static WaitForPlayers getInstance(){
        return instance;
    }

    /**
     * Handle informs of new DF registrations
     * @param inform inform message received
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println("Waiting for players");
        try {
            final DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
            if(results.length < 0)
                return;

            final Board board = (Board)getAgent();
            for (final DFAgentDescription dfd : results) {
                final AID playerAID = dfd.getName();
                final RemoteAgent player = new RemoteAgent(playerAID.getName());
                board.addPlayer(player);

                board.sendMessage(player, new ACLMessage(ACLMessage.CONFIRM), null);
                System.out.println("Player " + playerAID.getLocalName() + " joined the game (" + board.getNumberPlayers() + ")");
            }
        } catch (FIPAException e) {
            System.out.println("Error while waiting for players to join the game! " + e.getMessage());
        }
    }
}
