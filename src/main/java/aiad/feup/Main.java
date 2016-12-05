package aiad.feup;

import aiad.feup.agents.Board;
import aiad.feup.agents.GameAgent;
import aiad.feup.agents.Player;
import jade.core.PlatformManager;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.awt.EventQueue;
import java.io.File;

/**
 * Main class
 */
public class Main {

    /**
     * Game agent
     */
    private static GameAgent agent;

    /**
     * Main function of the game
     *
     * @param args arguments of the game
     */
    public static void main(String[] args) throws StaleProxyException {
        if (args.length < 3) {
            throw new IllegalArgumentException("Please use java -jar game.jar <host> <port> <board|player|simulate> [numberPlayers]");
        }

        // Get variables
        final String host = args[0],
                type = args[2];
        final int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (final Exception e) {
            throw new IllegalArgumentException("The number of players must be a natural number");
        }

        switch(type) {
            case "board":
                setupBoard(host, port);
                break;
            case "player":
                setupPlayer(host, port);
                break;
            case "simulate":
                if (args.length < 4) {
                    throw new IllegalArgumentException("Please use java -jar game.jar <host> <port> <board|player|simulate> [numberPlayers]");
                }

                int numberPlayers;
                try {
                    numberPlayers = Integer.parseInt(args[3]);
                    if(numberPlayers < 0)
                        throw new IllegalArgumentException("The number of players must be a natural number");
                } catch (final Exception e) {
                    throw new IllegalArgumentException("The number of players must be a natural number");
                }
                simulate(host, port, numberPlayers);
                break;
            default:
                throw new IllegalArgumentException("Please use java -jar game.jar <host> <port> <board|player|simulate> [numberPlayers]");
        }
    }

    private static void setupBoard(final String host, final int port) {
        agent = Board.getInstance();
        try {
            agent.init(host, port);
        } catch (StaleProxyException e) {
            System.out.println("Error! " + e.getMessage());
            return;
        }
    }

    private static void setupPlayer(final String host, final int port) {
        agent = Player.getInstance();
        try {
            agent.init(host, port);
        } catch (StaleProxyException e) {
            System.out.println("Error! " + e.getMessage());
            return;
        }
    }

    private static void simulate(final String host, final int port, final int numberPlayers) {

    }
}
