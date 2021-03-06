package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.messageObjects.UpdatePlayer;
import aiad.feup.models.GameState;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * The manage engotiation behaviour. Counts down the time until a round of negotiation ends
 */
public class ManageNegotiation extends OneShotBehaviour {

    /**
     * The singleton instance of the ManageNegotiation behaviours
     */
    private static ManageNegotiation instance;

    /**
     * The total duration of the negotiation round
     */
    private int roundDuration;

    private ManageNegotiation(){
    }

    public static ManageNegotiation getInstance(){
        if(instance == null)
            instance = new ManageNegotiation();
        return instance;
    }

    public void setRoundDuration(int roundDuration){
        this.roundDuration = roundDuration;
    }

    /**
     * Sleeps until the negotiation round is finished
     */
    @Override
    public void action() {
        long durationInMillis = roundDuration * 1000;

        long start = System.currentTimeMillis();
        long end = start;
        long sleepTime;
        while((end-start) < durationInMillis) {
            sleepTime = durationInMillis - (end - start);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignore) {
            }
            end = System.currentTimeMillis();
        }

        Board board = (Board)getAgent();
        board.setGameState(GameState.END_NEGOTIATION);
        ReceiveRoundInformation rriInstance = ReceiveRoundInformation.getInstance();
        rriInstance.setNumberAnswers(0);
        board.addBehaviour(board.getFactory().wrap(rriInstance));
        endNegotiationRound();
    }

    /**
     * Broadcast to all players that the negotiation round has ended
     */
    private void endNegotiationRound(){
        System.out.println("Ending the negotiation!");
        Board board = (Board) getAgent();
        UpdatePlayer updatePlayerMessage;
        for(RemoteAgent targetAgent : board.getPlayers()){
            updatePlayerMessage = new UpdatePlayer(0, 0, null, 0, 0, GameState.END_NEGOTIATION);
            board.sendMessage(targetAgent, new ACLMessage(ACLMessage.INFORM), updatePlayerMessage);
        }

    }
}
