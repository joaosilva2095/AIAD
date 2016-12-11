package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.messageObjects.EndGame;
import aiad.feup.messageObjects.Offer;
import aiad.feup.messageObjects.RoundInformation;
import aiad.feup.messageObjects.UpdatePlayer;
import aiad.feup.models.GameState;
import aiad.feup.models.PlayerType;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import java.text.DecimalFormat;
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

        // Apply fluctuations
        board.applyEndOfRoundFluctuation();

        // Calculate new player balances
        board.calculateBalances(acceptedOffers);

        // Apply kicks
        board.applyKicks();

        if(board.getCurrentRoundNumber() == Board.NUMBER_ROUNDS){
            //Broadcast updates
            Map<RemoteAgent, UpdatePlayer> playerUpdates = board.calculatePlayerUpdates(GameState.WAIT_WINNERS);
            for(RemoteAgent targetAgent : playerUpdates.keySet()){
                board.sendMessage(targetAgent, new ACLMessage(ACLMessage.INFORM), playerUpdates.get(targetAgent));
            }

            System.out.println("Reached the end of the game.");
            RemoteAgent winnerManager = null, winnerInvestor = null;
            double maxManagerBalance = Integer.MIN_VALUE, maxInvestorBalance = Integer.MIN_VALUE, balance;
            for(RemoteAgent agent : board.getPlayers()) {
                balance = board.getBalance(agent.getName());
                if(board.getType(agent.getName()) == PlayerType.INVESTOR) {
                    if(balance < maxInvestorBalance)
                        continue;
                    maxInvestorBalance = balance;
                    winnerInvestor = agent;
                } else {
                    if(balance < maxManagerBalance)
                        continue;
                    maxManagerBalance = balance;
                    winnerManager = agent;
                }
            }
            if(winnerInvestor == null || winnerManager == null)
                throw new IllegalStateException("Winners are null");

            // Disable integrity check
            CheckGameIntegrity.getInstance(board).stop();

            // Send winners
            DecimalFormat df = new DecimalFormat("#0.00");
            EndGame endGame = new EndGame("The game has ended. The investor winner is " + winnerInvestor.getName() + " (" + df.format(maxInvestorBalance) + "€) and the manager winner is " + winnerManager.getName() + " (" + df.format(maxManagerBalance) + "€)");
            for(RemoteAgent agent : board.getPlayers()) {
                board.sendMessage(agent, new ACLMessage(ACLMessage.INFORM), endGame);
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignore) {
            }
            board.killAgent("The game has ended. The investor winner is " + winnerInvestor.getName() + " (" + df.format(maxInvestorBalance) + "€) and the manager winner is " + winnerManager.getName() + " (" + df.format(maxManagerBalance) + "€)", 0);
            return;
        }
        board.incrementCurrentRound();

        // Assign new companies
        board.assignCompanies();

        //Broadcast updates
        Map<RemoteAgent, UpdatePlayer> playerUpdates = board.calculatePlayerUpdates(GameState.START_NEGOTIATION);
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
