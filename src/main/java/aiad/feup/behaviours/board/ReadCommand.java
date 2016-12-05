package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.messages.EndGame;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Scanner;

/**
 * Read command from the user
 */
public class ReadCommand extends SimpleBehaviour {

    /**
     * Constructor of ReadCommand
     * @param board board for reading commands
     */
    public ReadCommand(final Board board){
        super(board);
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
                System.out.println("Starting the game!");
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
