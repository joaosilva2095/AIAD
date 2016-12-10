package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.messageObjects.Offer;
import aiad.feup.messageObjects.RoundInformation;
import aiad.feup.messageObjects.UpdatePlayer;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Receive Round Information behaviour
 * Handles the reception of the offers that occured during the round
 */
public class ReceiveRoundInformation extends SimpleBehaviour{

    /**
     * The singleton instance of the ReceiveRoundInformation behaviour
     */
    private static ReceiveRoundInformation instance;

    /**
     * List with all accepted offers of the managers
     */
    private Map<RemoteAgent, List<Offer>> acceptedOffers;

    /**
     * The number of answers that have been received this round.
     */
    private int numberAnswers;

    private ReceiveRoundInformation(){
        acceptedOffers = new HashMap<>();
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
        acceptedOffers.put(board.getPlayer(message.getSender().getName()), roundInformation.getManagerOffers());

        //Increment the number of received answers
        numberAnswers++;
        if(numberAnswers != board.getNumberManagers())
            return;

        System.out.println("Received all the information of the managers!");

        Map<String, Double> currentCompaniesValue = new HashMap<>();
        for(Company company : board.getCompanies())
            currentCompaniesValue.put(company.getName(), company.getValue());

        // Apply fluctuations
        board.applyEndOfRoundFluctuation();

        // Calculate new player balances
        board.calculateBalances(acceptedOffers, currentCompaniesValue);

        if(board.getCurrentRoundNumber() == Board.NUMBER_ROUNDS){
            System.out.println("Reached the maximum number of rounds.");
            //Calculate Winner and broadcast
            return;
        }
        board.incrementCurrentRound();

        // Assign new companies
        board.assignCompanies();

        //Broadcast updates
        Map<RemoteAgent, UpdatePlayer> playerUpdates = board.calculatePlayerUpdates();
        for(RemoteAgent targetAgent : playerUpdates.keySet()){
            board.sendMessage(targetAgent, new ACLMessage(ACLMessage.INFORM), playerUpdates.get(targetAgent));
        }

        board.setGameState(GameState.START_NEGOTIATION);
        board.addBehaviour(board.getFactory().wrap(ManageNegotiation.getInstance()));
    }

    @Override
    public boolean done() {
        return (((Board)getAgent()).getGameState() != GameState.END_NEGOTIATION);
    }

    public void setNumberAnswers(int numberAnswers){
        this.numberAnswers = numberAnswers;
    }

}
