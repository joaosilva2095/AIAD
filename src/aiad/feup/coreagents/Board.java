<<<<<<< HEAD:src/aiad/feup/Core/Board.java
package aiad.feup.core;

import aiad.feup.agents.Investor;
import aiad.feup.agents.Manager;
import aiad.feup.agents.Player;
=======
package aiad.feup.coreagents;

import aiad.feup.agentstemp.Investor;
import aiad.feup.agentstemp.Manager;
import aiad.feup.agentstemp.Player;
>>>>>>> bbd2ae9ac49b0534f40319592a116478bb9402b1:src/aiad/feup/coreagents/Board.java

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Board singleton class.
 * Represents the board state and holds all information common throughout the elements of the board
 * Only 1 instance of Board may exist at any given time
 */
public class Board {

    /**
     * Players of the game
     */
    private Player[] players;

    /**
     * Companies of the game
     */
    private Company[] companies;

    /**
     * Constructor of Board
     * @param numberPlayers number of players that will play
     */
    public Board(final int numberPlayers) {
        this.players = new Player[numberPlayers];
    }

    /**
     * Get the number of players in the game
     * @return number of players in the game
     */
    public int getNumberPlayers() {
        return players.length;
    }

    /**
     * Get all the players in the game
     * @return all players in the game
     */
    public Player[] getPlayers() {
        return Arrays.copyOf(players, players.length);
    }

    /**
     * Get all managers in the game
     * @return all managers in the game
     */
    public Manager[] getManagers() {
        return Arrays.stream(players).filter(player -> player instanceof Manager).toArray(Manager[]::new);
    }

    /**
     * Get all investors in the game
     * @return all investors in the game
     */
    public Investor[] getInvestors() {
        return Arrays.stream(players).filter(player -> player instanceof Investor).toArray(Investor[]::new);
    }

    /**
     * Get all the companies in the game
     * @return all companies in the game
     */
    public Company[] getCompanies() {
        return Arrays.copyOf(companies, companies.length);
    }

    /**
     * Get the owner of a given company
     * @param company company to get the owner
     * @return owner of the company
     */
    public Player getCompanyOwner(final Company company) {
        for(final Player player : players) {
            if(!(player instanceof  Manager))
                continue;
            if(player.getCompanies().contains(company))
                return player;
        }
        return null;
    }

    /**
     * Returns companies that haven't been purchased
     * @return
     */
    public Company[] getOpenCompanies() {
        return Arrays.stream(companies).filter(company -> getCompanyOwner(company) == null).toArray(Company[]::new);
    }
}
