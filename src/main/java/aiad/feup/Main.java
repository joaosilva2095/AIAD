package aiad.feup;

import aiad.feup.agents.Board;
import aiad.feup.agents.GameAgent;
import aiad.feup.agents.Player;

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
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Please use java -jar game.jar <BOARD|PLAYER|SIMULATE> [numberPlayers]");
        }

        if (args[0].equalsIgnoreCase("BOARD")) {
            if (args.length != 1) {
                throw new IllegalArgumentException("Please use java -jar game.jar <BOARD|PLAYER|SIMULATE> [numberPlayers]");
            }

            agent = new Board();
        } else if (args[0].equalsIgnoreCase("PLAYER")) {
            if (args.length != 1) {
                throw new IllegalArgumentException("Please use java -jar game.jar <BOARD|PLAYER|SIMULATE> [numberPlayers]");
            }

            agent = new Player();
        } else if (args[0].equalsIgnoreCase("SIMULATE")) {
            if (args.length != 2) {
                throw new IllegalArgumentException("Please use java -jar game.jar <BOARD|PLAYER|SIMULATE> [numberPlayers]");
            }

            // Number of players
            int numberPlayers;
            try {
                numberPlayers = Integer.parseInt(args[1]);
                if(numberPlayers < 0)
                    throw new IllegalArgumentException("The number of players must be a natural number");
            } catch (final Exception e) {
                throw new IllegalArgumentException("The number of players must be a natural number");
            }

            agent = new Board();
        }
    }
}
