package aiad.feup;

import aiad.feup.agents.Board;
import aiad.feup.agents.GameAgent;
import aiad.feup.agents.Player;
import aiad.feup.models.PlayerStyle;
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
                if (args.length < 7) {
                    throw new IllegalArgumentException("Please use java -jar game.jar <mainHost> <mainPort> <board|player> <nickname> <high|low|random> <localHost> <localPort>");
                }

                final String nickname = args[3],
                        playerStyle = args[4],
                        localHost = args[5];
                final int localPort;
                try {
                    localPort = Integer.parseInt(args[6]);
                } catch (final Exception e) {
                    throw new IllegalArgumentException("The port must be a natural number");
                }

                setupPlayer(nickname, playerStyle, mainHost, mainPort, localHost, localPort);
                break;
            default:
                throw new IllegalArgumentException("Please use java -jar game.jar <host> <port> <board|player> [numberPlayers]");
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

    private static void setupPlayer(final String nickname, final String playerStyle, final String mainHost, final int mainPort, final String localHost, final int localPort) {
        agent = Player.getInstance();
        try {
            Player player = (Player) agent;
            player.init(nickname, mainHost, mainPort, localHost, localPort);
            switch(playerStyle.toLowerCase()) {
                case "high":
                    player.setStyle(PlayerStyle.HIGH_RISK);
                    break;
                case "low":
                    player.setStyle(PlayerStyle.LOW_RISK);
                    break;
                case "random":
                    player.setStyle(PlayerStyle.RANDOM);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown player style: " + playerStyle);
            }
        } catch (StaleProxyException e) {
            System.out.println("That nickname is already in use. Please choose another one.");
            return;
        }
    }
}
