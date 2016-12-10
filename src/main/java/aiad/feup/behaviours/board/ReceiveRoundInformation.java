package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.messageObjects.RoundInformation;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Receive Round Information behaviour
 * Handles the reception of the offers that occured during the round
 */
public class ReceiveRoundInformation extends SimpleBehaviour{

    /**
     * The singleton instance of the ReceiveRoundInformation behaviour
     */
    private static ReceiveRoundInformation instance;

    private ReceiveRoundInformation(){
    }

    public static ReceiveRoundInformation getInstance(){
        if(instance == null)
            instance = new ReceiveRoundInformation();
        return instance;
    }

    @Override
    public void action() {
        ACLMessage message = getAgent().blockingReceive();
        Board board = (Board) getAgent();

        Object content = board.extractMessageContentObject(message);

        if(!(content instanceof RoundInformation))
            return;

        RoundInformation roundInformation = (RoundInformation) content;

        System.out.println("Received round information from manager: " + message.getSender().getName());
        System.out.println("The round information contemplates " + roundInformation.getManagerOffers().size() + " offers");

    }

    @Override
    public boolean done() {
        return (((Board)getAgent()).getGameState() != GameState.END_NEGOTIATION);
    }



}
