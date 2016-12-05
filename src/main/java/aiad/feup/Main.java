package aiad.feup;

import aiad.feup.agents.Board;
import aiad.feup.agents.GameAgent;
import aiad.feup.agents.Player;
import jade.wrapper.StaleProxyException;

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
            throw new IllegalArgumentException("Please use java -jar game.jar <host> <port> <board|player|simulate>");
        }

        // Get variables
        final String mainHost = args[0],
                type = args[2];
        final int mainPort;
        try {
            mainPort = Integer.parseInt(args[1]);
        } catch (final Exception e) {
            throw new IllegalArgumentException("The port must be a natural number");
        }

        switch(type) {
            case "board":
                setupBoard(mainHost, mainPort);
                break;
            case "player":
                if (args.length < 6) {
                    throw new IllegalArgumentException("Please use java -jar game.jar <mainHost> <mainPort> <board|player|simulate> <nickname> <localHost> <localPort>");
                }

                final String nickname = args[3],
                        localHost = args[4];
                final int localPort;
                try {
                    localPort = Integer.parseInt(args[5]);
                } catch (final Exception e) {
                    throw new IllegalArgumentException("The port must be a natural number");
                }

                setupPlayer(nickname, mainHost, mainPort, localHost, localPort);
                break;
            case "simulate":
                if (args.length < 4) {
                    throw new IllegalArgumentException("Please use java -jar game.jar <host> <port> <board|player|simulate> <numberPlayers>");
                }

                int numberPlayers;
                try {
                    numberPlayers = Integer.parseInt(args[3]);
                    if(numberPlayers < 0)
                        throw new IllegalArgumentException("The number of players must be a natural number");
                } catch (final Exception e) {
                    throw new IllegalArgumentException("The number of players must be a natural number");
                }
                simulate(mainHost, mainPort, numberPlayers);
                break;
            default:
                throw new IllegalArgumentException("Please use java -jar game.jar <host> <port> <board|player|simulate> [numberPlayers]");
        }
    }

    private static void setupBoard(final String host, final int port) {
        agent = Board.getInstance();
        try {
            ((Board) agent).init(host, port);
        } catch (StaleProxyException e) {
            System.out.println("Error! " + e.getMessage());
            return;
        }
    }

    private static void setupPlayer(final String nickname, final String mainHost, final int mainPort, final String localHost, final int localPort) {
        agent = Player.getInstance();
        try {
            ((Player) agent).init(nickname, mainHost, mainPort, localHost, localPort);
        } catch (StaleProxyException e) {
            System.out.println("That nickname is already in use. Please choose another one.");
            return;
        }
    }

    private static void simulate(final String host, final int port, final int numberPlayers) {

    }
}
