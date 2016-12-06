package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.messages.EndGame;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.Scanner;

/**
 * Read command from the user
 */
public class ReadCommand extends SimpleBehaviour {

    private static ReadCommand instance;

    /**
     * Constructor of ReadCommand
     * @param board board for reading commands
     */
    private ReadCommand(final Board board){
        super(board);
    }

    public static ReadCommand getInstance(final Board board){
        if(instance == null)
            instance = new ReadCommand(board);
        return instance;
    }

    /**
     * Wait for player inputs
     */
    @Override
    public void action() {
        final Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        Board board = (Board)getAgent();
        switch(command.toLowerCase()) {
            case "start":
                if(board.getGameState() != GameState.WAITING_GAME_START){
                    System.out.println("Can't start the game when a game already underway.");
                    return;
                }

                if(board.getNumberPlayers() < 3) {
                    System.out.println("Cannot start the game with less than 3 players.");
                    return;
                }

                System.out.println("Starting the game!");
                board.assignRoles();
                WaitForPlayers wfpInstance = WaitForPlayers.getInstance();
                System.out.println(wfpInstance);
                board.removeBehaviour(wfpInstance);
                System.out.println("No longer listening for players.");

                board.setGameState(GameState.START_AUCTION);
                break;
            case "end":
                System.out.println("Ending the game!");
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                EndGame endGame = new EndGame(" -- Game forcefully ended by console.");
                for(RemoteAgent agent : board.getPlayers()) {
                    board.sendMessage(agent, message, endGame);
                }
                break;
            default:
                System.out.println("Unknown command.");
                break;
        }
    }

    /**
     * Check if the behaviour is complete
     * @return true if state is end game
     */
    @Override
    public boolean done() {
        return ((Board)getAgent()).getGameState() == GameState.END_GAME;
    }
}
