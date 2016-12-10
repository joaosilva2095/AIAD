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

    private final DFAgentDescription dfd;
    private final SearchConstraints sc;

    /**
     * Constructor of CheckGameIntegrity
     * @param board board to maintain integrity for
     *
     */
    private CheckGameIntegrity(final Board board, final DFAgentDescription dfd, final SearchConstraints sc){
        super(board, tickPeriod);
        this.dfd = dfd;
        this.sc = sc;
    }

    public static CheckGameIntegrity getInstance(final Board board, final DFAgentDescription dfd, final SearchConstraints sc) {
        if(instance == null)
            instance = new CheckGameIntegrity(board, dfd, sc);
        return instance;
    }

    @Override
    protected void onTick() {
        Board board = Board.getInstance();

        if(board.getGameState() == GameState.NONE || board.getGameState() == GameState.WAITING_GAME_START)
            return;


        AMSAgentDescription[] result = new AMSAgentDescription[0];
        try {
            SearchConstraints c = new SearchConstraints();
            c.setMaxResults((long) -1);
            result = AMSService.search(board, new AMSAgentDescription(), c);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        System.out.println(result.length-4 + " Active Players.");

        if (result.length < board.getNumberPlayers()+4){ //TODO remover este "magical number".  AMS retorna todos os agentes, incluindo a board, rma, ams e df
            System.out.println("Ending the game due to integrity breach: A player has quit the game.");

            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            EndGame endGame = new EndGame(" -- Game forcefully ended due to integrity breach.");

            for(RemoteAgent player : board.getPlayers()) {
                board.sendMessage(player, message, endGame);
            }
            System.exit(0);
        }
    }
}
