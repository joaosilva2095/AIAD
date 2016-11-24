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
     * @param args arguments of the game
     */
    public static void main(String[] args) {
        if(args[0].equalsIgnoreCase("BOARD")) {
            agent = new Board();
        } else if(args[0].equalsIgnoreCase("PLAYER")) {
            agent = new Player();
        }
    }
}
