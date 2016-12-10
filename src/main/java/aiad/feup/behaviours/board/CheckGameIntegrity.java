package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.messageObjects.EndGame;
import aiad.feup.models.GameState;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * Cyclic Behaviour that constantly validates if the game's integrity holds.
 * Aborts the game should a member disconnect abruptly or any other integrity contrains are broken.
 */
public class CheckGameIntegrity extends TickerBehaviour{

    private static final long tickPeriod = 5000;
    private static CheckGameIntegrity instance;

    /**
     * Constructor of CheckGameIntegrity
     * @param board board to maintain integrity for
     *
     */
    private CheckGameIntegrity(final Board board){
        super(board, tickPeriod);
    }

    public static CheckGameIntegrity getInstance(final Board board) {
        if(instance == null)
            instance = new CheckGameIntegrity(board);
        return instance;
    }

    @Override
    protected void onTick() {
        Board board = (Board) getAgent();

        if(board.getGameState() == GameState.NONE || board.getGameState() == GameState.WAITING_GAME_START)
            return;

        AMSAgentDescription[] result = null;
        try {
            SearchConstraints c = new SearchConstraints();
            c.setMaxResults((long) -1);
            result = AMSService.search(board, new AMSAgentDescription(), c);
        } catch (FIPAException e) {
            System.out.println("Error while getting online players: " + e.getMessage());
        }

        if(result == null)
            return;

        int numberOnlinePlayers = result.length - 4;

        if (numberOnlinePlayers < board.getNumberPlayers()){
            EndGame endGame = new EndGame(" -- Game forcefully ended due to integrity breach.");

            for(RemoteAgent player : board.getPlayers()) {
                board.sendMessage(player, new ACLMessage(ACLMessage.INFORM), endGame);
            }
            board.killAgent("Ending the game due to integrity breach: A player has quit the game.", 0);
        }
    }
}
