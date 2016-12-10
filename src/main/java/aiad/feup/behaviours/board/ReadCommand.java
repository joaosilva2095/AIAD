package aiad.feup.behaviours.board;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.messageObjects.EndGame;
import aiad.feup.messageObjects.UpdatePlayer;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

import java.util.Map;
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
                    System.out.println("Can't start the game with a game already underway.");
                    return;
                }

                if(board.getNumberPlayers() < 3) {
                    System.out.println("Cannot start the game with less than 3 players.");
                    return;
                }

                System.out.println("Starting the game!");
                WaitForPlayers wfpInstance = WaitForPlayers.getInstance();
                wfpInstance.cancel(board.getDefaultDF(), true);
                board.removeBehaviour(wfpInstance);
                System.out.println("No longer accepting new players.");

                board.setupPlayers();
                Map<RemoteAgent, UpdatePlayer> playerUpdates = board.calculatePlayerUpdates();

                //Broadcast messageObjects
                for(RemoteAgent targetAgent : playerUpdates.keySet()){
                    board.sendMessage(targetAgent, new ACLMessage(ACLMessage.INFORM), playerUpdates.get(targetAgent));
                }

                board.setGameState(GameState.START_NEGOTIATION);
                board.setCurrentRoundNumber(1);
                ManageNegotiation.getInstance().setRoundDuration(Board.ROUND_DURATION);
                board.addBehaviour(board.getFactory().wrap(ManageNegotiation.getInstance()));
                break;
            case "end":
                System.out.println("Ending the game!");
                EndGame endGame = new EndGame(" -- Game forcefully ended by console.");
                for(RemoteAgent agent : board.getPlayers()) {
                    board.sendMessage(agent, new ACLMessage(ACLMessage.INFORM), endGame);
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignore) {
                }
                board.killAgent("Players have been informed. Game ending.", 0);
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
